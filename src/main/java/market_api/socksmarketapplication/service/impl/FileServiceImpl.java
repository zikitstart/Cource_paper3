package market_api.socksmarketapplication.service.impl;

import market_api.socksmarketapplication.service.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {
    @Value("${path.to.file}")
    private String pathFile;
    @Value("${name.of.file.socks}")
    private String nameSocksFile;

    @Override
    public void saveFileSocks(String json) {
        try {
            Files.writeString(Path.of(pathFile, nameSocksFile), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String readFileSocks() {
        try {
            return Files.readString(Path.of(pathFile, nameSocksFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File getSocksFile() {
        return new File(pathFile + "/" + nameSocksFile);
    }

    @Override
    public Path createTempFile(String suffix) {
        try {
            return Files.createTempFile(Path.of(pathFile), "tempFile", suffix);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadSocksJson() throws FileNotFoundException {
        File file = getSocksFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SocksFile.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    public ResponseEntity<Void> uploadSocks(MultipartFile file) {
        File socksFile = getSocksFile();
        try (FileOutputStream fos = new FileOutputStream(socksFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}