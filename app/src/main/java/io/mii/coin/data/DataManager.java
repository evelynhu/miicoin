package io.mii.coin.data;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.mii.coin.data.local.PreferencesHelper;
import io.mii.coin.data.model.present.CryptoDetail;
import io.mii.coin.data.model.present.CryptoSummary;
import io.mii.coin.data.model.present.ExchangeRate;
import io.mii.coin.data.model.response.Quote;
import io.mii.coin.data.model.response.Ticker;
import io.mii.coin.data.remote.CoinService;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by evelyn on 10/5/18.
 */
@Singleton
public class DataManager {

    private CoinService coinService;

    @Inject
    public DataManager(CoinService coinService) {
        this.coinService = coinService;
    }

    public Single<List<CryptoSummary>> getCryptoList(int start, int limit, PreferencesHelper preferencesHelper) {
        List<CryptoSummary> favorites = preferencesHelper.getFavorites();

        return coinService
            .getTickerList(start, limit, null)
            .map(res -> new ArrayList<>(res.data.values()))
            .toObservable()
            .flatMapIterable(namedResource -> namedResource)
            .map(namedResource -> {
                Quote quote = namedResource.quotes.get("USD");

                return new CryptoSummary(
                        namedResource.id,
                        namedResource.name,
                        namedResource.symbol,
                        namedResource.rank,
                        quote.price,
                        quote.percentChange24h,
                        preferencesHelper.isFavorite(favorites, namedResource.id)
                        );
            })
            .toList();
    }

    public Single<List<CryptoSummary>> getFavoriteList(int limit, PreferencesHelper preferencesHelper) {
        List<CryptoSummary> favorites = preferencesHelper.getFavorites();
        return Single.fromObservable(Observable.just(favorites));
    }

    public Single<List<String>> getCoinList(int limit) {
        return coinService
                .getCoinList()
                .toObservable()
                .flatMapIterable(namedResource -> namedResource.data)
                .map(namedResource -> namedResource.name)
                .toList();
    }

    @SuppressLint("CheckResult")
    public Single<CryptoDetail> getExchangeRates(String id, String currencyCode) {
        return coinService
                .getExchangeRates(id, currencyCode)
                .map(res -> {
                    Ticker ticker = res.data;
                    List<ExchangeRate> rates = new ArrayList<>();

                    for (String currency : ticker.quotes.keySet()) {
                        if (!currency.equals(currencyCode)) {
                            continue;
                        }
                        Quote quote = ticker.quotes.get(currency);
                        rates.add(new ExchangeRate(currency, quote.price));
                    }

                    return new CryptoDetail(
                            ticker.id,
                            ticker.name,
                            ticker.symbol,
                            rates
                    );
                });
    }


    @SuppressLint("CheckResult")
    public Single<List<CryptoSummary>> getAllCoins(int limit, PreferencesHelper preferencesHelper) {
        List<CryptoSummary> favorites = preferencesHelper.getFavorites();

        return Observable.range(0, 1)
                .map(count -> count * limit + 1)
                .toList()
                .flatMap(starts -> {
                    List<Single<List<CryptoSummary>>> observables = new ArrayList<>();
                    for (Integer start : starts) {
                        observables.add(coinService.getTickerList(start, limit, null)
                                .map(res -> new ArrayList<>(res.data.values()))
                                .toObservable()
                                .flatMapIterable(namedResource -> namedResource)
                                .map(namedResource -> {
                                    Quote quote = namedResource.quotes.get("USD");
                                    return new CryptoSummary(
                                            namedResource.id,
                                            namedResource.name,
                                            namedResource.symbol,
                                            namedResource.rank,
                                            quote.price,
                                            quote.percentChange24h,
                                            preferencesHelper.isFavorite(favorites, namedResource.id)
                                    );
                                })
                                .toList());
                    }
                    return Single.zip(observables, objects -> {
                        List<CryptoSummary> cryptoSummaries = new ArrayList<>();
                        for (Object data : objects) {
                            if (data instanceof List) {
                                cryptoSummaries.addAll((List<CryptoSummary>)data);
                            }
                        }
                        Collections.sort(cryptoSummaries, (o1, o2) -> o1.rank - o2.rank);
                        return cryptoSummaries;
                    });
                });
    }

}
