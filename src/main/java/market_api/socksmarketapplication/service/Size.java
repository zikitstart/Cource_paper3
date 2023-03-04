package market_api.socksmarketapplication.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum Size {
    Size38(38),
    Size39(39),
    Size40(40),
    Size41(41),
    Size42(42);
    private final int sizeSocks;

    Size(int sizeSocks) {
        this.sizeSocks = sizeSocks;
    }

    public int getSizeSocks() {
        return sizeSocks;
    }
    public static Size forValues(String size) {
        for (Size size1 : Size.values()) {
            if (String.valueOf(size1.getSizeSocks()).equals(size)) {
                return size1;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не правильно указан размер! Доступные размеры:38,39,40,41,42");
    }
}