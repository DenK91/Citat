package com.axlsw.Citat;

import android.app.Activity;
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

    private List<String> mCitatList;
    private List<String> mAuthList;
    private int mCurrentCitat;
    private int mMaxCitats;

    /** Views */
    private TextView mCitatNumber;
    private TextView mCitatText;
    private TextView mCitatAuth;

    /** Controls */
    private ImageView mButtonPrev;
    private ImageView mButtonNext;
    private ImageView mButtonRandom;
    private ImageView mButtonFavorites;
    private ImageView mButtonBash;
    private ImageView mButtonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citat);

        /** Load content. */
        mCitatList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.array_of_citats)));
        mAuthList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.array_of_authors)));
        mMaxCitats = mCitatList.size();
        mCurrentCitat = loadCurrentCitatFromPreferences();
        if(mCurrentCitat == -1) {
            mCurrentCitat = 0;
            Toast.makeText(this, "first", Toast.LENGTH_SHORT).show();
        }
        if (mCurrentCitat > (mMaxCitats - 1)) {
            mCurrentCitat = 0;
        }

        mCitatNumber = (TextView) findViewById(R.id.citat_number);
        mCitatText = (TextView) findViewById(R.id.citat_text);
        mCitatAuth = (TextView) findViewById(R.id.citat_auth);
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
                Toast.makeText(this, getString(R.string.toast_message_send), Toast.LENGTH_SHORT).show();
                sendCurrentCitat();
                break;
            case MENU_ID_FAVORITES:
                Toast.makeText(this, getString(R.string.toast_message_favorites), Toast.LENGTH_SHORT).show();
                //TODO: after adding favorite
                //Intent intentFav = new Intent(this, FavoriteActivity.class);
                //startActivity(intentFav);
                break;
            case SUB_MENU_ID_AUTH:
                Toast.makeText(this, getString(R.string.toast_message_auth), Toast.LENGTH_SHORT).show();
                break;
            case SUB_MENU_ID_SITE:
                Toast.makeText(this, getString(R.string.toast_message_site), Toast.LENGTH_SHORT).show();
                break;
            case SUB_MENU_ID_PROD:
                Toast.makeText(this, getString(R.string.toast_message_prod), Toast.LENGTH_SHORT).show();
                break;
            case MENU_ID_EXIT:
                Toast.makeText(this, getString(R.string.toast_message_exit), Toast.LENGTH_SHORT).show();
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
        mButtonPrev = (ImageView) findViewById(R.id.button_prew);
        mButtonNext = (ImageView) findViewById(R.id.button_next);
        mButtonRandom = (ImageView) findViewById(R.id.button_random);
        mButtonFavorites = (ImageView) findViewById(R.id.button_add_favorites);
        mButtonBash = (ImageView) findViewById(R.id.button_bash);
        mButtonShare = (ImageView) findViewById(R.id.button_share);

        mButtonPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mCurrentCitat == 0) {
                    mCurrentCitat = mMaxCitats - 1;
                } else {
                    mCurrentCitat--;
                }
                setCurrentCitat();
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mCurrentCitat >= (mMaxCitats - 1)) {
                    mCurrentCitat = 0;
                } else {
                    mCurrentCitat++;
                }
                setCurrentCitat();
            }
        });

        mButtonRandom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCurrentCitat = generateRandomCitatNumber();
                setCurrentCitat();
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
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        saveCurrentCitatToPreferences(mCurrentCitat);
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
     * Saves current citat to shared preferences.
     *
     * @param aCurrentCitat will be saved
     */
    private void saveCurrentCitatToPreferences(int aCurrentCitat) {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences(PREFERENCES_CITAT_ACTIVITY, MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_CURRENT_CITAT, aCurrentCitat);
        editor.commit();
    }

    /**
     * Sets current citat.
     */
    void setCurrentCitat() {
        mCitatNumber.setText(Integer.toString(mCurrentCitat + 1));
        mCitatText.setText(mCitatList.get(mCurrentCitat));
        mCitatAuth.setText(mAuthList.get(mCurrentCitat));
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

    int generateRandomCitatNumber() {
        return Math.abs(new Random().nextInt()) % mMaxCitats;
    }
}
