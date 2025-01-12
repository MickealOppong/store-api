package com.store.api.model.user;

import com.store.api.util.LogEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "RefreshToken")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken extends LogEntity {

    @Id @GeneratedValue
    private Long id;
    private String token;
    private Instant expiredAt;
    private Instant issuedAt;

    @ManyToOne
    @JoinColumn(name = "fk_id",referencedColumnName = "id")
    private AppUser appUser;


}
