package com.store.api.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface AppImageUtil {
    void init();

    void store(MultipartFile file, String username);

    Stream<Path> loadAll(String filename);

    Path toPath(String filename);

    Resource loadAsResource(String filename);

    void delete(String file);
}
