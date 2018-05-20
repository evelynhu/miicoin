package io.mii.coin.features.main;

import javax.inject.Inject;

import io.mii.coin.data.DataManager;
import io.mii.coin.data.local.PreferencesHelper;
import io.mii.coin.features.base.BasePresenter;
import io.mii.coin.injection.ConfigPersistent;
import io.mii.coin.util.rx.scheduler.SchedulerUtils;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainCryptoView> {

    private final DataManager dataManager;

    @Inject
    public MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(MainCryptoView mvpView) {
        super.attachView(mvpView);
    }

    public void getCrypto(int start, int limit, PreferencesHelper preferencesHelper) {
        checkViewAttached();
        getView().showProgress(true);
        dataManager
                .getCryptoList(start, limit, preferencesHelper)
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
