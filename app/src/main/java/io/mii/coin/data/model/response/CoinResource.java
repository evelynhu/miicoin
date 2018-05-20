package io.mii.coin.data.model.response;

import com.google.gson.annotations.SerializedName;

public class CoinResource {
    public String name;
    public String id;
    public String symbol;
    @SerializedName("website_slug")
    public String websiteSlug;
}
