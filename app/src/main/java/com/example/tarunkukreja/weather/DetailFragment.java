package com.example.tarunkukreja.weather;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tarunkukreja.weather.data.WeatherContract;

/**
 * Created by tarunkukreja on 19/01/17.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


        ImageView weatherDetailImage;
        TextView dateDetailText;
        TextView highDetailText;
        TextView lowDetailText;
        TextView humidDetailText;
        TextView windDetailText;
        TextView pressureDetailText;
        TextView descTextView ;
        TextView dayTextView ;





    private static final String LOG_TAG = DetailFragment.class.getSimpleName() ;

    ShareActionProvider mShareActionProvider ;

    private static final int DETAIL_LOADER = 0;
    static final String DETAIL_URI = "URI";
    private Uri mUri;

    private static final String[] DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COLUMN_WEATHER_PRESSURE = 5 ;
    private static final int COLUMN_WEATHER_HUMIDITY = 6 ;
    private static final int COLUMN_WEATHER_COND_ID = 7 ;
    private static final int COLUMN_WEATHER_WIND_SPEED = 8 ;
    private static final int COLUMN_WEATHER_DEGREES = 9 ;


    private String forecastData ;

    public DetailFragment() {

        setHasOptionsMenu(true);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {


        Log.d(LOG_TAG,"onLoadFinished() called") ;

        if(!cursor.moveToFirst()) return;

        long date = cursor.getLong(COL_WEATHER_DATE) ;
        Boolean isMetric = Utility.isMetric(getActivity()) ;
        double high = cursor.getDouble(COL_WEATHER_MAX_TEMP);
        double low = cursor.getDouble(COL_WEATHER_MIN_TEMP) ;
        String highStr = Utility.formatTemperature(getActivity(), high, isMetric) ;
        String lowStr = Utility.formatTemperature(getActivity(),low, isMetric) ;
        String desc = cursor.getString(COL_WEATHER_DESC) ;

        float pressure = cursor.getFloat(COLUMN_WEATHER_PRESSURE) ;
        float humidity = cursor.getFloat(COLUMN_WEATHER_HUMIDITY) ;


        float windSpeed = cursor.getFloat(COLUMN_WEATHER_WIND_SPEED) ;
        float windDir = cursor.getFloat(COLUMN_WEATHER_DEGREES) ;

        String wind = Utility.getFormattedWind(getActivity(), windSpeed, windDir) ;


        int weatherId = cursor.getInt(COLUMN_WEATHER_COND_ID) ;
        int weatherIcon =  Utility.getArtResourceForWeatherCondition(weatherId);
        String dayName = Utility.getDayName(getActivity(), date) ;
        String dateView = Utility.getFormattedMonthDay(getActivity(), date) ;

        highDetailText.setText(highStr);
        lowDetailText.setText(lowStr);
        descTextView.setText(desc);
        dayTextView.setText(dayName);
        dateDetailText.setText(dateView);


        pressureDetailText.setText(getActivity().getString(R.string.format_pressure, pressure));
        humidDetailText.setText(getActivity().getString(R.string.format_humidity, humidity));

        windDetailText.setText(wind);
        weatherDetailImage.setImageResource(weatherIcon);
        weatherDetailImage.setContentDescription(desc); // setting the description for visually blind people or those who have low vision

        // We still need this for the share intent
                    forecastData = String.format("%s - %s - %s/%s", dateView, desc, high, low);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        // Update the shareActionProvider if it already exists

        if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG,"onLoadReset() called") ;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG,"onCreateLoader() called") ;

//        Intent recievedIntent = getActivity().getIntent();
//
//  // We are adding recievedIntent.getData() == null since if it is a two pane UI DetailFragment is launched with MainActivty and
//        // at that stage it doe'nt contain any URI so we don't want cursor to load
//        if(recievedIntent == null || recievedIntent.getData() == null){
//            return null ;
//        }
        if(null != mUri) {
            Log.d(LOG_TAG, "Uri isn't null") ;
            return new CursorLoader(getActivity(), mUri, DETAIL_COLUMNS, null, null, null);
        }

        return null ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        getLoaderManager().initLoader(DETAIL_LOADER, null, this) ;

        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        weatherDetailImage = (ImageView) rootView.findViewById(R.id.detail_image);
        dateDetailText = (TextView) rootView.findViewById(R.id.detail_date_textView);
        dayTextView = (TextView) rootView.findViewById(R.id.detail_dayText) ;
        highDetailText = (TextView) rootView.findViewById(R.id.fragment_high_textView);
        lowDetailText = (TextView) rootView.findViewById(R.id.fragment_low_textView);
        humidDetailText = (TextView) rootView.findViewById(R.id.humidity_text);
        pressureDetailText = (TextView) rootView.findViewById(R.id.pressure_text);
        windDetailText = (TextView) rootView.findViewById(R.id.wind_text);
        descTextView = (TextView) rootView.findViewById(R.id.detail_description_text) ;

        Bundle arguments = getArguments() ;

        if(arguments != null){
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI) ;
        }




//            Intent recievedIntent = getActivity().getIntent();
//
//            if (recievedIntent != null ) {
//
//                forecastData = recievedIntent.getDataString();
//                if(null != forecastData) {
//
//
//                    fragmentTextView = (TextView) rootView.findViewById(R.id.fragment_detail_textView);
//                    fragmentTextView.setText(forecastData);
//                }
//            }


        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.d(LOG_TAG,"onCreateOptionsMenu() called") ;
        inflater.inflate(R.menu.detail_frag_menu, menu);

        MenuItem item = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);



        // if onLoadFinished happens before then we can set the shareIntent here only
        if(forecastData != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

        else {
            Log.d(LOG_TAG, "Share cancelled") ;
        }



    }

    private Intent createShareForecastIntent(){

        Intent shareIntent = new Intent(Intent.ACTION_SEND) ;
        shareIntent.setType("text/plain") ;
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET) ;
        shareIntent.putExtra(Intent.EXTRA_TEXT, forecastData + "# Sunshine App" ) ;

        return shareIntent ;


    }

    void onLocationChanged(String newLocation){

        Uri uri = mUri ;

        if(null != uri){

            long date = WeatherContract.WeatherEntry.getDateFromUri(uri) ;

            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date) ;

            mUri = updatedUri ;

            getLoaderManager().restartLoader(DETAIL_LOADER, null, this) ;
        }


    }
}

