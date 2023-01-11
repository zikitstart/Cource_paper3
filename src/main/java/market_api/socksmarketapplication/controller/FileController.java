package market_api.socksmarketapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import market_api.socksmarketapplication.service.FileService;
import market_api.socksmarketapplication.service.SocksService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/file")
@Tag(name = "Файлы.", description = "Методы для работы с файлами.")
public class FileController {
    private final FileService fileService;
    private final SocksService socksService;

    public FileController(FileService fileService, SocksService socksService) {
        this.fileService = fileService;
        this.socksService = socksService;
    }

    @GetMapping("/downloadSocksTxt")
    @Operation(
            summary = "Скачать файл в txt.",
            description = "Метод для скачивания txt-файла."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Файл загружен."
                    )
            }
    )
    public ResponseEntity<Object> downloadSocksFileTxt() {
        return socksService.downloadSocksTxt();
    }

    @GetMapping("/downloadSocksJson")
    @Operation(
            summary = "Скачать файл в json.",
            description = "Метод для скачивания json-файла."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Файл загружен."
                    )
            }
    )
    public ResponseEntity<InputStreamResource> downloadSocksFileJson() throws FileNotFoundException {
        return fileService.downloadSocksJson();
    }

    @PostMapping(value = "/importSocks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Замена файла.",
            description = "Принимает json-файл и заменяет сохраненный на жестком (локальном) диске на новый."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Файл заменён."
                    )
            }
    )
    public ResponseEntity<Void> uploadSocksFile(@RequestParam MultipartFile file) {
        return fileService.uploadSocks(file);
    }
}