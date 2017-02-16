package com.example.tarunkukreja.weather;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements ForecastFragment.Callback{

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
  private final String LOG_TAG = MainActivity.class.getSimpleName() ;
 // private final String FORECASTFRAGMENT_TAG = "FFTAG"  ; // we no longer need it since we are declaring it statically
 private static final String DETAILFRAGMENT_TAG = "DFTAG"; // TAG is needed if we are calling the fragment multiple times and using it dynamically
  private String mLocation ;
  private boolean mTwoPane ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "OnCreate method called");
        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        if(findViewById(R.id.weather_detail_container) != null){

            Log.d(LOG_TAG, "detail fragment is not null !!") ;

            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.

            mTwoPane = true ;

            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            // if savedInstanceState != null then it will restore the state and we do not need to initialise it again
            // which also prevents occurence of any lags
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit() ;
            }


        }
        else {
            Log.d(LOG_TAG, "detail fragment is null !!") ;
            mTwoPane = false ;
          //  getSupportActionBar().setElevation(.5f);
        }

        ForecastFragment forecastFragment = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast) ;
        forecastFragment.setUseTodayLayout(!mTwoPane);

        /* We do not need the following code anymore since Forecast fragment is a static fragment */

        // Following is the dynamic declaration of Forecast fragment

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
//                    .commit();
//        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class) ;
            startActivity(intent);

            return true;
        }

        if(id == R.id.maps_view) {

            viewMap();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemSelected(Uri dateUri) {
        // Since forecast fragment interacts with detail fragment with the help of Main Activity so there can be 2 cases
        // for implementing the onClick

        // if a two-pane then display the get the Uri from Detail Fragment

        if(mTwoPane){
            Bundle args = new Bundle() ;
            args.putParcelable(DetailFragment.DETAIL_URI, dateUri);

            DetailFragment df = new DetailFragment() ;
            df.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, df, DETAILFRAGMENT_TAG)
                    .commit() ;


        }
        // if a one-pane then navigate to other activity
        else {
            Intent intent = new Intent(this, DetailActivty.class);
                    intent.setData(dateUri) ;
            startActivity(intent);
        }

    }



    private void viewMap() {

       String location = Utility.getPreferredLocation(this) ;
        String mapLoc = "http://maps.google.co.in/maps?q=" + location ;
        Uri loc = Uri.parse(mapLoc) ;

        Intent intent = new Intent(Intent.ACTION_VIEW) ;
        intent.setData(loc) ;


        if(intent.resolveActivity(getPackageManager()) != null) {

            startActivity(intent);
        }
        else {
            Log.d(LOG_TAG, "No Apps Installed") ;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(LOG_TAG, "OnStart() called")  ;
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "onPause() called") ;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, "OnDestroy() called")  ;

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(LOG_TAG, "OnStop() called")  ;
    }



    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "OnResume() called")  ;
        // update the location in our second pane using the fragment manager

        String loc = Utility.getPreferredLocation(this) ;

        if(loc != null && !loc.equals(mLocation)){
            // if the loccation is changed then call the Fragment to start with updated loc
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast) ;
          // if ff is not null then we can get the data for the updated loc
            if(null != ff){
                ff.onLocationChanged();
            }
            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG) ;

            if(null != df){
                df.onLocationChanged(loc);
            }
            mLocation = loc ;

        }


    }




}

    /**
     * A placeholder fragment containing a simple view.
     */
