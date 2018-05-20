package io.mii.coin.data.remote;

import android.support.annotation.Nullable;

import io.mii.coin.data.model.response.CoinListResponse;
import io.mii.coin.data.model.response.TickerResponse;
import io.mii.coin.data.model.response.TickerMapResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CoinService {

    @GET("ticker")
    Single<TickerMapResponse> getTickerList(@Nullable @Query("start") Integer start, @Nullable @Query("limit") Integer limit, @Nullable @Query("convert") Integer convert);

    @GET("listings")
    Single<CoinListResponse> getCoinList();

    @GET("ticker/{id}")
    Single<TickerResponse> getExchangeRates(@Path("id") String id, @Nullable @Query("convert") String convert);
}
