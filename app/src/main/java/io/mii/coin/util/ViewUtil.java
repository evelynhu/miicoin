package io.mii.coin.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import io.mii.coin.data.model.present.CryptoSummary;

public final class ViewUtil {

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }


    /**
     * Filtering crypto currency by search char sequence
     * @param list source crypto list
     * @param charString searching char sequence
     * @return filtered crypto list
     */
    public static List<CryptoSummary> searchCryotoFilter(List<CryptoSummary> list, String charString) {
        List<CryptoSummary> filteredTempList = new ArrayList<>();
        for (CryptoSummary cryptoSummary: list) {
            if (cryptoSummary != null ) {
                // Filter by user name and user id
                if (containsIgnoreCase(cryptoSummary.name, charString)
                        || containsIgnoreCase(String.valueOf(cryptoSummary.symbol), charString)) {
                    filteredTempList.add(cryptoSummary);
                }
            }
        }
        return filteredTempList;
    }


    /**
     * Search if substring has char sequence in source string ignore case
     * @param src source string
     * @param charString substring for searching
     * @return true if has coincidence
     */
    public static boolean containsIgnoreCase(String src, String charString) {
        final int length = charString.length();
        if (length == 0) {
            return true;
        }
        for (int i = src.length() - length; i >= 0; i--) {
            if (src.regionMatches(true, i, charString, 0, length)) {
                return true;
            }
        }
        return false;
    }
}
