package market_api.socksmarketapplication.service;

import market_api.socksmarketapplication.model.Socks;

public interface SocksService {
    void addSocks(Socks socks);
    Integer getSocksCotton(Color color, Size size, int cottonMin,int cottonMax);

    void removeSocks(Socks socks);

    void putSocks(Socks socks);
}