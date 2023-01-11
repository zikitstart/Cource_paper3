package market_api.socksmarketapplication.service.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import market_api.socksmarketapplication.model.Socks;

import java.util.TreeMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocksFile {
    private long id;
    private TreeMap<Long, Socks> socksFileMap;
}