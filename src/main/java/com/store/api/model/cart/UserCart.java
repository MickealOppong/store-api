package com.store.api.model.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserCart {
    private Long id;
    private boolean includeAllItems;
    private List<CartDto> cartList;
}
