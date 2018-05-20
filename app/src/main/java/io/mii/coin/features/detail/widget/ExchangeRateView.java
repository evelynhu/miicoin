package io.mii.coin.features.detail.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mii.coin.R;
import io.mii.coin.data.model.present.ExchangeRate;

public class ExchangeRateView extends RelativeLayout {

    @BindView(R.id.text_name)
    TextView nameText;

    @BindView(R.id.text_value)
    TextView valueText;

    public ExchangeRateView(Context context) {
        super(context);
        init();
    }

    public ExchangeRateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExchangeRateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExchangeRateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_exchange_rates, this);
        ButterKnife.bind(this);
    }

    @SuppressLint("SetTextI18n")
    public void setStat(ExchangeRate exchangeRate) {
        nameText.setText(exchangeRate.currency);

        valueText.setText(String.format("$%s", exchangeRate.price));
    }
}
