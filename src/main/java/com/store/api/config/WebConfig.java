package com.store.api.config;

import com.store.api.impl.AppImageUtilImpl;
import com.store.api.model.inventory.ItemAttribute;
import com.store.api.model.product.Product;
import com.store.api.model.product.ProductCategory;
import com.store.api.repository.ProductCategoryRepository;
import com.store.api.repository.ProductRepository;
import com.store.api.service.ItemAttributeService;
import com.store.api.service.ProductAttributeService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
@AllArgsConstructor
public class WebConfig {

    private AppImageUtilImpl appImageUtil;
    private ProductRepository productRepository;
    private ProductCategoryRepository  productCategoryRepository;
    private ProductAttributeService productAttributeService;
    private ItemAttributeService itemAttributeService;




    @Bean
    public CommandLineRunner init(){
        return args -> {

         ItemAttribute colour=itemAttributeService.saveAttribute("Colour","Colour");
            ItemAttribute sz=itemAttributeService.saveAttribute("Size","Size");
            /*

         itemAttributeService.saveAttributeValue(colour.getName(),"Pink","#AE54ED");
            itemAttributeService.saveAttributeValue(colour.getName(),"Lightblue","#e2fdff");
            itemAttributeService.saveAttributeValue(colour.getName(),"Blue","#788bff");

            itemAttributeService.saveAttributeValue(sz.getName(),"Small","S");
            itemAttributeService.saveAttributeValue(sz.getName(),"Large","L");
            itemAttributeService.saveAttributeValue(sz.getName(),"Extra large","XL");

             */


            appImageUtil.init();
            Product productA = Product.builder()
                    .name("McBook 13 Inch pro")
                    .description("McBook 13 Inch pro")
                    .price(BigDecimal.valueOf(300))
                    .reducedPrice(BigDecimal.valueOf(300))
                    .quantity(1L)
                    .isFreeShipping(true)
                    .isNewArrival(true)
                    .isFeaturedProduct(true)
                    .returnDays(14)
                    .build();
            Product productB = Product.builder()
                    .name("Iphone 11 pro")
                    .description("Iphone 11 x pro")
                    .price(BigDecimal.valueOf(3000))
                    .reducedPrice(BigDecimal.valueOf(3000))
                    .quantity(1L)
                    .isFreeShipping(false)
                    .isNewArrival(true)
                    .isFeaturedProduct(true)
                    .returnDays(14)
                    .build();
            Product productC = Product.builder()
                    .name("JBL Go 3")
                    .description("Small JBL outdoor speaker")
                    .price(BigDecimal.valueOf(299.99))
                    .reducedPrice(BigDecimal.valueOf(299.99))
                    .quantity(1L)
                    .isFreeShipping(true)
                    .isNewArrival(true)
                    .isFeaturedProduct(true)
                    .returnDays(14)
                    .build();
            Product productD = Product.builder()
                    .name("Iphone 11 pro")
                    .description("Iphone 11 x pro")
                    .price(BigDecimal.valueOf(4500))
                    .reducedPrice(BigDecimal.valueOf(4500))
                    .quantity(1L)
                    .isFreeShipping(false)
                    .isNewArrival(true)
                    .isFeaturedProduct(true)
                    .returnDays(14)
                    .build();
            Product productE = Product.builder()
                    .name("TCL Smart TV")
                    .description("TCL Smart TV")
                    .price(BigDecimal.valueOf(300))
                    .reducedPrice(BigDecimal.valueOf(300))
                    .quantity(1L)
                    .isFreeShipping(true)
                    .isNewArrival(true)
                    .isFeaturedProduct(true)
                    .returnDays(14)
                    .build();
            Product productF = Product.builder()
                    .name("Nike air-force 1")
                    .description("Nike air-force 1")
                    .price(BigDecimal.valueOf(2999.99))
                    .reducedPrice(BigDecimal.valueOf(2999.99))
                    .quantity(1L)
                    .isFreeShipping(false)
                    .isNewArrival(true)
                    .isFeaturedProduct(true)
                    .returnDays(14)
                    .build();

            Product productG = Product.builder()
                    .name("Iphone 11 pro")
                    .description("Iphone 11 x pro")
                    .price(BigDecimal.valueOf(2390.90))
                    .reducedPrice(BigDecimal.valueOf(2390))
                    .quantity(1L)
                    .isFreeShipping(false)
                    .isNewArrival(true)
                    .isFeaturedProduct(true)
                    .returnDays(14)
                    .build();
            productRepository.save(productA);
            productRepository.save(productB);
            productRepository.save(productC);
            productRepository.save(productD);
            productRepository.save(productE);
            productRepository.save(productF);
            productRepository.save(productG);

            productCategoryRepository.saveAll(List.of(new ProductCategory("Clothing",1,productA),
                    new ProductCategory("Brand",2,productA),
                    new ProductCategory("Elektronika",1,productB)));

        productAttributeService.saveAttributeAndValues(colour.getName(),"#AE54ED",productB);
            productAttributeService.saveAttributeAndValues(colour.getName(),"#AE54ED",productA);
            productAttributeService.saveAttributeAndValues(colour.getName(),"#e2fdff",productD);

            productAttributeService.saveAttributeAndValues(colour.getName(),"#AE3DE",productG);

            productAttributeService.saveAttributeAndValues(sz.getName(),"XL",productB);

            productAttributeService.saveAttributeAndValues(sz.getName(),"S",productC);

            productAttributeService.saveAttributeAndValues(sz.getName(),"S",productG);

            productAttributeService.saveAttributeAndValues(sz.getName(),"S",productC);
            productAttributeService.saveAttributeAndValues(sz.getName(),"M",productE);
            productAttributeService.saveAttributeAndValues(sz.getName(),"13 inch",productA);

        };


    }
}
