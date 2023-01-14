package market_api.socksmarketapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import market_api.socksmarketapplication.model.Socks;
import market_api.socksmarketapplication.service.Color;
import market_api.socksmarketapplication.service.Size;
import market_api.socksmarketapplication.service.SocksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/socks")
@Tag(name = "Носки.", description = "CRUD- методы для работы с носками.")
public class SocksController {
    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping
    @Operation(
            summary = "Добавление носков на склад.",
            description = "Метод для добавления носков на склад."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удалось добавить приход.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(
                                                    schema = @Schema(
                                                            implementation = Socks.class
                                                    )
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Параметры запроса отсутствуют или имеют некорректный формат."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны."
                    )
            }
    )
    public ResponseEntity<Socks> createSocks(@RequestParam("color") Color color, @RequestParam("size") Size size, @RequestParam int cotton, @RequestParam int quantity) {
        Socks socks = new Socks(color, size, cotton, quantity);
        this.socksService.addSocks(socks);
        return ResponseEntity.ok(socks);
    }

    @GetMapping
    @Operation(
            summary = "Получение носков по параметрам.",
            description = "Метод для получения носков по параметрам со склада."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос выполнен.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(
                                                    schema = @Schema(
                                                            implementation = Socks.class
                                                    )
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Параметры запроса отсутствуют или имеют некорректный формат."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны."
                    )
            }
    )
    public ResponseEntity<Integer> getSocksByCotton(@RequestParam("color") Color color, @RequestParam("size") Size size, @RequestParam(value = "cottonMin", required = false, defaultValue = "0") int cottonMin, @RequestParam(value = "cottonMax", required = false, defaultValue = "100") int cottonMax) {
        Integer socks = socksService.getSocksCotton(color, size, cottonMin, cottonMax);
        return ResponseEntity.ok(socks);
    }

    @PutMapping
    @Operation(
            summary = "Отгрузка носков со склада.",
            description = "Метод для отгрузки носков со склад."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удалось произвести отпуск носков со склада."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Товара нет на складе в нужном количестве или параметры запроса имеют некорректный формат."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны."
                    )
            }
    )
    public ResponseEntity<Socks> sendingSocks(@RequestParam("color") Color color, @RequestParam("size") Size size, @RequestParam int cotton, @RequestParam int quantity) {
        Socks socks = new Socks(color, size, cotton, quantity);
        this.socksService.putSocks(socks);
        return ResponseEntity.ok(socks);
    }

    @DeleteMapping
    @Operation(
            summary = "Списание бракованного товара со склада.",
            description = "Метод для списания бракованного товара со склада."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос выполнен, товар списан со склада."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Параметры запроса отсутствуют или имеют некорректный формат."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны."
                    )
            }
    )
    public ResponseEntity<Socks> deleteSocks(@RequestParam("color") Color color, @RequestParam("size") Size size, @RequestParam int cotton, @RequestParam int quantity) {
        Socks socks = new Socks(color, size, cotton, quantity);
        this.socksService.removeSocks(socks);
        return ResponseEntity.ok(socks);
    }
}