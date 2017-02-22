package com.example.tarunkukreja.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tarunkukreja.weather.data.WeatherContract;
import com.example.tarunkukreja.weather.services.WeatherService;

/**
 * Created by tarunkukreja on 11/10/16.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG = ForecastFragment.class.getSimpleName() ;
    private static final int LOADER_ID = 0 ;
    private ListView mListView ;
    private int mPos  = ListView.INVALID_POSITION ;
    private final String SELECTED_KEY = "selected position" ;
    private ForecastAdapter mForecastAdapter ;
    private boolean mUseTodayLayout ;

    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;

    /**
     +     * A callback interface that all activities containing this fragment must
     +     * implement. This mechanism allows activities to be notified of item
     +     * selections.
     +     */
    public interface Callback {
        /**
         +         * DetailFragmentCallback for when an item has been selected.
         +         */

        public void OnItemSelected(Uri dateUri) ;
    }

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setHasOptionsMenu(true);// for the Fragment to know that it has menu options

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecast_fragment_menu, menu);

    }

//    @Override
//    public void onStart() {
//        super.onStart();   // for now we do not need to download data everytime
//
//        updateWeather();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {


            updateWeather();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    //UI gets initialised
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        //We need to return the View object if fragment provides a UI which is the root of the fragment's layout

        // Since we need to populate the mListView from the Database if not connected to Network then we need to fetch
        // data from the database



        mForecastAdapter = new ForecastAdapter(getActivity(), null, 0) ;
        //We need to return the View object if fragment provides a UI which is the root of the fragment's layout


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        mListView.setAdapter(mForecastAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d(LOG_TAG, "Item is clicked") ;
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position) ;

                if(cursor != null){
                    String locSetting = Utility.getPreferredLocation(getActivity()) ;

//                    Intent intent = new Intent(getActivity(), DetailActivty.class)
//                            .setData(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(locSetting, cursor.getLong(COL_WEATHER_DATE))) ;
//
//                    startActivity(intent);

                    ((Callback) getActivity())
                            .OnItemSelected(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                                    locSetting, cursor.getLong(COL_WEATHER_DATE)
                            ));
                }

                mPos = position ;


            }
        });

        // reading from the saved state
        if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)){
            savedInstanceState.getInt(SELECTED_KEY) ;
        }
        mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
        return rootView; //We can return null if the fragment does'nt provide a UI
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        getLoaderManager().initLoader(LOADER_ID, null, this) ; // initialising the loader
        super.onActivityCreated(savedInstanceState);
    }



    // since we read the location when we create the loader, all we need to do is restart things

    void onLocationChanged(){
        updateWeather();
        getLoaderManager().restartLoader(LOADER_ID, null, this) ;


    }

    private void updateWeather(){
//        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(getActivity()) ;
//
//        String location = Utility.getPreferredLocation(getActivity());
//
//        fetchWeatherTask.execute(location) ;

//        Intent serviceIntent = new Intent(getActivity(), WeatherService.class) ;
//        serviceIntent.putExtra(WeatherService.LOCATION_QUERY_EXTRA,
//                Utility.getPreferredLocation(getActivity()));
//        getActivity().startService(serviceIntent) ;

        Intent alarmIntent = new Intent(getActivity(), WeatherService.AlarmReciever.class) ;
        alarmIntent.putExtra(WeatherService.LOCATION_QUERY_EXTRA,
                Utility.getPreferredLocation(getActivity()));
        // we use a pending intent with Alarm Manager to send intents at specific time
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, PendingIntent.FLAG_ONE_SHOT) ;
        // we have used FLAG_ONE_SHOT since we need to trigger the intent only once

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE) ;
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent);
        // RTC_WAKEUP triggers the Alarm Reciever and wakes up the device to fire pending intent at specified time

    }


   // storing the state in Bundle
    @Override
    public void onSaveInstanceState(Bundle outState) {
        /* +        // When tablets rotate, the currently selected list item needs to be saved.
+        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
+        // so check for that before storing.*/


        if(mPos != ListView.INVALID_POSITION){
            outState.putInt(SELECTED_KEY, mPos);
        }

        super.onSaveInstanceState(outState);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // In onCreateLoader CursorLoader is returned for that we need to transfer the code from onCreateView to this callback
        // Since we need to populate the listView from the Database if not connected to Network then we need to fetch
        // data from the database


/* +        // This is called when a new Loader needs to be created.  This
+        // fragment only uses one loader, so we don't care about checking the id.
+
+        // To only show current and future dates, filter the query to return weather only for
+        // dates after or including today.*/

        // Sort Order - Asc by Date

        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC" ;

        String locSettings = Utility.getPreferredLocation(getActivity()) ;

        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locSettings, System.currentTimeMillis());

        // getting the Cursor returned as a result of query() and populating the forecastAdapter with its values

        //  Cursor cursor = getActivity().getContentResolver().query(weatherForLocationUri, null, null, null, sortOrder) ;

        // Parameters of CursorLoader :
        /*
        CursorLoader (Context context,
                Uri uri,
                String[] projection, // projections are that is used after select statement while querying the database
                String selection,
                String[] selectionArgs,
                String sortOrder)

         */


        return new CursorLoader(getActivity(), weatherForLocationUri, FORECAST_COLUMNS , null, null, sortOrder) ;

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mForecastAdapter.swapCursor(null) ; // here we want to release all the resources which we are using that's why we use null
    }

    public void setUseTodayLayout(boolean useTodayLayout){
        mUseTodayLayout = useTodayLayout ;

        if(mForecastAdapter != null) {
            mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        mForecastAdapter.swapCursor(cursor) ; // after the loader is finished we swap the old cursor with the new one
        // we always call the swapCursor() on our adapter

        if(mPos != ListView.INVALID_POSITION){

          /*  +            // If we don't need to restart the loader, and there's a desired position to restore
                    +            // to, do so now.
**/
            mListView.smoothScrollToPosition(mPos);
        }
    }












}
