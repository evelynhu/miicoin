package io.mii.coin.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Ticker {
    public String name;
    public String id;
    public String symbol;

    @SerializedName("website_slug")
    public String websiteSlug;

    public int rank;

    @SerializedName("circulating_supply")
    public double circulatingSupply;

    @SerializedName("total_supply")
    public double totalSupply;

    @SerializedName("max_supply")
    public double maxSupply;

    public Map<String, Quote> quotes;

    @SerializedName("last_updated")
    public int lastUpdated;
}
