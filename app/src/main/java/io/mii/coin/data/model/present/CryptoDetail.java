package io.mii.coin.data.model.present;

import java.util.List;

public class CryptoDetail {
    public String id;
    public String name;
    public String symbol;
    public List<ExchangeRate> rates;

    public CryptoDetail(String id, String name, String symbol, List<ExchangeRate> rates) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.rates = rates;
    }

}
