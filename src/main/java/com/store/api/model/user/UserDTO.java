package com.store.api.model.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class UserDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String telephone;

    private TokenDto tokenDto;
}
