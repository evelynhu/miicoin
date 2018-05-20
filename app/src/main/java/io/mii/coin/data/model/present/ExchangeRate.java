package io.mii.coin.data.model.present;

public class ExchangeRate {
    public String currency;
    public double price;

    public ExchangeRate(String currency, double price) {
        this.currency = currency;
        this.price = price;

    }
}
