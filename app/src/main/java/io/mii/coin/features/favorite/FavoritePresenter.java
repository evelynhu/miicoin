package io.mii.coin.features.favorite;

import javax.inject.Inject;

import io.mii.coin.data.DataManager;
import io.mii.coin.data.local.PreferencesHelper;
import io.mii.coin.features.base.BasePresenter;
import io.mii.coin.features.favorite.FavoriteCryptoView;
import io.mii.coin.injection.ConfigPersistent;
import io.mii.coin.util.rx.scheduler.SchedulerUtils;

@ConfigPersistent
public class FavoritePresenter extends BasePresenter<FavoriteCryptoView> {

    private final DataManager dataManager;

    @Inject
    public FavoritePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(FavoriteCryptoView mvpView) {
        super.attachView(mvpView);
    }

    public void getCrypto(int limit, PreferencesHelper preferencesHelper) {
        checkViewAttached();
        getView().showProgress(true);
        dataManager
                .getFavoriteList(limit, preferencesHelper)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        cryptoSummary -> {
                            getView().showProgress(false);
                            getView().showCryptoSummary(cryptoSummary);
                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError(throwable);
                        });
    }
}
