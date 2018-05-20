package io.mii.coin.features.favorite;

import java.util.List;

import io.mii.coin.data.model.present.CryptoSummary;
import io.mii.coin.features.base.MvpView;

public interface FavoriteCryptoView extends MvpView {

    void showCryptoSummary(List<CryptoSummary> crypto);

    void showProgress(boolean show);

    void showError(Throwable error);
}
