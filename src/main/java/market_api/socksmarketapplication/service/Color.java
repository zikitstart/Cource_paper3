package market_api.socksmarketapplication.service;

public enum Color {
    Black("Чёрный"),
    Red("Красный"),
    White("Белый"),
    Yellow("Жёлтый");
    private final String colorSocks;

    Color(String colorSocks) {
        this.colorSocks = colorSocks;
    }

    public String getColorSocks() {
        return colorSocks;
    }
}