package com.store.api.controller;

import com.store.api.model.favourite.UserFavourite;
import com.store.api.service.FavouriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/fav")
public class FavouriteController {

    private  final FavouriteService favouriteService;

    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @GetMapping
    public ResponseEntity<UserFavourite> all(Long  userId){
        return ResponseEntity.ok().body( favouriteService.getUserFavourites(userId));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Integer> addToFavourites(@RequestParam Long userId,@RequestParam Long productId){
     try{
         favouriteService.addToFavourite(userId,productId);
          return ResponseEntity.ok().body(favouriteService.getFavouriteCount(userId));
     }catch (Exception e){
         return ResponseEntity.ok(0);
     }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> userFavQuantity(Long userId){
        return ResponseEntity.ok().body(favouriteService.getFavouriteCount(userId));
    }

    @GetMapping("/fav-check")
    public ResponseEntity<Boolean> isUserFavourite(Long userId,Long productId){
        return ResponseEntity.ok().body(favouriteService.isUserFavourite(userId,productId));
    }

    @DeleteMapping("/fav-item")
    public ResponseEntity<Integer> removeFromFavourite(Long userId,Long productId){
        try{
            favouriteService.deleteItemFromFavourite(userId,productId);
            return ResponseEntity.ok().body(favouriteService.getFavouriteCount(userId));
        }catch (Exception e){
            return ResponseEntity.ok(0);
        }
    }
}
