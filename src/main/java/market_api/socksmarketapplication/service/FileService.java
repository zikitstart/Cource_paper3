package market_api.socksmarketapplication.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public interface FileService {
    void saveFileSocks(String json);

    String readFileSocks();

    File getSocksFile();

    Path createTempFile(String suffix);

    ResponseEntity<InputStreamResource> downloadSocksJson() throws FileNotFoundException;

    ResponseEntity<Void> uploadSocks(MultipartFile file);
}