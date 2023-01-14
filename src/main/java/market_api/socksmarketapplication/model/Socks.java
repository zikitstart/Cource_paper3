package market_api.socksmarketapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import market_api.socksmarketapplication.service.Color;
import market_api.socksmarketapplication.service.Size;

import java.util.Objects;

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
                "%.)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Socks socks = (Socks) o;
        return cottonPart == socks.cottonPart && color == socks.color && size == socks.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, size, cottonPart);
    }
}