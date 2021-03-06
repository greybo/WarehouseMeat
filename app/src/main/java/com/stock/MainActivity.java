package com.stock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.stock.activity.LoginActivity;
import com.stock.entity.Meat;
import com.stock.fragment.AllListFragment;
import com.stock.fragment.DetailsFragment;
import com.stock.utils.StockUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.stock.utils.StockConstants.TAG_ALLLISTFRAGMENT;
import static com.stock.utils.StockConstants.TAG_DETAILFRAGMENT;

public class MainActivity extends AppCompatActivity {

    //    TODO
    private static final String TAG = "log_tag";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.splashScreenTheme);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        showCurrentFragment();
    }

    private void createMenu() {
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsAbsolute(160, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (getTagFrg(this) == null)
            return false;
        if (getTagFrg(this).equals(TAG_DETAILFRAGMENT)) {
            menu.findItem(R.id.action_save).setVisible(true);
            menu.findItem(R.id.action_add).setVisible(false);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }
        if (getTagFrg(this).equals(TAG_ALLLISTFRAGMENT)) {
            menu.findItem(R.id.action_save).setVisible(false);
            menu.findItem(R.id.action_add).setVisible(true);
            toolbar.setNavigationIcon(null);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.containerFragment);
        DetailsFragment frg;
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                //TODO
//                frg = (DetailsFragment) fragment;
//                frg.saveDetailMeat();
//                StockUtil.changeFragment(this, new AllListFragment(), "AllListFragment");
                onBackPressed();
                break;
            case R.id.action_save:
                if (fragment instanceof DetailsFragment) {
                    frg = (DetailsFragment) fragment;
                    frg.saveDetailMeat();
                }
                break;
            case R.id.action_add:
                StockUtil.changeFragment(this,
                        DetailsFragment.newInstance(new Meat()), "DetailsFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            exit();
            return;
        }
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
        changeAll();
    }

    private void changeAll() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.containerFragment);
        if (fragment != null) {
            setTagFrg(this, fragment.getClass().getSimpleName());
        }
        showCurrentFragment();
    }

    public static void setTitle(Activity activity, String title) {
        ((TextView) activity.findViewById(R.id.toolbar_title)).setText(title);
    }

    public static void setTagFrg(Activity activity, String tagFrg) {
        ((StockApp) activity.getApplication()).setTagFrg(tagFrg);
        ((MainActivity) activity).createMenu();
    }

    public String getTagFrg(Activity activity) {
        return ((StockApp) activity.getApplication()).getTagFrg();
    }

    private void exit() {
        new MaterialDialog.Builder(this)
                .content("Хотите выйти из приложения?")
                .positiveText("Выйти")
                .negativeText("Отмена")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }

    //TODO
    private void showCurrentFragment() {
        Fragment frg = null;
        if (getTagFrg(this) == null) {
            setTagFrg(this, TAG_ALLLISTFRAGMENT);
        }
        if (getTagFrg(this).equals(TAG_ALLLISTFRAGMENT)) {
            frg = new AllListFragment();
        }
        if (getTagFrg(this).equals(TAG_DETAILFRAGMENT)) {
            frg = new DetailsFragment();
        }
        StockUtil.changeFragment(this, frg, "main");
    }
}
