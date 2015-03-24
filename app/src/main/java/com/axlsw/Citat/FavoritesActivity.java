package com.axlsw.Citat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FavoritesActivity extends Activity {

    private List<String> mCitatList;
    private List<String> mAuthList;
    private List<Integer> mFavoritesList;

    /**  SharedPreferences.     */
    private SharedPreferences mSharedPreferences;
    final private static String PREFERENCES_CITAT_ACTIVITY = "preferences_citat_activity";

    /**  Key for SharedPreferences.  */
    final private static String KEY_FAVORITES_LIST = "com.axlsw.citat.key_favorites_list";
    final private static String SEPARATOR_FOR_FAVORITES = ":";

    /** Views */
    private TextView mCitatNumber;
    private TextView mCitatText;
    private TextView mCitatAuth;
    private ImageView mButtonPrev;
    private ImageView mButtonNext;
    private ImageView mButtonShare;
    private ImageView mButtonRand;
    private ImageView mButtonFavorites;
    private ImageView mButtonBash;

    int current = 0;
    int currentFav = 0;
    int max = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citat);

        mCitatList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_of_citats)));
        mAuthList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_of_authors)));

        mFavoritesList = loadFavoritesListFromPreferences();

        max = mFavoritesList.size();

        mCitatNumber = (TextView) findViewById(R.id.citat_number);
        mCitatText = (TextView) findViewById(R.id.citat_text);
        mCitatAuth = (TextView) findViewById(R.id.citat_auth);
        mButtonPrev = (ImageView) findViewById(R.id.button_prew);
        mButtonNext = (ImageView) findViewById(R.id.button_next);
        mButtonRand = (ImageView) findViewById(R.id.button_random);
        mButtonShare = (ImageView) findViewById(R.id.button_share);
        mButtonFavorites = (ImageView) findViewById(R.id.button_go_to_favorites);
        mButtonBash = (ImageView) findViewById(R.id.button_bash);

        if (max < 1) {
            mCitatNumber.setVisibility(View.INVISIBLE);
            mCitatAuth.setVisibility(View.INVISIBLE);
            mButtonPrev.setVisibility(View.INVISIBLE);
            mButtonNext.setVisibility(View.INVISIBLE);
            mButtonRand.setVisibility(View.INVISIBLE);

            mCitatText.setText(R.string.have_not_favorite);
        }

        mButtonFavorites.setVisibility(View.INVISIBLE);
        mButtonBash.setVisibility(View.INVISIBLE);
        mButtonShare.setVisibility(View.INVISIBLE);

        drawScren();

        mButtonPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentFav--;
                if (currentFav < 0) {
                    currentFav = max - 1;
                }
                drawScren();
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentFav++;
                if (currentFav >= max) {
                    currentFav = 0;
                }
                drawScren();
            }
        });

        mButtonRand.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentFav = generateRandom(max);
                if (currentFav < 0) {
                    currentFav = 0;
                }
                if (currentFav > max) {
                    currentFav = max;
                }
                drawScren();
            }
        });
    }

    void drawScren() {
        if (currentFav < max) {
            current = mFavoritesList.get(currentFav);
        } else {
            current = 0;
        }
        mCitatNumber.setTextSize(20);
        mCitatNumber.setText(new StringBuilder((currentFav+1) + " (" + (current + 1)
                + ")").toString());
        mCitatText.setText(mCitatList.get(current));
        mCitatAuth.setText(mAuthList.get(current));
    }

    int generateRandom(int p) {
        Random random = new Random();
        return Math.abs(random.nextInt()) % p;
    }

    /**
     * Load favorites list from shared preferences.
     *
     * @return favorites list.
     */
    List<Integer> loadFavoritesListFromPreferences() {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences(PREFERENCES_CITAT_ACTIVITY, MODE_PRIVATE);
        }
        String favString = mSharedPreferences.getString(KEY_FAVORITES_LIST, "");
        List<Integer> favList = new ArrayList<>();
        if (!favString.isEmpty()) {
            String[] favArray = favString.split(SEPARATOR_FOR_FAVORITES);
            for (String i : favArray) {
                favList.add(Integer.valueOf(i));
            }
        }
        return favList;
    }
}

