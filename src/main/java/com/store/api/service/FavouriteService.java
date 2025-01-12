package com.store.api.service;

import com.store.api.exceptions.InvalidOperationException;
import com.store.api.model.favourite.FavouriteDto;
import com.store.api.model.favourite.FavouriteLineItem;
import com.store.api.model.favourite.FavouriteTable;
import com.store.api.model.favourite.UserFavourite;
import com.store.api.model.product.Product;
import com.store.api.model.user.AppUser;
import com.store.api.repository.FavouriteLineItemRepository;
import com.store.api.repository.FavouriteTableRepository;
import com.store.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FavouriteService {
    private final FavouriteTableRepository  favouriteTableRepository;
    private final FavouriteLineItemRepository favouriteLineItemRepository;
    private final ProductService  productService;
    private final UserRepository userRepository;
    private final PhotoService photoService;

    public FavouriteService(FavouriteTableRepository favouriteTableRepository,
                            FavouriteLineItemRepository favouriteLineItemRepository, ProductService productService,
                            UserRepository userRepository, PhotoService photoService) {
        this.favouriteTableRepository = favouriteTableRepository;
        this.favouriteLineItemRepository = favouriteLineItemRepository;
        this.productService = productService;
        this.userRepository = userRepository;
        this.photoService = photoService;
    }

    public void updateUser(AppUser appUser){
        Optional<FavouriteTable> userCart=  favouriteTableRepository.findByUsername(appUser.getUsername());
        if(userCart.isPresent()){
            FavouriteTable  user =  userCart.get();
            user.setUsername(appUser.getUsername());
            user.setUserId(appUser.getId());
            favouriteTableRepository.save(user);
        }

    }

    public void addToFavourite(Long userId,Long productId){

        //yes user exist in contextHolder
        Product product = productService.getProductById(productId);
        AppUser currentUser=userRepository.findById(userId).orElse(null);
        if(currentUser != null){
            FavouriteTable userFavourite = favouriteTableRepository.findByUserId(currentUser.getId()).orElse(null);
            if(userFavourite!=null){
               FavouriteLineItem item= favouriteLineItemRepository
                        .findByProductIdAndRecId(productId,userFavourite.getId()).orElse(null);
                if(item!=null){
                   favouriteLineItemRepository.deleteById(item.getId());
                }else{
                    FavouriteLineItem  favouriteLineItem =  FavouriteLineItem.builder()
                            .productName(product.getName())
                            .favouriteTable(userFavourite)
                            .productId(product.getId())
                            .build();
                    favouriteLineItemRepository.save(favouriteLineItem);
                }
            }  else{
                FavouriteTable favouriteTable   = FavouriteTable.builder()
                        .username(currentUser.getUsername())
                        .userId(currentUser.getId())
                        .build();
                favouriteTableRepository.save(favouriteTable);

                FavouriteLineItem favouriteLineItem = FavouriteLineItem.builder()
                        .favouriteTable(favouriteTable)
                        .productId(product.getId())
                        .productName(product.getName())
                        .build();
                   favouriteLineItemRepository.save(favouriteLineItem);
            }
        }

    }

    public void deleteItemFromFavourite(Long userId,Long productId){
        FavouriteTable userFav = favouriteTableRepository.findByUserId(userId).orElse(null);
        if(userFav!=null){
            FavouriteLineItem  item= favouriteLineItemRepository.findByProductIdAndRecId(productId, userFav.getId())
                    .orElseThrow(()->new InvalidOperationException("Record  does not exist"));
            favouriteLineItemRepository.delete(item);
            long itemCount = favouriteLineItemRepository.count();
            if(itemCount==0){
                favouriteTableRepository.deleteById(userFav.getId());
            }
        }
    }



    public UserFavourite getUserFavourites(Long userId){
        FavouriteTable userFavourites = favouriteTableRepository.findByUserId(userId).orElse(null);
        if(userFavourites  !=null){
            List<FavouriteDto> favouriteDto = favouriteLineItemRepository.findAllByRecId(userFavourites.getId())
                    .stream().map(FavouriteDto::new).toList();
            for(FavouriteDto item:  favouriteDto){
                String  img = photoService.getProductImages(item.getProductId()).size()>0?photoService.getProductImages(item.getProductId()).get(0):null;
                item.setProductImage(img);
                item.setPrice(productService.getProductById(item.getProductId()).getPrice());
            }
            return UserFavourite.builder()
                    .id(userFavourites.getId())
                    .favouriteList(favouriteDto)
                    .build();
        }
        return null;

    }




    public int getFavouriteCount(Long userId){
        FavouriteTable  userFavTable = favouriteTableRepository.findByUserId(userId).orElse(null);
        if(userFavTable!=null){
           return favouriteLineItemRepository.findAllByRecId(userFavTable.getId()).size();
        }
        return 0;
    }


    public boolean isUserFavourite(Long userId,Long productId){
       FavouriteTable userFavouriteTable = favouriteTableRepository.findByUserId(userId).orElse(null);
       if(userFavouriteTable!=null){
         FavouriteLineItem product=   favouriteLineItemRepository.
                 findByProductIdAndRecId(productId,userFavouriteTable.getId()).orElse(null);
           return product != null;
       }
        return false;
    }

}
