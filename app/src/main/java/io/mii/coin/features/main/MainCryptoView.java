package io.mii.coin.features.main;

import java.util.List;

import io.mii.coin.data.model.present.CryptoSummary;
import io.mii.coin.features.base.MvpView;

public interface MainCryptoView extends MvpView {

    void showCryptoSummary(List<CryptoSummary> crypto);

    void showProgress(boolean show);

    void showError(Throwable error);
}
