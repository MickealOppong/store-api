package com.store.api.service;

import com.store.api.exceptions.InvalidOperationException;
import com.store.api.exceptions.ProductNotFoundException;
import com.store.api.model.product.*;
import com.store.api.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PhotoService photoService;
    private final ProductAttributeService productAttributeService;
    private final ProductCategoryService productCategoryService;
    private final FavouriteService favouriteService;
    public ProductService(ProductRepository productRepository, @Lazy PhotoService photoService,
                          ProductAttributeService productAttributeService,
                          ProductCategoryService productCategoryService,
                          @Lazy FavouriteService favouriteService) {
        this.productRepository = productRepository;
        this.photoService = photoService;
        this.productAttributeService = productAttributeService;
        this.productCategoryService = productCategoryService;
        this.favouriteService =  favouriteService;
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("Could not find the requested product id: "+id));
    }

    //select products with featuredProduct=true
    public List<SingleProductDTO> featuredProducts(){
    List<SingleProductDTO> featuredProducts = new ArrayList<>();
        for(Product product: productRepository.getFeaturedProducts()){
            SingleProductDTO singleProductDTO = SingleProductDTO.builder()
                    .name(product.getName())
                    .description(product.getDescription())
                    .quantity(product.getQuantity())
                    .price(product.getPrice())
                    .isFreeShipping(product.isFreeShipping())
                    .build();
          featuredProducts.add(singleProductDTO);
        }
        return  featuredProducts;
    }

    //select products with new arrival=true
    public List<SingleProductDTO> newArrivals(){
        List<SingleProductDTO> newArrivals = new ArrayList<>();
        for(Product product: productRepository.getNewArrivalProducts()){
            SingleProductDTO singleProductDTO = SingleProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .quantity(product.getQuantity())
                    .price(product.getPrice())
                    .isFreeShipping(product.isFreeShipping())
                    .productImages(photoService.getProductImages(product.getId()))
                    .build();

            newArrivals.add(singleProductDTO);
        }
        return  newArrivals;
    }

    //select products with free shipping=true
    public List<SingleProductDTO> getFreeShippingProducts(){
        String  username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<SingleProductDTO> newArrivals = new ArrayList<>();
        for(Product product: productRepository.getNewArrivalProducts()){
            SingleProductDTO singleProductDTO = SingleProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .quantity(product.getQuantity())
                    .price(product.getPrice())
                    .isFreeShipping(product.isFreeShipping())
                    .productImages(photoService.getProductImages(product.getId()))
                    .build();
            newArrivals.add(singleProductDTO);
        }
        return  newArrivals;
    }

    //save product and images
    public void save(SingleProductDTO singleProductDTO, MultipartFile[] files){
        Product product = Product.builder()
                .name(singleProductDTO.getName())
                .description(singleProductDTO.getDescription())
                .quantity(singleProductDTO.getQuantity())
                .price(singleProductDTO.getPrice())
                .isFeaturedProduct(singleProductDTO.isFeaturedProduct())
                .isFreeShipping(singleProductDTO.isFreeShipping())
                .isNewArrival(singleProductDTO.isNewArrival())
                .shippingCost(singleProductDTO.getShippingCost())
                .quantity(singleProductDTO.getQuantity())
                .reducedPrice(singleProductDTO.getReducedPrice())
                .build();
        Product savedProduct= productRepository.save(product);
        photoService.save(files, savedProduct.getId());

    }

    public List<ProductsDTO> all(){
        List<ProductsDTO> productList = new ArrayList<>();
        for(Product product: productRepository.findAll()){
           ProductsDTO products = ProductsDTO.builder()
                   .id(product.getId())
                   .name(product.getName())
                   .price(product.getPrice())
                   .reducedPrice(product.getReducedPrice())
                   .productImages(photoService.getProductImages(product.getId()))
                   .build();
            productList.add(products);
        }
        return  productList;
    }

    public SingleProductDTO getById(Long id){

        Product product = productRepository.findById(id)
                .orElseThrow(()->new InvalidOperationException("Product  does not exist"));
        return SingleProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .shippingCost(product.getShippingCost())
                .quantity(product.getQuantity())
                .productImages(photoService.getProductImages(product.getId()))
                .productAttributeDTO(productAttributeService.getAttributeAndValues(id)
                        .stream().map(SingleProductAttributeDto::new).toList())
                .productCategoryList(productCategoryService.getProductCategoryList(product.getId()).stream().map(ProductCategory::getName).toList())
                .build();
    }




}
