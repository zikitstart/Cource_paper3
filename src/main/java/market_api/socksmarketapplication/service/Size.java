package market_api.socksmarketapplication.service;

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
}