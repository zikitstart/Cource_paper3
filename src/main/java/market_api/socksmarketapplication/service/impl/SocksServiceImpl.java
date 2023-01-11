package market_api.socksmarketapplication.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import market_api.socksmarketapplication.model.Socks;
import market_api.socksmarketapplication.service.Color;
import market_api.socksmarketapplication.service.FileService;
import market_api.socksmarketapplication.service.Size;
import market_api.socksmarketapplication.service.SocksService;
import market_api.socksmarketapplication.service.files.SocksFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class SocksServiceImpl implements SocksService {

    private final FileService fileService;
    private TreeMap<Long, Socks> socksMap = new TreeMap<>();
    private Long id = 1L;

    public SocksServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @PostConstruct
    private void init() {
        try {
            readToFileSocks();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addSocks(Socks socks) {
        checkCottons(socks.getCottonPart());
        checkQuantity(socks.getQuantityInStock());
        for (Map.Entry<Long, Socks> longSocksEntry : socksMap.entrySet()) {
            if (longSocksEntry.getValue().getColor().equals(socks.getColor())) {
                if (longSocksEntry.getValue().getSize().equals(socks.getSize())) {
                    if (longSocksEntry.getValue().getCottonPart() == socks.getCottonPart()) {
                        int quantity = longSocksEntry.getValue().getQuantityInStock() + socks.getQuantityInStock();
                        Socks newSocks = new Socks(socks.getColor(), socks.getSize(), socks.getCottonPart(), quantity);
                        socksMap.put(longSocksEntry.getKey(), newSocks);
                        saveToFileSocks();
                        return;
                    }
                }
            }
        }
        socksMap.put(id++, socks);
        saveToFileSocks();
    }

    @Override
    public List<Socks> getSocksCotton(Color color, Size size, int cottonMin, int cottonMax) {
        checkCottons(cottonMin);
        checkCottons(cottonMax);
        List<Socks> socksList = new ArrayList<>();
        for (Socks socks : socksMap.values()) {
            if (socks.getColor().equals(color)) {
                if (socks.getSize().equals(size)) {
                    if (socks.getCottonPart() >= cottonMin && socks.getCottonPart() <= cottonMax) {
                        socksList.add(socks);
                    }
                }
            }
        }
        return socksList;
    }

    @Override
    public void removeSocks(Socks socks) {
        checkCottons(socks.getCottonPart());
        checkQuantity(socks.getQuantityInStock());
        for (Map.Entry<Long, Socks> longSocksEntry : socksMap.entrySet()) {
            if (longSocksEntry.getValue().getColor().equals(socks.getColor())) {
                if (longSocksEntry.getValue().getSize().equals(socks.getSize())) {
                    if (longSocksEntry.getValue().getCottonPart() == socks.getCottonPart()) {
                        int quantity = longSocksEntry.getValue().getQuantityInStock() - socks.getQuantityInStock();
                        if (quantity < 0) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "На складе недостаточное количество данного товара для списания!");
                        }
                        if (quantity == 0) {
                            socksMap.remove(longSocksEntry.getKey());
                            saveToFileSocks();
                            throw new ResponseStatusException(HttpStatus.OK, "Данный товар был полностью списан, на складе его больше нет.");
                        }
                        Socks newSocks = new Socks(socks.getColor(), socks.getSize(), socks.getCottonPart(), quantity);
                        socksMap.put(longSocksEntry.getKey(), newSocks);
                        saveToFileSocks();
                        return;
                    }
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "На складе нет данного товара для списания!");
    }

    @Override
    public void putSocks(Socks socks) {
        checkCottons(socks.getCottonPart());
        checkQuantity(socks.getQuantityInStock());
        for (Map.Entry<Long, Socks> longSocksEntry : socksMap.entrySet()) {
            if (longSocksEntry.getValue().getColor().equals(socks.getColor())) {
                if (longSocksEntry.getValue().getSize().equals(socks.getSize())) {
                    if (longSocksEntry.getValue().getCottonPart() == socks.getCottonPart()) {
                        int quantity = longSocksEntry.getValue().getQuantityInStock() - socks.getQuantityInStock();
                        if (quantity < 0) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "На складе недостаточное количество данного товара для отгрузки!");
                        }
                        if (quantity == 0) {
                            socksMap.remove(longSocksEntry.getKey());
                            saveToFileSocks();
                            throw new ResponseStatusException(HttpStatus.OK, "Данный товар был полностью отгружен, на складе его больше нет.");
                        }
                        Socks newSocks = new Socks(socks.getColor(), socks.getSize(), socks.getCottonPart(), quantity);
                        socksMap.put(longSocksEntry.getKey(), newSocks);
                        saveToFileSocks();
                        return;
                    }
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "На складе нет данного товара для отгрузки!");
    }

    private void checkQuantity(int quantity) {
        if (quantity < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Количество носков не может быть отрицательным!");
        }
    }

    private void checkCottons(int cotton) {
        if (cotton < 0 || cotton > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Должен выражаться в % соотношение хлопка в составе от 0 до 100!");
        }
    }

    private void saveToFileSocks() {
        try {
            SocksFile socksFile = new SocksFile(id, socksMap);
            String json = new ObjectMapper().writeValueAsString(socksFile);
            fileService.saveFileSocks(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void readToFileSocks() {
        try {
            String json = fileService.readFileSocks();
            SocksFile socksFile = new ObjectMapper().readValue(json, new TypeReference<>() {
            });
            id = socksFile.getId();
            socksMap = socksFile.getSocksFileMap();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResponseEntity<Object> downloadSocksTxt() {
        try {
            Path path = createSocksReport();
            if (Files.size(path) == 0) {
                return ResponseEntity.noContent().build();
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .contentLength(Files.size(path))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SocksReport.txt\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

    @Override
    public Path createSocksReport() throws IOException {
        Path path = fileService.createTempFile("socksReport");
        for (Socks socks : socksMap.values()) {
            try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                writer.append(socks.toString());
                writer.append("\n");
            }
        }
        return path;
    }
}