package com.store.api.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.store.api.model.inventory.ItemCategory;
import com.store.api.model.user.UserRole;
import com.store.api.model.util.PaymentMethod;
import com.store.api.repository.CourierRepository;
import com.store.api.repository.ItemCategoryRepository;
import com.store.api.repository.PaymentMethodRepository;
import com.store.api.repository.UserRepository;
import com.store.api.service.UserDetailsServiceImpl;
import com.store.api.service.UserRoleService;
import com.store.api.model.util.Courier;
import com.store.api.util.RsaKeyProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {


    private final RsaKeyProperties rsaKey;
    private UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private UserRoleService  userRoleService;



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKey.publicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKey.publicKey()).privateKey(rsaKey.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:5174"));
        config.setAllowedMethods(Arrays.asList("POST","GET","DELETE","PUT","PATCH","OPTIONS","HEAD"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
    }
    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsServiceImpl);
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request->request.requestMatchers("/auth","/auth/**",
                                "/users/user","/sms","/sms/**","/store","/store/**","/imgs","/imgs/**"
                        ,"/cart","/cart/**","/session","/session/**",
                                "/fav","/fav/**","/courier","/courier/**","/payment-method","/payment-method/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(rs->rs.jwt(Customizer.withDefaults()))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("SCOPE");
    }

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Bean
    public CommandLineRunner load(){
        return args -> {
            UserRole USER = new UserRole("USER");
            UserRole ADMIN = new UserRole("ADMIN");
            userRoleService.save(USER);
            userRoleService.save(ADMIN);

            ItemCategory s1 = new ItemCategory("TV","Electronics");
            ItemCategory s2 = new ItemCategory("Computers","Electronics");
            ItemCategory s3 = new ItemCategory("Smart tv","TV");
            ItemCategory s4 = new ItemCategory("Keyboard","Computers");
            ItemCategory s5 = new ItemCategory("Lg","Smart tv");
            ItemCategory s6 = new ItemCategory("apple","smart tv");


            itemCategoryRepository.save(s1);
            itemCategoryRepository.save(s2);
            itemCategoryRepository.save(s3);
            itemCategoryRepository.save(s4);
            itemCategoryRepository.save(s5);
            itemCategoryRepository.save(s6);

            Courier  dhl = new Courier("DHL", BigDecimal.valueOf(9.99),2L);
            Courier  poczta = new Courier("Poczta", BigDecimal.valueOf(10.99),2L);
            Courier  inpost = new Courier("InPost", BigDecimal.valueOf(10.99),2L);
            Courier  free = new Courier("Free shipping", BigDecimal.valueOf(0.00),2L);
            Courier  express = new Courier("Same day shipping", BigDecimal.valueOf(14.99),1L);
            courierRepository.save(dhl);
            courierRepository.save(inpost);
            courierRepository.save(poczta);
            courierRepository.save(free);
            courierRepository.save(express);


            PaymentMethod  cash =  PaymentMethod.builder()
                    .paymentMethod("Cash")
                    .build();
            paymentMethodRepository.save(cash);

            PaymentMethod  card =  PaymentMethod.builder()
                    .paymentMethod("Card")
                    .build();
            paymentMethodRepository.save(card);


            PaymentMethod  transfer =  PaymentMethod.builder()
                    .paymentMethod("Transfer")
                    .build();
            paymentMethodRepository.save(transfer);

            PaymentMethod  blik =  PaymentMethod.builder()
                    .paymentMethod("Blik")
                    .build();
            paymentMethodRepository.save(blik);
        };
    }


}
