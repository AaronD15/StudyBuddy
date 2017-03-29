package edu.usf.devices.mobile.studybuddy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment {

    Button button;
    CalendarView calendar;

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
        button = (Button) v.findViewById(R.id.stduyPlaceButton);


        //Changes dates selected
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day){
                Toast.makeText(getActivity(), month+1 + "/" + day + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });
        

        //Opens MapActivity when the places button is clicked
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);

                startActivity(intent);

            }
        });

        return v;
    }

}
