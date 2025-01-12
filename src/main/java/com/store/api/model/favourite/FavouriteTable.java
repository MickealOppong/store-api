package com.store.api.model.favourite;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favourites")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FavouriteTable {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String username;

}
