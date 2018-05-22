package io.mii.coin.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.mii.coin.data.model.present.CryptoSummary;

import static io.mii.coin.Constants.ALL;
import static io.mii.coin.Constants.FAVORITES;


@Singleton
public class PreferencesHelper {

    String PREF_FILE_NAME = "MIICOIN";

    private static SharedPreferences preferences = null;

    @Inject
    public PreferencesHelper(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        }
    }

    // This four methods are used for maintaining favorites.
    public void saveSharedPreference(String prefKey, List<CryptoSummary> cryptoList) {
        Gson gson = new Gson();
        String jsonCryptoList = gson.toJson(cryptoList);
        SharedPreferences.Editor edit = preferences.edit();
//        edit.clear();
        edit.putString(prefKey, jsonCryptoList);
        edit.commit();
    }

    public void addFavorite(CryptoSummary cryptoSummary) {
        addCrypto(FAVORITES, cryptoSummary);
        updateCryptoCheckStatus(ALL, cryptoSummary.id, true);
    }

    public void removeFavorite(CryptoSummary cryptoSummary) {
        removeCrypto(FAVORITES, cryptoSummary);
        updateCryptoCheckStatus(ALL, cryptoSummary.id, false);
    }

    public void updateCryptoCheckStatus(String prefKey, String id, boolean isChecked ) {
        List<CryptoSummary> cryptoList = getSharedPreference(prefKey);
        for (CryptoSummary crypto : cryptoList) {
            if (crypto.id.equals(id)) {
                crypto.setCheckStatus(isChecked);
                break;
            }
        }
        saveSharedPreference(prefKey, cryptoList);
    }

    private void addCrypto(String prefKey, CryptoSummary cryptoSummary) {
        List<CryptoSummary> cryptoList = getSharedPreference(prefKey);
        if (cryptoList == null)
            cryptoList = new ArrayList<CryptoSummary>();

        if (!cryptoList.contains(cryptoSummary)) {
            cryptoList.add(cryptoSummary);
        }

        saveSharedPreference(prefKey, cryptoList);
    }

    private void removeCrypto(String prefKey, CryptoSummary cryptoSummary) {
        ArrayList<CryptoSummary> cryptoList = getSharedPreference(prefKey);
        ArrayList<CryptoSummary> newCryptoList = new ArrayList<>();
        if (cryptoList != null) {
            for (CryptoSummary crypto : cryptoList) {
                if (!crypto.id.equals(cryptoSummary.id)) {
                    newCryptoList.add(crypto);
                }
            }
            saveSharedPreference(prefKey, newCryptoList);
        }
    }

    public ArrayList<CryptoSummary> getSharedPreference(String prefKey) {
        List<CryptoSummary> cryptoList;
        if (preferences.contains(prefKey)) {
            String jsonCryptoList = preferences.getString(prefKey, null);
            Gson gson = new Gson();
            CryptoSummary[] cryptoItems = gson.fromJson(jsonCryptoList,
                    CryptoSummary[].class);

            cryptoList = Arrays.asList(cryptoItems);
            cryptoList = new ArrayList<CryptoSummary>(cryptoList);
            return (ArrayList<CryptoSummary>) cryptoList;
        } else
            return new ArrayList<CryptoSummary>();
    }


    public boolean isFavorite(List<CryptoSummary> favorites, String id) {
        if (favorites == null) {
            return false;
        }

        for (CryptoSummary crypto : favorites) {
            if (crypto.id.equals(id)) {
                return true;
            }
        }
        return false;
    }

}
