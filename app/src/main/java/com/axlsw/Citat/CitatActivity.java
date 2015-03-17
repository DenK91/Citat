package com.axlsw.Citat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Main Activity for Citat app.
 */
public class CitatActivity extends Activity {

    /**  Menu items id.  */
    final int MENU_ID_SEND = 100;
    final int MENU_ID_FAVORITES = 101;
    final int MENU_ID_EXIT = 102;
    final int SUB_MENU_ID_AUTH = 201;
    final int SUB_MENU_ID_SITE = 202;
    final int SUB_MENU_ID_PROD = 203;

    private static AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citat);
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
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
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
}
