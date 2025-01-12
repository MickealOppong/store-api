package com.store.api.model.util;

import com.store.api.model.user.AppUser;
import com.store.api.util.LogEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Photos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppImage extends LogEntity {

    @Id @GeneratedValue
    private Long id;
    private String contentType;
    private String name;
    private String path;
    private Long paymentId;
    private Long productId;

    @OneToOne
    @JoinColumn(name = "uid",referencedColumnName = "id")
    private AppUser appUser;

}
