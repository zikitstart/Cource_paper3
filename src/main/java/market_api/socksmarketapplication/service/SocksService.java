package market_api.socksmarketapplication.service;

import market_api.socksmarketapplication.model.Socks;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface SocksService {
    void addSocks(Socks socks);
    List<Socks> getSocksCotton(Color color, Size size, int cottonMin,int cottonMax);

    void removeSocks(Socks socks);

    void putSocks(Socks socks);

    ResponseEntity<Object> downloadSocksTxt();

    Path createSocksReport() throws IOException;
}