package com.store.api.service;

import com.store.api.controller.PhotoController;
import com.store.api.impl.AppImageUtilImpl;
import com.store.api.model.product.Product;
import com.store.api.model.user.AppUser;
import com.store.api.model.util.AppImage;
import com.store.api.model.util.PaymentMethod;
import com.store.api.repository.AppImageRepository;
import com.store.api.repository.PaymentMethodRepository;
import com.store.api.util.ImageStorageLocation;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService  {

    private final AppImageRepository appImageRepository;
    private final ImageStorageLocation imageStorageLocation;
    private final AppImageUtilImpl appImageUtil;
    private final ProductService productService;
    private final UserDetailsServiceImpl userDetailsService;
    private final PaymentMethodRepository paymentMethodRepository;

    public PhotoService(AppImageRepository appImageRepository, ImageStorageLocation imageStorageLocation,
                        AppImageUtilImpl appImageUtil,
                        ProductService productService, UserDetailsServiceImpl userDetailsService,
                        PaymentMethodRepository paymentMethodRepository) {
        this.appImageRepository = appImageRepository;
        this.imageStorageLocation = imageStorageLocation;
        this.appImageUtil = appImageUtil;
        this.productService = productService;
        this.userDetailsService = userDetailsService;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public void save(MultipartFile file, Long userId){
        AppUser user =userDetailsService.getUserById(userId);
        AppImage image = AppImage.builder()
                .name(file.getOriginalFilename())
                .contentType(file.getContentType())
                .path(imageStorageLocation.getLocation()+"/"+user.getUsername()+"-"+file.getOriginalFilename())
                .appUser(user)
                .build();
         appImageRepository.save(image);
        appImageUtil.store(file, user.getUsername());
    }

    public void savePaymentMethodImage(MultipartFile file, String paymentMethod){
        PaymentMethod payment = paymentMethodRepository.findByPaymentMethod(paymentMethod).orElse(null);
        if(payment!=null && !file.isEmpty()){
            AppImage image = AppImage.builder()
                    .name(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .path(imageStorageLocation.getLocation()+"/"+payment.getPaymentMethod()+"-"+file.getOriginalFilename())
                    .paymentId(payment.getId())
                    .build();
            appImageRepository.save(image);
            appImageUtil.store(file, paymentMethod);
        }

    }



    public void save(MultipartFile[] files,Long productId){
        Arrays.stream(files).forEach(file -> {
            Product product = productService.getProductById(productId);
            AppImage image = AppImage.builder()
                    .name(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .path(imageStorageLocation.getLocation()+"/"+product.getName()+"-"+file.getOriginalFilename())
                    .productId(product.getId())
                    .build();
            appImageRepository.save(image);
            appImageUtil.store(file, product.getName());
        });

    }
    public void saveProductPhoto(MultipartFile file,Long productId){
        Product product = productService.getProductById(productId);
        AppImage image = AppImage.builder()
                .name(file.getOriginalFilename())
                .contentType(file.getContentType())
                .path(imageStorageLocation.getLocation()+"/"+product.getId()+"-"+file.getOriginalFilename())
                .productId(product.getId())
                .build();
        appImageRepository.save(image);
        appImageUtil.store(file, String.valueOf(product.getId()));
    }

    public Resource getResource(String fileName){
       return appImageUtil.loadAsResource(fileName);
    }

    public List<String> getProductImages(Long productId){
        List<String> urls = new ArrayList<>();
        for(AppImage image: appImageRepository.findByProductId(productId)){
            Resource uri = appImageUtil.loadAsResource(image.getPath());
            urls.add(MvcUriComponentsBuilder.fromMethodName(PhotoController.class, "serveFile",
                    uri.getFilename()).build().toUri().toString());
        }
        return urls;
    }


    public String getUserImage(Long userId){
        AppImage userImage = appImageRepository.findById(userId)
                .orElse(null);
        if(userImage!=null){

            Resource uri = appImageUtil.loadAsResource(userImage.getPath());
            return  MvcUriComponentsBuilder.fromMethodName(PhotoController.class, "serveFile",
                    uri.getFilename()).build().toUri().toString();
        }
        return null;
    }

    public String getPaymentImage(Long id){
        AppImage paymentMethodImage = appImageRepository.findByPaymentMethodId(id)
                .orElse(null);
        if(paymentMethodImage!=null){
            Resource uri = appImageUtil.loadAsResource(paymentMethodImage.getPath());
            return  MvcUriComponentsBuilder.fromMethodName(PhotoController.class, "serveFile",
                    uri.getFilename()).build().toUri().toString();
        }
       return null;
    }

    public String deleteImage(String filename){
        //check whether file exist in db
        Optional<AppImage> image =appImageRepository.findByPath(imageStorageLocation.getLocation()+"/"+filename);
        if(image.isPresent()){
            appImageRepository.deleteByPath(imageStorageLocation.getLocation()+"/"+filename);
            appImageUtil.delete(imageStorageLocation.getLocation()+"/"+filename);
            return filename+" deleted";
        }
       return filename+" does not exist";
    }

}
