package com.store.api.model.inventory;

import com.store.api.util.LogEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemAttribute extends LogEntity {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;


    public ItemAttribute(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
