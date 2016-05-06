package com.huawei.stellarfmr.stellarfmr.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.adapter.DrawerListAdapter;
import com.huawei.stellarfmr.stellarfmr.fragments.BrandingFragment;
import com.huawei.stellarfmr.stellarfmr.fragments.ChangePassFragment;
import com.huawei.stellarfmr.stellarfmr.fragments.CompetitorSalesFragment;
import com.huawei.stellarfmr.stellarfmr.fragments.MessageFragment;
import com.huawei.stellarfmr.stellarfmr.fragments.NewCompetitorInfo;
import com.huawei.stellarfmr.stellarfmr.fragments.OnlineAgentsFragment;
import com.huawei.stellarfmr.stellarfmr.fragments.PosmFragment;
import com.huawei.stellarfmr.stellarfmr.fragments.SaleStockFragment;
import com.huawei.stellarfmr.stellarfmr.fragments.ShelfSpaceFragment;
import com.huawei.stellarfmr.stellarfmr.fragments.SiteInfoFragment;
import com.huawei.stellarfmr.stellarfmr.fragments.TrainingFragment;
import com.huawei.stellarfmr.stellarfmr.listdata.NavigationListData;
import com.huawei.stellarfmr.stellarfmr.myalarmservice.AlarmReceiver;
import com.huawei.stellarfmr.stellarfmr.utils.Config;

import java.io.InputStream;
import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private PendingIntent pendingIntent;
    Bundle bundle;

    SharedPreferences sP;
    SharedPreferences.Editor editor;
    ActionBarDrawerToggle mDrawerToggle;
    ImageView company, brand,myavtar;
    TextView username, location;
    String[] name = new String[]{"Messages", "Journey Plan", "Site Visit Info", "Sales, Stock and orders", "POSM Info", "Branding", "Shelf Space Data", "Training Info", "Competitor Sales", "Competitor Info",
            "Online Agent",
            "Change Password", "Sign Out"};
    int[] icon = new int[]{R.drawable.messageicon,
            R.drawable.journeyicon,
            R.drawable.sitevisiticon,
            R.drawable.salesstockicon,
            R.drawable.posmicon,
            R.drawable.brandingicon,
            R.drawable.shelfspaceicon,
            R.drawable.trainingicon,
            R.drawable.competitorsalesicon,
            R.drawable.competitorinfoicon,
            R.drawable.onlineagenticon,
            R.drawable.passwordicon,
            R.drawable.signouticon};
    ArrayList<NavigationListData> myList = new ArrayList<>();
    Fragment fragment;
    FragmentTransaction transaction;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        if (savedInstanceState == null) {
            fragment = new MessageFragment();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            //transaction.addToBackStack(null);
            transaction.commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        username = (TextView) findViewById(R.id.userName);
        location = (TextView) findViewById(R.id.location);
       // Toast.makeText(HomePageActivity.this,"Network is "+Config.isNetworkAvailable(this)+"",Toast.LENGTH_LONG).show();

        try {
            username.setText(Config.userDetails.get(0)+" "+Config.userDetails.get(2));
            location.setText(Config.userDetails.get(4));
        } catch (Exception e) {
            e.printStackTrace();
        }

        company = (ImageView) findViewById(R.id.company);
        brand = (ImageView) findViewById(R.id.brand);
        myavtar = (ImageView) findViewById(R.id.avatar);
        //profileImage = (ImageView) findViewById(R.id.brand);
        new DownloadImageTask(company).execute(Config.userDetails.get(6));
        new DownloadImageTask(brand).execute(Config.userDetails.get(7));
        new DownloadImageTask(myavtar).execute(Config.userDetails.get(9));
        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(HomePageActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(HomePageActivity.this, 0, alarmIntent, 0);
        StartLocationTracker();


        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);

        // Drawer List
        mDrawerList = (ListView) findViewById(R.id.navList);
        getListViewData();
        mDrawerList.setAdapter(new DrawerListAdapter(this, myList));

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.blank, // open Drawer
                R.string.blank // close Drawer
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                mDrawerLayout.setClickable(false);
                mDrawerList.setClickable(false);
                mDrawerList.setVisibility(View.GONE);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerList.setVisibility(View.VISIBLE);
            }
        };

        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void StartLocationTracker() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = Integer.parseInt(Config.userDetails.get(16));

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        //Toast.makeText(this, "Location Tracker Start", Toast.LENGTH_SHORT).show();
    }

    private void selectItemFromDrawer(int position) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        switch (position) {
            case 0:
                 Bundle bundle;
                 bundle = new Bundle();
                bundle.putString("tag", "0");
                fragment = new MessageFragment();
                fragment.setArguments(bundle);
                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();

                mDrawerList.setItemChecked(position, true);
                break;

            case 1:
                bundle = new Bundle();
                bundle.putString("tag", "1");
                fragment = new MessageFragment();
                fragment.setArguments(bundle);
                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();

                mDrawerList.setItemChecked(position, true);
                break;

            case 2:
                fragment = new SiteInfoFragment();
                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();

                mDrawerList.setItemChecked(position, true);
                break;

            case 3:
                fragment = new SaleStockFragment();
                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();

                mDrawerList.setItemChecked(position, true);
                break;

            case 4:
                fragment = new PosmFragment();
                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();

                mDrawerList.setItemChecked(position, true);
                break;

            case 5:
                fragment = new BrandingFragment();
                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();

                mDrawerList.setItemChecked(position, true);
                break;

            case 6:
                fragment = new ShelfSpaceFragment();
                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();

                mDrawerList.setItemChecked(position, true);
                break;

            case 7:
                fragment = new TrainingFragment();
                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();

                mDrawerList.setItemChecked(position, true);
                break;

            case 8:
                fragment = new CompetitorSalesFragment();
                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();

                mDrawerList.setItemChecked(position, true);
                break;

            case 9:
                //fragment = new CompetitorFragment();

                fragment = new NewCompetitorInfo();
                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();

                mDrawerList.setItemChecked(position, true);
                break;

            case 10:
                if (Config.userDetails.get(17).toString().contains("FMRADMIN")){
                    fragment = new OnlineAgentsFragment();
                    transaction.replace(R.id.main_content, fragment);
                    //transaction.addToBackStack(null);
                    transaction.commit();

                    mDrawerList.setItemChecked(position, true);

                }else {
                    Config.dialog(HomePageActivity.this,"You are not authorize to see online agents.","Alert");
                }
                break;

            case 11:
                fragment = new ChangePassFragment();
                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();
                mDrawerList.setItemChecked(position, true);
                break;


            case 12:
                // Sign Out
                sP = getSharedPreferences("TOKEN", MODE_PRIVATE);
                editor = sP.edit().clear();
                editor.commit();
                finish();
                StopLocationTracker();
                Intent i = new Intent(HomePageActivity.this,LoginActivity.class);
                startActivity(i);

                break;
        }

        // Close the drawer

        try {
            mDrawerLayout.closeDrawer(mDrawerPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void StopLocationTracker() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
       // Toast.makeText(this, "Location Tracker Stopped", Toast.LENGTH_SHORT).show();
    }

    private void getListViewData() {
        for (int i = 0; i < icon.length; i++) {
            // Create a new object for each list item
            NavigationListData ld = new NavigationListData();
            ld.setName(name[i]);
            ld.setIcon(icon[i]);
            myList.add(ld);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (Config.siteInfo) {
                Config.siteInfo = false;
                SiteInfoFragment.reloadData(HomePageActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Config.shelfSpace) {
                Config.shelfSpace = false;
                ShelfSpaceFragment.reloadData(HomePageActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Config.posm) {
                Config.posm = false;
                PosmFragment.reloadData(HomePageActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Config.branding) {
                Config.branding = false;
                BrandingFragment.reloadData(HomePageActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Config.SalesSpace) {
                Config.SalesSpace = false;
                SaleStockFragment.reloadData(HomePageActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Config.competitorinfo) {
                Config.competitorinfo = false;
                NewCompetitorInfo.reloadData(HomePageActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null) bmImage.setImageBitmap(result);
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}