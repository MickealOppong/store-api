package com.store.api.model.inventory;

import com.store.api.util.LogEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemCategory extends LogEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String parent;

    public ItemCategory(String name, String parent) {
        this.name = name;
        this.parent = parent;
    }
}
