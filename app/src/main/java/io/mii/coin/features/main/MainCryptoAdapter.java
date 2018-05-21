package io.mii.coin.features.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.mii.coin.R;
import io.mii.coin.data.local.PreferencesHelper;
import io.mii.coin.data.model.present.CryptoSummary;
import io.mii.coin.util.ViewUtil;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

@SuppressWarnings("unchecked")
public class MainCryptoAdapter extends RecyclerView.Adapter<MainCryptoAdapter.CryptoViewHolder> implements Filterable {

    private List<CryptoSummary> cryptoList;
    private List<CryptoSummary> filteredList;
    private Subject<CryptoSummary> cryptoClickSubject;

    @Inject
    MainCryptoAdapter() {
        cryptoClickSubject = PublishSubject.create();
        cryptoList = Collections.emptyList();
        filteredList = Collections.emptyList();
    }

    public void setCrypto(List<CryptoSummary> crypto) {
        this.cryptoList = crypto;
        this.filteredList = crypto;
        notifyDataSetChanged();
    }

    @Override
    public CryptoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_crypto, parent, false);
        return new CryptoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CryptoViewHolder holder, int position) {
        CryptoSummary crypto = this.filteredList.get(position);
        holder.onBind(crypto);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    Observable<CryptoSummary> getCryptoClick() {
        return cryptoClickSubject;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = cryptoList;
                } else {
                    filteredList = ViewUtil.searchCryotoFilter(cryptoList, charString);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (List<CryptoSummary>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class CryptoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_icon)
        ImageView iconImage;

        @BindView(R.id.text_symbol)
        TextView symbolText;

        @BindView(R.id.text_price)
        TextView priceText;

        @BindView(R.id.text_rank)
        TextView rankText;

        @BindView(R.id.text_name)
        TextView nameText;

        @BindView(R.id.text_percent_24)
        TextView percent24Text;

        @BindView(R.id.checkbox_favorite)
        CheckBox cbxFavorite;

        private NumberFormat mCurrencyFormat;
        private NumberFormat mPercentFormat;

        private PreferencesHelper mPreferenceHelper;


        @OnCheckedChanged(R.id.checkbox_favorite)
        void addToFavorite(CompoundButton button, boolean checked) {
            if (this.crypto.getCheckStatus() == checked) {
                return;
            }
            this.crypto.setCheckStatus(checked);
            if (checked) {
                mPreferenceHelper.addFavorite(new CryptoSummary(this.crypto));
            } else {
                mPreferenceHelper.removeFavorite(new CryptoSummary(this.crypto));
            }
        }

        private CryptoSummary crypto;
        private Context context;

        CryptoViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> cryptoClickSubject.onNext(crypto));
            mCurrencyFormat = NumberFormat.getCurrencyInstance();
            mPercentFormat = NumberFormat.getPercentInstance();
            mPercentFormat.setMaximumFractionDigits( 2 );
            mPreferenceHelper = new PreferencesHelper(this.context);
        }

        void onBind(CryptoSummary crypto) {
            this.crypto = crypto;
            nameText.setText(
                    String.format(
                            "%s%s", crypto.name.substring(0, 1).toUpperCase(), crypto.name.substring(1)));

            rankText.setText(
                    String.format(
                            "#%s", crypto.rank, crypto.rank));

            symbolText.setText(
                    String.format(
                            "%s%s", crypto.symbol.substring(0, 1).toUpperCase(), crypto.symbol.substring(1)));

            try {
                Class res = R.drawable.class;
                Field field = res.getField(crypto.symbol.toLowerCase());
                int drawableId = field.getInt(null);
                iconImage.setImageResource(drawableId);
            }
            catch (Exception e) {
                Log.e("ICON", "Failure to get drawable id.", e);
            }

            priceText.setText(mCurrencyFormat.format(crypto.price));

            percent24Text.setText(mPercentFormat.format(crypto.percentChange24h / 100.0f));
            if (crypto.percentChange24h  < 0) {
                percent24Text.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                percent24Text.setTextColor(context.getResources().getColor(R.color.green));
            }

            cbxFavorite.setChecked(crypto.getCheckStatus());

        }
    }
}
