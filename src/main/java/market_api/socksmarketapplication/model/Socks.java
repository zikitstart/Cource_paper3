package market_api.socksmarketapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import market_api.socksmarketapplication.service.Color;
import market_api.socksmarketapplication.service.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Socks {
    private Color color;
    private Size size;
    private int cottonPart;
    private int quantityInStock;

    @Override
    public String toString() {
        return "Носки: (" +
                "Цвет: " + color.getColorSocks() +
                " / Размер: " + size.getSizeSocks() +
                " / Содержание хлопка: " + cottonPart +
                "% / Количество на складе: " + quantityInStock +
                "шт.)";
    }
}