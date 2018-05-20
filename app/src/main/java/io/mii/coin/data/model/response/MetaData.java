package io.mii.coin.data.model.response;

import com.google.gson.annotations.SerializedName;

public class MetaData {
    public String timestamp;

    @SerializedName("num_cryptocurrencies")
    public String numCryptocurrencies;

    public String error;
}
