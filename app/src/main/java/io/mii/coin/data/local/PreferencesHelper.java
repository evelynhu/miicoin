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


@Singleton
public class PreferencesHelper {

    public static final String FAVORITES = "CRYPTO_FAVORITE";
    String PREF_FILE_NAME = "MIICOIN";

    private static SharedPreferences preferences = null;

    @Inject
    public PreferencesHelper(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        }
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(List<CryptoSummary> favorites) {
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.putString(FAVORITES, jsonFavorites);
        edit.commit();
    }

    public void addFavorite(CryptoSummary cryptoSummary) {
        List<CryptoSummary> favorites = getFavorites();
        if (favorites == null)
            favorites = new ArrayList<CryptoSummary>();

        if (!favorites.contains(cryptoSummary)) {
          favorites.add(cryptoSummary);
        }

        saveFavorites(favorites);
    }

    public void removeFavorite(CryptoSummary cryptoSummary) {
        ArrayList<CryptoSummary> favorites = getFavorites();
        ArrayList<CryptoSummary> newFavorites = new ArrayList<>();
        if (favorites != null) {
            for (CryptoSummary crypto : favorites) {
                if (!crypto.id.equals(cryptoSummary.id)) {
                    newFavorites.add(crypto);
                }
            }
            saveFavorites(newFavorites);
        }
    }

    public ArrayList<CryptoSummary> getFavorites() {
        List<CryptoSummary> favorites;
        if (preferences.contains(FAVORITES)) {
            String jsonFavorites = preferences.getString(FAVORITES, null);
            Gson gson = new Gson();
            CryptoSummary[] favoriteItems = gson.fromJson(jsonFavorites,
                    CryptoSummary[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<CryptoSummary>(favorites);
            return (ArrayList<CryptoSummary>) favorites;
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

    public boolean isFavorite(String id) {
        List<CryptoSummary> favorites = getFavorites();

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
