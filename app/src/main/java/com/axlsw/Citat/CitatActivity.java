package com.axlsw.Citat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Main Activity for Citat app.
 */
public class CitatActivity extends Activity {

    private static AdView mAdView;

    /**  Menu items id.  */
    final private static int MENU_ID_SEND = 100;
    final private static int MENU_ID_FAVORITES = 101;
    final private static int MENU_ID_EXIT = 102;
    final private static int SUB_MENU_ID_AUTH = 201;
    final private static int SUB_MENU_ID_SITE = 202;
    final private static int SUB_MENU_ID_PROD = 203;

    /**  SharedPreferences.     */
    private SharedPreferences mSharedPreferences;
    final private static String PREFERENCES_CITAT_ACTIVITY = "preferences_citat_activity";

    /**  Keys for SharedPreferences.  */
    final private static String KEY_CURRENT_CITAT = "com.axlsw.citat.key_current_citat";
    final private static String KEY_FAVORITES_LIST = "com.axlsw.citat.key_favorites_list";

    final private static String SEPARATOR_FOR_FAVORITES = ":";

    private List<String> mCitatList;
    private List<String> mAuthList;
    private int mCurrentCitat;
    private int mMaxCitats;
    private List<Integer> mFavoritesList;

    /** Views */
    private TextView mCitatNumber;
    private TextView mCitatText;
    private TextView mCitatAuth;
    private ImageView mButtonPrev;
    private ImageView mButtonNext;
    private ImageView mButtonShare;
    private ImageView mButtonRand;
    private ImageView mButtonAddToFavorites;
    private ImageView mButtonGoToFavorites;
    private ImageView mButtonBash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citat);

        /** Load content. */
        mCitatList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_of_citats)));
        mAuthList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_of_authors)));
        mMaxCitats = mCitatList.size();
        mCurrentCitat = loadCurrentCitatFromPreferences();
        mFavoritesList = loadFavoritesListFromPreferences();
        if(mCurrentCitat == -1) {
            mCurrentCitat = 0;
            showTostWithMessage(this, "first");
        }
        if (mCurrentCitat > (mMaxCitats - 1)) {
            mCurrentCitat = 0;
        }

        mCitatNumber = (TextView) findViewById(R.id.citat_number);
        mCitatText = (TextView) findViewById(R.id.citat_text);
        mCitatAuth = (TextView) findViewById(R.id.citat_auth);
        mButtonPrev = (ImageView) findViewById(R.id.button_prew);
        mButtonNext = (ImageView) findViewById(R.id.button_next);
        mButtonRand = (ImageView) findViewById(R.id.button_random);
        mButtonShare = (ImageView) findViewById(R.id.button_share);
        mButtonAddToFavorites = (ImageView) findViewById(R.id.button_like_favorites);
        mButtonGoToFavorites = (ImageView) findViewById(R.id.button_go_to_favorites);
        mButtonBash = (ImageView) findViewById(R.id.button_bash);
        setCurrentCitat();

        initControls();
        initAdMob();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu aMenu) {
        aMenu.add(Menu.NONE, MENU_ID_SEND, Menu.NONE, R.string.menu_text_send);
        aMenu.add(Menu.NONE, MENU_ID_FAVORITES, Menu.NONE, R.string.menu_text_favorites);
        SubMenu subMenu = aMenu.addSubMenu(R.string.menu_text_about);
        aMenu.add(Menu.NONE, MENU_ID_EXIT, Menu.NONE, R.string.menu_text_exit);
        subMenu.add(Menu.NONE, SUB_MENU_ID_AUTH, Menu.NONE, R.string.sub_menu_text_auth);
        subMenu.add(Menu.NONE, SUB_MENU_ID_SITE, Menu.NONE, R.string.sub_menu_text_site);
        subMenu.add(Menu.NONE, SUB_MENU_ID_PROD, Menu.NONE, R.string.sub_menu_text_prod);
        return super.onCreateOptionsMenu(aMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem aItem) {
        switch (aItem.getItemId()) {
            case MENU_ID_SEND:
                showTostWithMessage(this, getString(R.string.toast_message_send));
                sendCurrentCitat();
                break;
            case MENU_ID_FAVORITES:
                showTostWithMessage(this, getString(R.string.toast_message_favorites));
                //TODO: after adding favorite
                Intent intentFav = new Intent(this, FavoritesActivity.class);
                startActivity(intentFav);
                break;
            case SUB_MENU_ID_AUTH:
                showTostWithMessage(this, getString(R.string.toast_message_auth));
                break;
            case SUB_MENU_ID_SITE:
                showTostWithMessage(this, getString(R.string.toast_message_site));
                break;
            case SUB_MENU_ID_PROD:
                showTostWithMessage(this, getString(R.string.toast_message_prod));
                break;
            case MENU_ID_EXIT:
                showTostWithMessage(this, getString(R.string.toast_message_exit));
                this.finish();
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Init controls.
     */
    void initControls() {
        mButtonPrev.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View aView) {
                        if (mCurrentCitat == 0) {
                            mCurrentCitat = mMaxCitats - 1;
                        } else {
                            mCurrentCitat--;
                        }
                        setCurrentCitat();
                    }
                });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View aView) {
                        if (mCurrentCitat >= (mMaxCitats - 1)) {
                            mCurrentCitat = 0;
                        } else {
                            mCurrentCitat++;
                        }
                        setCurrentCitat();
                    }
                });

        mButtonRand.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View aView) {
                        mCurrentCitat = generateRandomCitatNumber();
                        setCurrentCitat();
                    }
                });

        mButtonShare.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View aView) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, mCitatList.get(mCurrentCitat) + "\n\n"
                                + mAuthList.get(mCurrentCitat) + "\n\n"
                                + getString(R.string.share_mesage) + " \""
                                + getString(R.string.app_name) + "\"");
                        startActivity(Intent.createChooser(intent,
                                getString(R.string.share_way)));

                        showTostWithMessage(aView.getContext(), getString(R.string.toast_message_send));
                    }
                });

        mButtonAddToFavorites.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View aView) {
                        if (!mFavoritesList.contains(mCurrentCitat)) {
                            mFavoritesList.add(mCurrentCitat);
                            mButtonAddToFavorites.setImageResource(R.drawable.button_favorite_remove);
                        } else {
                            mFavoritesList.remove(mFavoritesList.indexOf(mCurrentCitat));
                            mButtonAddToFavorites.setImageResource(R.drawable.button_favorite_add);
                        }
                    }
                });

        mButtonGoToFavorites.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View aView) {
                        showTostWithMessage(aView.getContext(), getString(R.string.toast_message_favorites));
                        //TODO: favorites
                        Intent intentFav = new Intent(aView.getContext(), FavoritesActivity.class);
                        startActivity(intentFav);
                    }
                });

        mButtonBash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View aView) {
                        showTostWithMessage(aView.getContext(), getString(R.string.toast_message_online));
                    }
                });
    }

    /**
     * Init adView.
     */
    void initAdMob() {
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        mAdView.loadAd(request);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                mAdView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onPause() {
        saveCurrentCitatToPreferences(mCurrentCitat);
        saveFavoritesListToPreferences(mFavoritesList);
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }

    /**
     * Load current citat from shared preferences.
     *
     * @return current citat.
     */
    int loadCurrentCitatFromPreferences() {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences(PREFERENCES_CITAT_ACTIVITY, MODE_PRIVATE);
        }
        return mSharedPreferences.getInt(KEY_CURRENT_CITAT, -1);
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

    /**
     * Saves current citat to shared preferences.
     *
     * @param aCurrentCitat will be saved.
     */
    private void saveCurrentCitatToPreferences(int aCurrentCitat) {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences(PREFERENCES_CITAT_ACTIVITY, MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_CURRENT_CITAT, aCurrentCitat);
        editor.apply();
    }

    /**
     * Saves favorites list to shared preferences.
     *
     * @param aFavoritesList will be saved.
     */
    private void saveFavoritesListToPreferences(List<Integer> aFavoritesList) {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences(PREFERENCES_CITAT_ACTIVITY, MODE_PRIVATE);
        }
        String favString = "";
        for (int i = 0; i < aFavoritesList.size(); i++) {
            if (i != 0) {
                favString += SEPARATOR_FOR_FAVORITES + aFavoritesList.get(i);
            } else {
                favString += aFavoritesList.get(i);
            }
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_FAVORITES_LIST, favString);
        editor.apply();
    }

    /**
     * Sets current citat.
     */
    void setCurrentCitat() {
        mCitatNumber.setText(Integer.toString(mCurrentCitat + 1));
        mCitatText.setText(mCitatList.get(mCurrentCitat));
        mCitatAuth.setText(mAuthList.get(mCurrentCitat));
        if (mFavoritesList.contains(mCurrentCitat)) {
            mButtonAddToFavorites.setImageResource(R.drawable.button_favorite_remove);
        } else {
            mButtonAddToFavorites.setImageResource(R.drawable.button_favorite_add);
        }
    }

    /**
     * Send intent with current citat in extra for citat sending.
     */
    private void sendCurrentCitat() {
        /*TODO: sendCitat
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getCitat()[current] + "\n\n"
                + getAuth()[current] + "\n\n"
                + getString(R.string.send_mesage) + " \""
                + getString(R.string.app_name) + "\"");
        startActivity(Intent.createChooser(intent,
                getString(R.string.send_way2)));*/
    }

    /**
     * Shows toast with message.
     *
     * @param aContext context.
     * @param aMsg message for show.
     */
    public static void showTostWithMessage(Context aContext, String aMsg) {
        Toast.makeText(aContext, aMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Generate random number from range of MaxCitat.
     * @return random number from range of MaxCitat.
     */
    int generateRandomCitatNumber() {
        return Math.abs(new Random().nextInt()) % mMaxCitats;
    }
}
