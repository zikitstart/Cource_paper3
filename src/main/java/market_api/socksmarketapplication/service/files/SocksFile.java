package market_api.socksmarketapplication.service.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import market_api.socksmarketapplication.model.Socks;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocksFile {
    private HashMap<Socks, Integer> socksFileMap;
}