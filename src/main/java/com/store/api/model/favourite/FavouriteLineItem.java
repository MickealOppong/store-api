package com.store.api.model.favourite;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FavouriteLineItem {


    @Id
    @GeneratedValue
    private Long id;
    private Long productId;
    private String productName;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name ="recId",referencedColumnName = "id")
    private FavouriteTable  favouriteTable;
}
