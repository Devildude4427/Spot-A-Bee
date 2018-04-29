package com.assignment.spotabee;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.DatabaseInitializer;
import com.assignment.spotabee.database.Description;
import com.assignment.spotabee.fragments.DonationLogin;
import com.assignment.spotabee.fragments.FragmentAboutUs;
import com.assignment.spotabee.fragments.FragmentHome;
import com.assignment.spotabee.fragments.FragmentHowTo;
import com.assignment.spotabee.fragments.FragmentLeaderboard;
import com.assignment.spotabee.fragments.FragmentMap;
import com.assignment.spotabee.fragments.PaymentInfo;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import static com.assignment.spotabee.Config.Config.PAYPAL_REQUEST_CODE;


/**
 * This is the main backbone of the app. It handles all fragments
 * and non-fragment specific methods.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION_AND_ACCOUNTS = 0;
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int PERMISSION_REQUEST_ACCESS_ACCOUNT_DETAILS = 2;
    public static final int IMAGE_CAPTURE = 3;
    public static final int ACCESS_IMAGE_GALLERY = 4;
    public static final int PERMISSION_REQUEST_CAMERA = 5;
    public static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 6;
    public static final int CHOOSE_ACCOUNT = 99;

    private AccountManager accountManager;
    private static Context contextOfApplication;
    private AppDatabase db;
    public static final int PICK_IMAGE = 100;
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "Main Activity Debug";

    private int payPalResultCode;
    private Intent payPalData;

    private boolean mReturningWithResult = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accountManager = (AccountManager)
                getSystemService(Context.ACCOUNT_SERVICE);

        contextOfApplication = this;

        PermissionsManager.getInstance()
                .requestAllManifestPermissionsIfNecessary(this,
                        new PermissionsResultAction() {
                            @Override
                            public void onGranted() {
                                Log.v(TAG, "All Bueno");
                            }

                            @Override
                            public void onDenied(String permission) {
                                Log.v(TAG, "Cocks said no");
                            }
                        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED
                ) {
            Intent intent =
                    accountManager.newChooseAccountIntent(null,
                            null, new String[]{"com.google"},
                            null, null,
                            null, null);
            startActivityForResult(intent, CHOOSE_ACCOUNT);
        }


        db = AppDatabase.getAppDatabase(getApplicationContext());

        //This clears out the database, and is called every time! Remove if you need persistence!
        db.descriptionDao().nukeTable();
        //This clears out the database, and is called every time! Remove if you need persistence!

        DatabaseInitializer.populateAsync(db);

        Description description = new Description(
                51.4816, -3.1791,
                "Cardiff", "Rose", "None",
                1, "17-05-2018", "15:39");

        db.descriptionDao().insertDescriptions(description);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_home);

//        Intent intent = new Intent(this, ScreenService.class);
//        startService(intent);
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public AppCompatActivity getActivity() {
        return this;
    }

    /**
     * A static method that retrieves Activity context
     *
     * @return The context for the current activity
     */
    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method that handles all screen changes through the nav drawer.
     *
     * @param itemId A page, identified by "R.id.page_name",
     *               that the app wants to open.
     */
    private void displaySelectedScreen(final int itemId) {
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new FragmentHome();
                break;
            case R.id.nav_howto:
                fragment = new FragmentHowTo();
                break;
            case R.id.nav_results:
                fragment = new FragmentMap();
                break;
            case R.id.nav_aboutus:
                fragment = new FragmentAboutUs();
                break;
            case R.id.nav_leaderboard:
                fragment = new FragmentLeaderboard();
                break;
            case R.id.nav_donate:
                fragment = new DonationLogin();
                break;
            case R.id.nav_resources:
                fragment = new FragmentDownloadPdfGuide();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    /**
     * Creates gets details and confirms operation from account picker.
     *
     * @param requestCode An integer that relates to the permission. So
     *                    location might be 1, account access 2, and so on.
     * @param resultCode If the result succeeded or failed
     * @param data The intent that is being requested
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_ACCOUNT && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Log.v(TAG, accountName);
        } else if (requestCode == CHOOSE_ACCOUNT) {
            Log.v(TAG, "There was an error in the account picker");
        } else if (requestCode == Activity.RESULT_CANCELED) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, new FragmentHome());
            fragmentTransaction.commit();
        } else if (requestCode == PAYPAL_REQUEST_CODE) {
            mReturningWithResult = true;
            payPalData = data;
            payPalResultCode = resultCode;
        } else {
            Log.v(TAG, "Nothing exists to handle that request code" + requestCode);
        }
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    public void payPalResult(final int resultCode,
                             final Intent data) {
        if (resultCode == RESULT_OK) {
            PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirmation != null) {
                try {
                    String paymentDetails = confirmation.toJSONObject().toString(7);
                    PaymentInfo paymentInfo = new PaymentInfo();
                    Bundle args = new Bundle();
                    args.putString("paymentInfo", paymentDetails);
                    paymentInfo.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, paymentInfo).commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();

        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(getActivity(), "Invalid", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mReturningWithResult) {
            payPalResult(payPalResultCode, payPalData);
        }
        // Reset the boolean flag back to false for next time.
        mReturningWithResult = false;
    }
}

