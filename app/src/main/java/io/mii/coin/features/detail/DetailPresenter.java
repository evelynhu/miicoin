package io.mii.coin.features.detail;

import javax.inject.Inject;

import io.mii.coin.data.DataManager;
import io.mii.coin.data.model.present.ExchangeRate;
import io.mii.coin.features.base.BasePresenter;
import io.mii.coin.injection.ConfigPersistent;
import io.mii.coin.util.rx.scheduler.SchedulerUtils;

@ConfigPersistent
public class DetailPresenter extends BasePresenter<DetailCryptoView> {

    private final DataManager dataManager;

    @Inject
    public DetailPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(DetailCryptoView mvpView) {
        super.attachView(mvpView);
    }

    public void getCrypto(String id, String currency) {
        checkViewAttached();
        getView().showProgress(true);
        dataManager
                .getExchangeRates(id, currency)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        crypto -> {
                            // It should be always checked if MvpView (Fragment or Activity) is attached.
                            // Calling showProgress() on a not-attached fragment will throw a NPE
                            // It is possible to ask isAdded() in the fragment, but it's better to ask in the presenter
                            getView().showProgress(false);
                            getView().showCryptoDetail(crypto);
                            for (ExchangeRate exchangeRate : crypto.rates) {
                                getView().showExchangeRates(exchangeRate);
                            }
                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError(throwable);
                        });
    }
}
