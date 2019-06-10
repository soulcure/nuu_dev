package com.nuu.nuuinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.nuu.buy.BuyPackageFragment;
import com.nuu.contact.ContactUsFragment;
import com.nuu.devices.DevicesFragment;
import com.nuu.home.HomeFragment;
import com.nuu.login.LoginFragment;
import com.nuu.my.MyPackageFragment;
import com.nuu.my.PurchasePackageActivity;
import com.nuu.news.NewsFragment;
import com.nuu.tutorial.TutorialFragment;

public class Main2Activity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private TextView tv_title;
    private Menu menu;
    private long pressTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        fragmentManager = getSupportFragmentManager();
        initView();
    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        tv_title = (TextView) toolbar.findViewById(R.id.tv_title);

        skipToFragment(HomeFragment.TAG, null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentManager.findFragmentByTag(HomeFragment.TAG) == null) {
                skipToFragment(HomeFragment.TAG, null);
            } else {
                if (isFinish()) {
                    super.onBackPressed();
                } else {
                    Toast.makeText(this, R.string.exit_toast, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (fragmentManager.findFragmentByTag(BuyPackageFragment.TAG) != null) {
                Intent intent = new Intent(mContext, PurchasePackageActivity.class);
                intent.putParcelableArrayListExtra(PurchasePackageActivity.PACK_LIST, null);
                mContext.startActivity(intent);
            } else {
                skipToFragment(BuyPackageFragment.TAG, null);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            tv_title.setText(R.string.menu_home);
            menu.findItem(R.id.action_settings).setVisible(true);
            menu.findItem(R.id.action_settings).setIcon(R.drawable.ic_buy);

            skipToFragment(HomeFragment.TAG, null);
        } else if (id == R.id.nav_news) {
            tv_title.setText(R.string.menu_news);
            menu.findItem(R.id.action_settings).setVisible(false);

            skipToFragment(NewsFragment.TAG, null);
        } else if (id == R.id.nav_tutorial) {
            tv_title.setText(R.string.menu_tutorial);
            menu.findItem(R.id.action_settings).setVisible(false);

            skipToFragment(TutorialFragment.TAG, null);
        } else if (id == R.id.nav_buy) {
            tv_title.setText(R.string.menu_tutorial);

            menu.findItem(R.id.action_settings).setVisible(true);
            menu.findItem(R.id.action_settings).setIcon(R.drawable.ic_history);

            skipToFragment(BuyPackageFragment.TAG, null);
        } else if (id == R.id.nav_package) {
            tv_title.setText(R.string.menu_package);

            menu.findItem(R.id.action_settings).setVisible(false);

            skipToFragment(MyPackageFragment.TAG, null);
        } else if (id == R.id.nav_device) {
            tv_title.setText(R.string.menu_device);


            menu.findItem(R.id.action_settings).setVisible(false);
            skipToFragment(DevicesFragment.TAG, null);
        } else if (id == R.id.nav_login) {
            tv_title.setText(R.string.menu_login);
            skipToFragment(LoginFragment.TAG, null);
        } else if (id == R.id.nav_contact) {
            tv_title.setText(R.string.menu_contact);

            menu.findItem(R.id.action_settings).setVisible(false);
            skipToFragment(ContactUsFragment.TAG, null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private boolean isFinish() {
        boolean b = false;
        long curTime = System.currentTimeMillis();
        if (curTime - pressTime < 2000) {
            b = true;
        }
        pressTime = curTime;
        return b;
    }

    public void skipToFragment(String tag, Bundle bundle) {
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (fragmentManager.findFragmentByTag(tag) == null) {
            Fragment frag = null;
            if (tag.equals(HomeFragment.TAG)) {
                frag = new HomeFragment();
            } else if (tag.equals(NewsFragment.TAG)) {
                frag = new NewsFragment();
            } else if (tag.equals(TutorialFragment.TAG)) {
                frag = new TutorialFragment();
            } else if (tag.equals(BuyPackageFragment.TAG)) {
                frag = new BuyPackageFragment();
            } else if (tag.equals(MyPackageFragment.TAG)) {
                frag = new MyPackageFragment();
            } else if (tag.equals(DevicesFragment.TAG)) {
                frag = new DevicesFragment();
            } else if (tag.equals(LoginFragment.TAG)) {
                frag = new LoginFragment();
            } else if (tag.equals(ContactUsFragment.TAG)) {
                frag = new ContactUsFragment();
            }

            if (frag != null) {
                if (bundle != null) {
                    frag.setArguments(bundle);
                }
                ft.replace(R.id.content, frag, tag);
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.commitAllowingStateLoss();
            }
        }
    }
}
