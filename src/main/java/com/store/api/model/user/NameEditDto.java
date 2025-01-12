package com.store.api.model.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameEditDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
