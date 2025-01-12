package com.store.api.model.util;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmationCode {

    @Id @GeneratedValue
    private Long id;
    private String code;
    @NotBlank
    @Size(max =9)
    private String telephone;
    private Instant expireDate;

}
