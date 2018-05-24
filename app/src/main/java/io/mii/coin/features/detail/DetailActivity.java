package io.mii.coin.features.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;

import javax.inject.Inject;

import butterknife.BindView;
import io.mii.coin.Constants;
import io.mii.coin.R;
import io.mii.coin.data.model.present.CryptoDetail;
import io.mii.coin.data.model.present.ExchangeRate;
import io.mii.coin.features.base.BaseActivity;
import io.mii.coin.features.common.ErrorView;
import io.mii.coin.features.detail.widget.ExchangeRateView;
import io.mii.coin.injection.component.ActivityComponent;
import timber.log.Timber;

public class DetailActivity extends BaseActivity implements DetailCryptoView, ErrorView.ErrorListener, AdapterView.OnItemSelectedListener {

    public static final String EXTRA_CRYPTO_NAME = "EXTRA_CRYPTO_NAME";
    public static final String EXTRA_CRYPTO_ID = "EXTRA_CRYPTO_ID";
    public static final String EXTRA_CRYPTO_SYMBOL = "EXTRA_CRYPTO_SYMBOL";

    @Inject
    DetailPresenter detailPresenter;

    @BindView(R.id.view_error)
    ErrorView errorView;

    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.text_value)
    TextView value;

    @BindView(R.id.text_symbol)
    TextView symbol;

    @BindView(R.id.layout_crypto)
    View cryptoLayout;

    @BindView(R.id.spinner)
    Spinner spinner;

    private String cryptoSymbol;
    private String cryptoName;
    private String cryptoId;
    private String currency = "USD";
    private NumberFormat mCurrencyFormat;

    public static Intent getStartIntent(Context context, String cryptoName, String cryptoId, String cryptoSymbol) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_CRYPTO_NAME, cryptoName);
        intent.putExtra(EXTRA_CRYPTO_ID, cryptoId);
        intent.putExtra(EXTRA_CRYPTO_SYMBOL, cryptoSymbol);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cryptoName = getIntent().getStringExtra(EXTRA_CRYPTO_NAME);
        if (cryptoName == null) {
            throw new IllegalArgumentException("Detail Activity requires a crypto name@");
        }

        cryptoId = getIntent().getStringExtra(EXTRA_CRYPTO_ID);
        if (cryptoId == null) {
            throw new IllegalArgumentException("Detail Activity requires a crypto id@");
        }

        cryptoSymbol = getIntent().getStringExtra(EXTRA_CRYPTO_SYMBOL);
        if (cryptoSymbol == null) {
            throw new IllegalArgumentException("Detail Activity requires a crypto symbol@");
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(cryptoName.substring(0, 1).toUpperCase() + cryptoName.substring(1));

        errorView.setErrorListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        fetchData();
        mCurrencyFormat = NumberFormat.getCurrencyInstance();

    }

    private void fetchData() {
        detailPresenter.getCrypto(cryptoId, currency);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        currency = parent.getItemAtPosition(pos).toString();
        fetchData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        fetchData();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        detailPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        detailPresenter.detachView();
    }

    @Override
    public void showCryptoDetail(CryptoDetail cryptoDetail) {
        cryptoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showExchangeRates(ExchangeRate exchangeRate) {
        value.setText(mCurrencyFormat.format(exchangeRate.price));
        symbol.setText(cryptoSymbol);
    }

    @Override
    public void showProgress(boolean show) {
        errorView.setVisibility(View.GONE);
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        cryptoLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        Timber.e(error, "There was a problem retrieving the crypto...");
    }

    @Override
    public void onReloadData() {
        fetchData();
    }
}
