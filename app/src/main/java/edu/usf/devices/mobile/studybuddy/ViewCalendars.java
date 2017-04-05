package edu.usf.devices.mobile.studybuddy;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class ViewCalendars extends FragmentActivity {

    public final int MY_CAL_REQ = 1;
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calendars);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }

        getCalendars();

    }

    class MyCalendar {
        public String name;
        public String id;

        public MyCalendar(String _name, String _id) {
            name = _name;
            id = _id;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private MyCalendar m_calendars[];
    private String m_selectedCalendarId = "0";
    StringBuilder l_displayText = new StringBuilder();


    private void getCalendars() {

        String[] l_projection = new String[]{"_id", "calendar_displayName"};
        Uri l_calendars;
        if (Build.VERSION.SDK_INT >= 8) {
            l_calendars = Uri.parse("content://com.android.calendar/calendars");
        } else {
            l_calendars = Uri.parse("content://calendar/calendars");
        }
        Cursor l_managedCursor = this.getContentResolver().query(l_calendars, l_projection, null, null, null);    //all calendars
        //Cursor l_managedCursor = this.managedQuery(l_calendars, l_projection, "selected=1", null, null);   //active calendars
        if (l_managedCursor != null) {
            if (l_managedCursor.moveToFirst()) {
                m_calendars = new MyCalendar[l_managedCursor.getCount()];
                String l_calName;
                String l_calId;
                int l_cnt = 0;
                int l_nameCol = l_managedCursor.getColumnIndex(l_projection[1]);
                int l_idCol = l_managedCursor.getColumnIndex(l_projection[0]);
                do {
                    l_calName = l_managedCursor.getString(l_nameCol);
                    l_calId = l_managedCursor.getString(l_idCol);
                    m_calendars[l_cnt] = new MyCalendar(l_calName, l_calId);
                    ++l_cnt;
                    m_selectedCalendarId = l_calId;
                    getEvents();
                } while (l_managedCursor.moveToNext());
                RelativeLayout layout = (RelativeLayout) (findViewById(R.id.activity_view_calendars));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

               /* TextView tv1 = new TextView(this);
                tv1.setText(l_calName);
                layout.addView(tv1, params);*/


            }
            l_managedCursor.close();
        }

    }

    private void getEvents() {
        Uri l_eventUri;
        if (Build.VERSION.SDK_INT >= 8) {
            l_eventUri = Uri.parse("content://com.android.calendar/events");
        } else {
            l_eventUri = Uri.parse("content://calendar/events");
        }
        String[] l_projection = new String[]{"title", "dtstart", "dtend"};
        Cursor l_managedCursor = this.getContentResolver().query(l_eventUri, l_projection, "_id=" + m_selectedCalendarId, null, "dtstart DESC, dtend DESC");
        //Cursor l_managedCursor = this.managedQuery(l_eventUri, l_projection, null, null, null);
        if (l_managedCursor != null) {

            if (l_managedCursor.moveToFirst()) {

                int l_cnt = 0;
                String l_title;
                String l_begin;
                String l_end;
                //StringBuilder l_displayText = new StringBuilder();
                int l_colTitle = l_managedCursor.getColumnIndex(l_projection[0]);
                int l_colBegin = l_managedCursor.getColumnIndex(l_projection[1]);
                int l_colEnd = l_managedCursor.getColumnIndex(l_projection[2]);
                do {
                    l_title = l_managedCursor.getString(l_colTitle);
                    l_begin = getDateTimeStr(l_managedCursor.getString(l_colBegin));
                    l_end = getDateTimeStr(l_managedCursor.getString(l_colEnd));
                    l_displayText.append(l_title + ": " + l_begin + " to " + l_end + "\n");
                    ++l_cnt;
                } while (l_managedCursor.moveToNext() && l_cnt < 5);

                RelativeLayout layout = (RelativeLayout) (findViewById(R.id.activity_view_calendars));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                TextView tv1 = new TextView(this);
                tv1.setText(l_displayText);
                layout.addView(tv1, params);
                //  m_text_event.setText(l_displayText.toString());
            }
            l_managedCursor.close();
        } else {

        }
    }

    private static final String DATE_TIME_FORMAT = "yyyy MMM dd, HH:mm:ss";

    public static String getDateTimeStr(int p_delay_min) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        if (p_delay_min == 0) {
            return sdf.format(cal.getTime());
        } else {
            Date l_time = cal.getTime();
            //l_time.setTime(l_time+p_delay_min);
            //l_time.setMinutes(l_time.getMinutes() + p_delay_min);
            return sdf.format(l_time);
        }
    }

    public static String getDateTimeStr(String p_time_in_millis) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date l_time = new Date(Long.parseLong(p_time_in_millis.trim()));
        return sdf.format(l_time);
    }

}

