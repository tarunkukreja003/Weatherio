package com.example.tarunkukreja.weather;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by tarunkukreja on 16/01/17.
 */

public class ForecastAdapter extends CursorAdapter {

    private Context mContext ;
    private final int VIEW_TYPE_TODAY = 0 ;
    private final int VIEW_TYPE_FUTURE = 1 ;
    private final int NO_OF_VIEW_TYPES = 2 ;
    private boolean mUseTodayLayout = true;

    public class ViewHolder {
        ImageView weatherIcon ;
        TextView dateTextView ;
        TextView descTextView ;
        TextView highText ;
        TextView lowText ;

        public ViewHolder(View view){

            weatherIcon = (ImageView)view.findViewById(R.id.list_item_icon) ;
            dateTextView = (TextView)view.findViewById(R.id.list_item_date_textview) ;
            descTextView = (TextView)view.findViewById(R.id.list_item_forecast_textview) ;
            highText = (TextView)view.findViewById(R.id.list_item_high_textview) ;
            lowText = (TextView)view.findViewById(R.id.list_item_low_textview) ;

        }
    }


    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context ;

    }

    public void setUseTodayLayout(boolean useTodayLayout){
        mUseTodayLayout = useTodayLayout ;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 && mUseTodayLayout ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE ;
    }

    @Override
    public int getViewTypeCount() {
        return NO_OF_VIEW_TYPES ; // returns the no. of viewTypes
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

//        TextView textView = (TextView)view ;
//        textView.setText(convertCursorRowToUXFormat(cursor));
         ViewHolder viewHolder = (ViewHolder) view.getTag();

       int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID) ;

    //    ImageView weatherIcon = (ImageView)view.findViewById(R.id.list_item_icon) ;

        // we need to see the viewType here since today and future viewtypes have different icons so to set them we need
        // if else condition

        int viewType = getItemViewType(cursor.getPosition());


        if(viewType == VIEW_TYPE_TODAY) {
           viewHolder.weatherIcon.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId)) ;
             }
        else if(viewType == VIEW_TYPE_FUTURE){
          viewHolder.weatherIcon.setImageResource(Utility.getIconResourceForWeatherCondition(weatherId));
           }

      //  viewHolder.weatherIcon.setImageResource(R.mipmap.ic_launcher1);

        long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE) ;
      //  TextView dateTextView = (TextView)view.findViewById(R.id.list_item_date_textview) ;
        viewHolder.dateTextView.setText(Utility.getFriendlyDayString(context, dateInMillis));

        String desc = cursor.getString(ForecastFragment.COL_WEATHER_DESC) ;

      //  TextView descTextView = (TextView)view.findViewById(R.id.list_item_forecast_textview) ;
        viewHolder.descTextView.setText(desc);
        viewHolder.weatherIcon.setContentDescription(desc); // setting the description for visually blind people or those who have low vision

        boolean isMetric = Utility.isMetric(mContext) ;

        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP) ;

     //   TextView highText = (TextView)view.findViewById(R.id.list_item_high_textview) ;
        viewHolder.highText.setText(Utility.formatTemperature(context, high, isMetric));

        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP) ;

//        TextView lowText = (TextView)view.findViewById(R.id.list_item_low_textview) ;
        viewHolder.lowText.setText(Utility.formatTemperature(context, low, isMetric));


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
              int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1 ;

        if(viewType == VIEW_TYPE_TODAY) {
            layoutId = R.layout.list_item_forecast_today ; }
        else if(viewType == VIEW_TYPE_FUTURE){
            layoutId = R.layout.list_item_forecast ; }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false) ;
        ViewHolder viewHolder = new ViewHolder(view) ;
        view.setTag(viewHolder);
        return view;
    }



//    private String formatHighLows(double high, double low) {
//        boolean isMetric = isMetric(mContext);
//        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
//        return highLowStr;
//    }
//
//    /*
//        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
//        string.
//     */
//    private String convertCursorRowToUXFormat(Cursor cursor) {
//        // get row indices for our cursor
//
//        String highAndLow = formatHighLows(
//                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
//                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));
//
//        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
//                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
//                " - " + highAndLow;
//    }

}
