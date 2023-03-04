package market_api.socksmarketapplication.config;

import org.springframework.core.convert.converter.Converter;
import market_api.socksmarketapplication.service.Size;

public class StringToSizeEnumConverter implements Converter<String, Size> {

    @Override
    public Size convert(String source) {
        return Size.forValues(source);
    }
}