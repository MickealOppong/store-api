package com.store.api.controller;

import com.store.api.model.product.ProductCategory;
import com.store.api.model.product.SingleProductDTO;
import com.store.api.service.ItemAttributeService;
import com.store.api.service.ProductCategoryService;
import com.store.api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ItemAttributeService itemAttributeService;
    private final ProductCategoryService productCategoryService;

    public ProductController(ProductService productService,
                             ItemAttributeService itemAttributeService,ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.itemAttributeService = itemAttributeService;
        this.productCategoryService = productCategoryService;
    }

    @PostMapping("/product")
    public ResponseEntity<String> product(@RequestBody @Valid SingleProductDTO singleProductDTO, MultipartFile[] images){
        if(images.length==0 ){
            return ResponseEntity.badRequest().body("Please attach at least one image");
        }
        productService.save(singleProductDTO,images);
        return ResponseEntity.ok().body("Product created");
    }





    @PostMapping("/product-attribute")
    public ResponseEntity<String> addProductAttribute(String name,Long id){
        return null;
    }

    @PostMapping("/product-attribute-value")
    public ResponseEntity<String> addAttributeValue(String name,String value,Long id){
      return null;
    }

    @PostMapping("/product-category")
    public ResponseEntity<String> addProductCategory(String name,Long id){
       try{
           ProductCategory productCategory =productCategoryService.setProductCategory(name,id);
           if(productCategory !=null){
               return ResponseEntity.ok("Product category updated");
           }
           return ResponseEntity.ok("Product category does not exist");
       }catch (InvalidDataAccessApiUsageException e){
           return ResponseEntity.badRequest().body("Oops... Something went wrong. "+e.getMessage());
       }

    }

}
