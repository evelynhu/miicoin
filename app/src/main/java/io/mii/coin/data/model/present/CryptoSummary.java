package io.mii.coin.data.model.present;

public class CryptoSummary {
    public String name;
    public String id;
    public String symbol;
    public int rank;
    public double price;
    public double percentChange24h;
    public boolean isChecked;

    public CryptoSummary(String id, String name, String symbol, int rank, double price, double percentChange24h, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.rank = rank;
        this.price = price;
        this.percentChange24h = percentChange24h;
        this.isChecked = isChecked;
    }

    public CryptoSummary(CryptoSummary cryptoSummary) {
        this.id = cryptoSummary.id;
        this.name = cryptoSummary.name;
        this.symbol = cryptoSummary.symbol;
        this.rank = cryptoSummary.rank;
        this.price = cryptoSummary.price;
        this.percentChange24h = cryptoSummary.percentChange24h;
        this.isChecked = cryptoSummary.isChecked;
    }

    public void setCheckStatus(boolean checked) {
        this.isChecked = checked;
    }

    public boolean getCheckStatus() {
        return this.isChecked;
    }

}
