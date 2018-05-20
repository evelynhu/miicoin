package io.mii.coin.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Quote {
    public double price;

    @SerializedName("volume_24h")
    public double volume24h;

    @SerializedName("market_cap")
    public double marketCap;

    @SerializedName("percent_change_1h")
    public double percentChange1h;

    @SerializedName("percent_change_24h")
    public double percentChange24h;

    @SerializedName("percent_change_7d")
    public double percentChange7d;
}
