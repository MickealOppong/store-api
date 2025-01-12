package com.store.api.controller;

import com.store.api.exceptions.ImageStorageException;
import com.store.api.exceptions.InvalidOperationException;
import com.store.api.exceptions.ProductNotFoundException;
import com.store.api.exceptions.UserNotFoundException;
import com.store.api.service.PhotoService;
import com.store.api.util.ImageStorageLocation;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/imgs")
public class PhotoController {

    private final PhotoService photoService;
    private final ImageStorageLocation imageStorageLocation;

    public PhotoController(PhotoService photoService,ImageStorageLocation imageStorageLocation){
        this.photoService = photoService;
        this.imageStorageLocation = imageStorageLocation;
    }


    @PostMapping("/user-image")
    public ResponseEntity<String> userImage(MultipartFile file, Long id) {
     try{
         if (file == null && id == null) {
             return ResponseEntity.badRequest().body("Your did not attach a file or provide user id");
         }
         if (file == null) {
             return ResponseEntity.badRequest().body("Your did not attach a file");
         }
         if (id == null) {
             return ResponseEntity.badRequest().body("Your did not provide user id");
         }
         photoService.save(file, id);
         return ResponseEntity.ok().body("User photo updated");
     }catch (NullPointerException | UserNotFoundException |ImageStorageException e){
         return ResponseEntity.badRequest().body("Oops... Something went wrong. "+ e.getMessage());
     }
    }

    @PostMapping("/product-images")
    public ResponseEntity<String> productImages(MultipartFile[] file, Long id) {
        try{
            if (file == null && id == null) {
                return ResponseEntity.badRequest().body("Your did not attach a file or provide product id");
            }
            if (file == null) {
                return ResponseEntity.badRequest().body("Your did not attach a file");
            }
            if (id == null) {
                return ResponseEntity.badRequest().body("Your did not provide product id");
            }
            photoService.save(file,id);
            return ResponseEntity.ok().body("Product images updated");
        }catch (NullPointerException | ProductNotFoundException | ImageStorageException e){
            return ResponseEntity.badRequest().body("Oops... Something went wrong. "+ e.getMessage());
        }
    }

    @PostMapping("/product-image")
    public ResponseEntity<String> productImage(@NotNull MultipartFile file, Long id) {
        try{
            if (file == null && id == null) {
                return ResponseEntity.badRequest().body("Your did not attach a file or provide product id");
            }
            if (file == null) {
                return ResponseEntity.badRequest().body("Your did not attach a file");
            }
            if (id == null) {
                return ResponseEntity.badRequest().body("Your did not provide product id");
            }
            photoService.saveProductPhoto(file,id);
            return ResponseEntity.ok().body("Product images updated");
        }catch (NullPointerException | ProductNotFoundException | ImageStorageException e){
            return ResponseEntity.badRequest().body("Oops... Something went wrong. "+ e.getMessage());
        }
    }

    @GetMapping("/{filename:..+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws DataFormatException {
        Resource image = photoService.getResource(imageStorageLocation.getLocation()+"/"+filename);
        if(image == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=\""+image.getFilename()+"\"").body(image);
    }

    @GetMapping("/image")
    public ResponseEntity<String> image(Long id){
       try{
           return ResponseEntity.ok().body(photoService.getUserImage(id));
       }catch (InvalidOperationException | InvalidDataAccessApiUsageException e){
           return ResponseEntity.badRequest().body("Oops... Something went wrong. "+e.getMessage());
       }
    }

    @GetMapping("/product-image")
    public ResponseEntity<List<String>> productImages(Long id){
        try{
            return ResponseEntity.ok().body(photoService.getProductImages(id));
        }catch (InvalidOperationException | InvalidDataAccessApiUsageException e){
            return ResponseEntity.badRequest().body(List.of("Oops... Something went wrong. "+e.getMessage()));
        }
    }

    @DeleteMapping("/image")
    public void deletePhoto(String filename){
           photoService.deleteImage(filename);
    }
}
