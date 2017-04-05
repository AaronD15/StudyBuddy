package edu.usf.devices.mobile.studybuddy;


import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment {

    Button button1, button2;
    CalendarView calendar;

    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };






    public TabFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //inflater.inflate(R.layout.fragment_tab_fragment2, container, false);
        View v = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);
        calendar = (CalendarView) v.findViewById(R.id.calendar1);
        button1 = (Button) v.findViewById(R.id.stduyPlaceButton);
        button2 = (Button) v.findViewById(R.id.schedulerButton);



        //Changes dates selected
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day){
                Toast.makeText(getActivity(), month+1 + "/" + day + "/" + year, Toast.LENGTH_SHORT).show();
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setData(CalendarContract.Events.CONTENT_URI);
                GregorianCalendar calDate = new GregorianCalendar(year, month, day);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calDate.getTimeInMillis());
                startActivity(calIntent);

            }
        });


        //Opens MapActivity when the places button is clicked
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);

                startActivity(intent);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calIntent = new Intent(getActivity(), ViewCalendars.class);

                startActivity(calIntent);
            }
        });

        return v;
    }



}
