package com.store.api.model.cart;

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
@Builder
public class CartTable extends LogEntity {

    @Id @GeneratedValue
    private Long id;
    private Long userId;
    private String firstName;
    private String username;
    private String lastName;
    private String sessionId;
    private boolean includeAllItems;


}
