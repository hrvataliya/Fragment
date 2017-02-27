package com.brainstorm.hardik.allwishas.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brainstorm.hardik.allwishas.R;
import com.brainstorm.hardik.allwishas.fragment.FragmentDrawer;
import com.brainstorm.hardik.allwishas.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    public static MainActivity obj;
    public static Toolbar toolbar;
    public static ImageView menu, back,home,search,cross,blank;
    public static TextView lblTitle;
    public static EditText searchbar;
    public static FragmentDrawer drawerFragment;
    FrameLayout frameContainer;
    public static FragmentManager f;
    public static int id = R.id.container_body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout l = (LinearLayout) findViewById(R.id.toolbar1);
        toolbar = (Toolbar) l.findViewById(R.id.toolbar123);
        frameContainer = (FrameLayout) findViewById(R.id.container_body);
        obj=MainActivity.this;
        toolbar.setTitle("Home");
        lblTitle = (TextView) toolbar.findViewById(R.id.lblTitle);
        lblTitle.setText("Home");
        menu = (ImageView) toolbar.findViewById(R.id.menu);
        back = (ImageView) toolbar.findViewById(R.id.back);
        home = (ImageView) toolbar.findViewById(R.id.home);
        searchbar = (EditText) toolbar.findViewById(R.id.searchbar);
        search = (ImageView) toolbar.findViewById(R.id.search);
        cross = (ImageView) toolbar.findViewById(R.id.cross);
        blank = (ImageView) toolbar.findViewById(R.id.blank);

        lblTitle.setVisibility(View.VISIBLE);
        home.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        searchbar.setVisibility(View.GONE);
        search.setVisibility(View.VISIBLE);
        cross.setVisibility(View.GONE);
        blank.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ActionBarDrawerToggle ad = new ActionBarDrawerToggle(this, drawer,
                toolbar, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        );

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(MainActivity.this);

        initilizedFirstFragment();

    }

    private void initilizedFirstFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public static FragmentRefreshListener fragmentRefreshListener;

    public static FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }
    public interface FragmentRefreshListener {
        void onRefresh();
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    int backStackEntryCount = manager.getBackStackEntryCount();
                    if (backStackEntryCount == 0) {
                        finish();
                    }
                    Fragment fragment = manager.getFragments()
                            .get(backStackEntryCount - 1);
                    fragment.onResume();
                }
            }
        };
        return result;
    }



    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                //fragment = new MyProfileFragment();
                break;
            case 2:
                //fragment = new AllRequetstedFragment();
                break;
            case 3:
                //fragment = new RatesFragment();
                break;
            case 4:
                //fragment = new HistoryFragment();
                break;
            case 5:
                //fragment = new CreditCardFragment();
                break;
            case 6:
                //fragment = new ContactusFragment();
                break;
            case 7:
                //alertLogout();
                break;

            default:
                fragment = new HomeFragment();
                break;

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            lblTitle.setText(title);
            toolbar.setTitle(title);

        }
        else{
            initilizedFirstFragment();
        }
    }


    @Override
    public void onBackPressed()
    {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container_body);
        /*if (frag instanceof Dashboard && drawer.isDrawerOpen(GravityCompat.START))
        {
            drawerFragment.closeDrawers();
        }*/
        if(frag instanceof HomeFragment)
        {
            this.finish();
        }

        else
        {
            super.onBackPressed();
        }

    }

}
