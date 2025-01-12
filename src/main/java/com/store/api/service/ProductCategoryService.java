package com.store.api.service;

import com.store.api.model.inventory.ItemCategory;
import com.store.api.model.product.Product;
import com.store.api.model.product.ProductCategory;
import com.store.api.repository.ItemCategoryRepository;
import com.store.api.repository.ProductCategoryRepository;
import com.store.api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private ItemCategoryRepository itemCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository,
                                  ProductRepository productRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
    }



    public ProductCategory setProductCategory(String name, Long productId){
        Product product = productRepository.findById(productId).orElse(null);
        ItemCategory itemCategory = itemCategoryRepository.findByName(name).orElse(null);

        if(product!=null && itemCategory != null){
            ProductCategory productCategory = new ProductCategory(itemCategory.getName(),1,product);
            return productCategoryRepository.save(productCategory);
        }
        return  null;
    }

    public List<ProductCategory> getProductCategoryList(Long productId){
      return  productCategoryRepository.findByProductIdOrderByCategoryHierarchy(productId);
    }
}
