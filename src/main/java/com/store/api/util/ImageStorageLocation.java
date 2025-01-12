package com.store.api.util;

import org.springframework.stereotype.Component;

@Component
public class ImageStorageLocation {


    private String location="app-Images";

    public String getLocation(){
        return this.location;
    }
}
