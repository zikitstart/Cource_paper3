package market_api.socksmarketapplication.service.impl;
import market_api.socksmarketapplication.model.Socks;
import market_api.socksmarketapplication.service.Color;
import market_api.socksmarketapplication.service.Size;
import market_api.socksmarketapplication.service.SocksService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class SocksServiceImpl implements SocksService {

    private final HashMap<Socks, Integer> socksMap = new HashMap<>();

    @Override
    public void addSocks(Socks socks) {
        checkCottons(socks.getCottonPart());
        checkQuantity(socks.getQuantityInStock());
        int quantity = socks.getQuantityInStock();
        int mapQuantity = socksMap.getOrDefault(socks,0);
        socksMap.put(socks,quantity+mapQuantity);
    }

    @Override
    public Integer getSocksCotton(Color color, Size size, int cottonMin, int cottonMax) {
        checkCottons(cottonMin);
        checkCottons(cottonMax);
        int quantity = 0;
        for (Map.Entry<Socks, Integer> socksIntegerEntry : socksMap.entrySet()) {
            Socks sock = socksIntegerEntry.getKey();
            if (sock.getColor().equals(color)
                    &&sock.getSize().equals(size)
                    &&sock.getCottonPart() >= cottonMin
                    &&sock.getCottonPart()<=cottonMax){
                quantity += socksIntegerEntry.getValue();
            }
        }
        return quantity;
    }

    @Override
    public void removeSocks(Socks socks) {
        checkCottons(socks.getCottonPart());
        checkQuantity(socks.getQuantityInStock());
        int quantity = socks.getQuantityInStock();
        int mapQuantity = socksMap.getOrDefault(socks,0);
        if (!socksMap.containsKey(socks)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "На складе нет данного товара для списания!");
        }
        if (mapQuantity-quantity  < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "На складе недостаточное количество данного товара для списания!");
        }
        if (mapQuantity-quantity == 0) {
            socksMap.remove(socks);
            throw new ResponseStatusException(HttpStatus.OK, "Данный товар был полностью списан, на складе его больше нет.");
        }
        socksMap.put(socks,mapQuantity-quantity);
    }

    @Override
    public void putSocks(Socks socks) {
        checkCottons(socks.getCottonPart());
        checkQuantity(socks.getQuantityInStock());
        int quantity = socks.getQuantityInStock();
        int mapQuantity = socksMap.getOrDefault(socks,0);
        if (!socksMap.containsKey(socks)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "На складе нет данного товара для отгрузки!");
        }
        if (mapQuantity-quantity  < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "На складе недостаточное количество данного товара для отгрузки!");
        }
        if (mapQuantity-quantity == 0) {
            socksMap.remove(socks);
            throw new ResponseStatusException(HttpStatus.OK, "Данный товар был полностью отгружен, на складе его больше нет.");
        }
        socksMap.put(socks,mapQuantity-quantity);
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
}