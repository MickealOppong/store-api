package com.store;

import com.store.api.util.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class StoreApiApplication {


	public static void main(String[] args) {
		SpringApplication.run(StoreApiApplication.class, args);
	}




}
