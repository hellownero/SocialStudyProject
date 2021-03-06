package com.ndlp.socialstudy.NavigationDrawer_BottomNavigation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ndlp.socialstudy.NewsFeed.NewsFeedFragment;
import com.ndlp.socialstudy.NewsFeed.NotificationFragment;
import com.ndlp.socialstudy.Notifications.RegisterTokenAsyncTask;
import com.ndlp.socialstudy.R;
import com.ndlp.socialstudy.Skripte.SkripteFragment;
import com.ndlp.socialstudy.Stundenplan.CalendarFragment;
import com.ndlp.socialstudy.Umfragen.AktuelleUmfragenAnzeigen.BasicUmfragenFragment;
import com.ndlp.socialstudy.activity.TinyDB;
import com.testfairy.TestFairy;

import java.lang.reflect.Field;

/**
 * Activity to handle the fragments with bottomNavigationView and navigationDrawer
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    boolean doubleBackToExitPressedOnce = false;
    public static Activity fa;

    BottomNavigationView topnavigationview;
    Fragment selectedFragment;

    Boolean fromnotification;

    private String token;

    private Toolbar mToolbar;

    //nav draver view workaround
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface quicksand_regular = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand-Regular.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , quicksand_regular), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    //top nav gravity
    private static void adjustGravity(View v) {
        if (v.getId() == android.support.design.R.id.smallLabel) {
            ViewGroup parent = (ViewGroup) v.getParent();
            parent.setPadding(0, 0, 0, 0);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parent.getLayoutParams();
            params.gravity = Gravity.CENTER;
            parent.setLayoutParams(params);
        }

        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;

            for (int i = 0; i < vg.getChildCount(); i++) {
                adjustGravity(vg.getChildAt(i));
            }
        }
    }

    //top nav item width
    private static void adjustWidth(BottomNavigationView nav) {
        try {
            Field menuViewField = nav.getClass().getDeclaredField("mMenuView");
            menuViewField.setAccessible(true);
            Object menuView = menuViewField.get(nav);

            Field itemWidth = menuView.getClass().getDeclaredField("mActiveItemMaxWidth");
            itemWidth.setAccessible(true);
            itemWidth.setInt(menuView, Integer.MAX_VALUE);
        }
        catch (NoSuchFieldException e) {
            // TODO
        }
        catch (IllegalAccessException e) {
            // TODO
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //identifiying user to testfairy
        SharedPreferences sharedPrefLoginData = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        TestFairy.setUserId(sharedPrefLoginData.getString("firstname", "Unidentified User"));


        //firebase messaging handling notifications tokens in database eintragen
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();

        //register token
        TinyDB tinyDB = new TinyDB(this);
        token = tinyDB.getString("notificationtoken");
        Integer matrikelnummer;
        matrikelnummer = sharedPrefLoginData.getInt("matrikelnummer", 1);

        if (matrikelnummer != 1){
            new RegisterTokenAsyncTask(token, matrikelnummer).execute();
        }






        //for closing activity when navigating to logout
        fa = this;

        //declaring typefaces
        Typeface quicksand_regular = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand-Regular.otf");
        Typeface quicksand_bold = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand-Bold.otf");

        //  listener für click events in navigationDrawer -> calls method
        setNavigationViewListner();

        //  set toolbar and get a ActionBarDrawerToggle for onOptionsItemsSelected
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("");

        TextView tv_toolbar_title_workaround = (TextView) findViewById(R.id.tv_toolbar_title_workaround);
        tv_toolbar_title_workaround.setTypeface(quicksand_bold);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open, R.string.close);

        //  set a drawerListener for onOptionsItemsSelected
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /**
         * Get Permissions or check them
         */

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhaveWritePermission() || !checkIfAlreadyhaveReadPermission()) {
                requestForSpecificPermission();
            }
        }


        /**
         * BottomNavigationView
         */

        topnavigationview = (BottomNavigationView) findViewById(R.id.top_navigation_view);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setItemBackgroundResource(R.drawable.mainactivitybackgroundhighlight);
        bottomNavigationView.setItemIconTintList(null);

        topnavigationview.setItemBackgroundResource(R.drawable.mainactivitybackgroundhighlight_top);
        topnavigationview.setItemIconTintList(null);

        adjustWidth(topnavigationview);
        adjustGravity(topnavigationview);

        CustomTypefaceSpan typefaceSpan = new CustomTypefaceSpan("", quicksand_regular);
        for (int i = 0; i <topnavigationview.getMenu().size(); i++) {
            MenuItem menuItem = topnavigationview.getMenu().getItem(i);
            SpannableStringBuilder spannableTitle = new SpannableStringBuilder(menuItem.getTitle());
            spannableTitle.setSpan(typefaceSpan, 0, spannableTitle.length(), 0);
            menuItem.setTitle(spannableTitle);
        }

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 43, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 43, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        //  listen for clicks and navigate between fragments
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = NewsFeedFragment.newInstance();
                                topnavigationview.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_item2:
                                selectedFragment = MainMenuFragment.newInstance();
                                topnavigationview.setVisibility(View.GONE);
                                break;
                            case R.id.action_item3:
                                selectedFragment = CalendarFragment.newInstance();
                                topnavigationview.setVisibility(View.GONE);
                                break;
                        }

                        //  get an transaction when switching the fragment -> UE seems less buggy
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        FragmentManager fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        transaction.replace(R.id.frame_layout, selectedFragment);

                        transaction.commit();
                        return true;
                    }
        });

        topnavigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigaton_item_news:
                        selectedFragment = NewsFeedFragment.newInstance();
                        break;
                    case R.id.navigation_item_notification:
                        selectedFragment = NotificationFragment.newInstance();
                        break;

                }

                //  get an transaction when switching the fragment -> UE seems less buggy
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragmentManager fm = getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                transaction.replace(R.id.frame_layout, selectedFragment);

                transaction.commit();
                return true;
            }
        });

        //receive intent from notification for fragment navigation
        String menuintent = getIntent().getStringExtra("notificationintent");


        FragmentManager notificationfragmentmanager = getSupportFragmentManager();
        FragmentTransaction notificationtransaction = notificationfragmentmanager.beginTransaction();


        // If menuFragment is defined, then this activity was launched with a fragment selection
        if (menuintent != null) {

            // Here we can decide what do to -- perhaps load other parameters from the intent extras such as IDs, etc
            if (menuintent.equals("BasicUmfragenFragment")) {
                BasicUmfragenFragment basicUmfragenFragment = new BasicUmfragenFragment();
                notificationtransaction.replace(R.id.frame_layout, basicUmfragenFragment);
                notificationtransaction.addToBackStack(null);
                topnavigationview.setVisibility(View.GONE);
                notificationtransaction.commit();
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
            }else {
                String subfolder = menuintent.substring(0, menuintent.indexOf("!"));
                String categorie = menuintent.substring(menuintent.indexOf("!")+1);

                Bundle b = new Bundle();

                b.putString("category", categorie);
                b.putString("subFolder", subfolder);
                b.putBoolean("fromnotification", true);

                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                topnavigationview.setVisibility(View.GONE);


                SkripteFragment skripteFragment = new SkripteFragment();
                skripteFragment.setArguments(b);
                notificationtransaction.replace(R.id.frame_layout, skripteFragment);
                notificationtransaction.addToBackStack(null);
                notificationtransaction.commit();

            }

        } else {

            notificationtransaction.replace(R.id.frame_layout, NewsFeedFragment.newInstance());
            notificationtransaction.commit();
        }





    }

    //  listens for click events in the navigationDrawer & assigns typefaces to menu items
    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_drawer_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }

    }

    //  Öffnen des Navigation Drawers
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //  handles click events for the menu items -> starts activities
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_feedback_senden: {
                Intent intent = new Intent (MainActivity.this, FeedbackActivity.class);
                this.startActivity(intent);
                break;
            }
            case R.id.nav_about_us: {
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                this.startActivity(intent);
                break;
            }
            case R.id.nav_fehler_melden: {
                Intent intent = new Intent(MainActivity.this, FehlerMeldenAcivity.class);
                this.startActivity(intent);
                break;
            }
            case R.id.nav_account: {
                Intent intent = new Intent(MainActivity.this, MyAccount.class);
                this.startActivity(intent);
                break;
            }
            case R.id.nav_einstellungen: {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                this.startActivity(intent);
                break;
            }


        }

        //  close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkIfAlreadyhaveWritePermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIfAlreadyhaveReadPermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,}, 101);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read and write your External storage", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {


            //normal
            if (getFragmentManager().getBackStackEntryCount() > 0 ){
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }






    }





}

