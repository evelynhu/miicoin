package io.mii.coin.features.detail;

import io.mii.coin.data.model.present.CryptoDetail;
import io.mii.coin.data.model.present.ExchangeRate;
import io.mii.coin.features.base.MvpView;

public interface DetailCryptoView extends MvpView {

    void showCryptoDetail(CryptoDetail cryptoDetail);

    void showExchangeRates(ExchangeRate exchangeRate);

    void showProgress(boolean show);

    void showError(Throwable error);
}
