package com.store.api.model.favourite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserFavourite {
    private Long id;
    private List<FavouriteDto> favouriteList;
}
