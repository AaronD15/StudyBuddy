package edu.usf.devices.mobile.studybuddy;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class ViewCalendars extends FragmentActivity {

    public final int MY_CAL_REQ=1;
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calendars);

        getDataFromCalendarTable();

    }

    public void getDataFromCalendarTable() {
        // Run query to get default calendar
        //check calendar permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }
        Cursor cur ;
        RelativeLayout layout = (RelativeLayout) (findViewById(R.id.activity_view_calendars));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        Uri l_eventUri = Uri.parse("content://calendar/events");
        cur= getContentResolver().query(Uri.parse("content://com.android.calendar/calendars"),
                new String[]{"_id","calendar_displayName"},null,null,null);



        if (cur != null && cur.getCount() > 0)
        {
            cur.moveToFirst();

            int calID = 0;

            // Get the field values
            calID = cur.getInt(0);
            Toast.makeText(getApplicationContext(), "ID: " + calID
                    , Toast.LENGTH_SHORT).show();
            String calId = "Calendar: " + calID;
            TextView tv1 = new TextView(this);
            tv1.setText(calId);
            layout.addView(tv1,params);

            String[] l_projection = new String[]{"title", "dtstart", "dtend"};
            Cursor l_managedCursor = getContentResolver().query(l_eventUri, l_projection, "calendar_id=" , null, "dtstart DESC, dtend DESC");
            StringBuilder l_displayText = new StringBuilder();

            if (l_managedCursor != null && l_managedCursor.getCount() > 0)
            {
                l_managedCursor.moveToFirst();
                int l_cnt = 0;
                String l_title;
                String l_begin;
                String l_end;

                int l_colTitle = l_managedCursor.getColumnIndex(l_projection[0]);
                int l_colBegin = l_managedCursor.getColumnIndex(l_projection[1]);
                int l_colEnd = l_managedCursor.getColumnIndex(l_projection[1]);
                do {
                    l_title = l_managedCursor.getString(l_colTitle);
                    l_begin = getDateTimeStr(l_managedCursor.getString(l_colBegin));
                    l_end = getDateTimeStr(l_managedCursor.getString(l_colEnd));
                    l_displayText.append(l_title + "\n" + l_begin + "\n" + l_end + "\n----------------\n");
                    ++l_cnt;
                } while (l_managedCursor.moveToNext() && l_cnt < 3);

                TextView tv2 = new TextView(this);
                tv2.setText(l_displayText.toString());
                layout.addView(tv2,params);
                l_managedCursor.close();

            }




            //displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);

            cur.close();




            // Do something with the values...


            /*TextView tv1 = new TextView(this);
            tv1.setText(calId);
            layout.addView(tv1);

            params.addRule(RelativeLayout.BELOW, tv1.getId());*/


        }

        /*boolean val = cur.moveToFirst();

        if(val){
            int pos = cur.getPosition();


            if(cur.isNull(0)){
                Toast.makeText(getApplicationContext(), "Null: " + cur.isNull(0)
                        , Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                int calID = 0;
                //String displayName = null;

                // Get the field values
                //calID = cur.getInt(0);
                //displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);


                cur.close();

                // Do something with the values...

                TextView tv1 = new TextView(this);
                tv1.setText(calID);
                layout.addView(tv1);

            }
        }

            //int CalID = cur.getInt(0);
        /*ContentResolver cr = getContentResolver();

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[] {"hera@example.com", "com.example",
                "hera@example.com"};
// Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }
        cur = cr.query(uri, EVENT_PROJECTION, selection, null, null);

        while (cur.moveToNext()) {

            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(0);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            cur.close();

            // Do something with the values...

            TextView tv1 = new TextView(this);
            tv1.setText(displayName);
            layout.addView(tv1);*/

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

