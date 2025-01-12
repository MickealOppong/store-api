package com.store.api.model.inventory;

import com.store.api.util.LogEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemAttributeValue extends LogEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String value;

    private Long itemAttributeId;

    public ItemAttributeValue(String name, String value, ItemAttribute itemAttribute) {
        this.name = name;
        this.value = value;
        this.itemAttributeId = itemAttribute.getId();
    }
}
