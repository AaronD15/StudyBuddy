package edu.usf.devices.mobile.studybuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import static edu.usf.devices.mobile.studybuddy.R.id.profileGroups;

/**
 * Created by Mayra on 4/5/2017.
 */

public class PopRating extends Activity {

    //DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spinner spinner = (Spinner) findViewById(R.id.groups_list);
        setContentView(R.layout.popup_rating);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            String value = extras.getString("my text");
            TextView editText = (TextView)findViewById(R.id.tittle);
            editText.setText(value);
            String valuea = extras.getString("my address");
            TextView editTex = (TextView)findViewById(R.id.address);
            editTex.setText(valuea);
            //ratings
           // DatabaseReference ratingRef = mRootRef.child("places");
            RatingBar ratingBar = (RatingBar) findViewById(R.id.pop_ratingbar);
            ratingBar.setRating(4.0f);
            //Displaying groups
          //  DatabaseReference groupData = FirebaseDatabase.getInstance().getReference().child("groups");

            FloatingActionButton rate = (FloatingActionButton) findViewById(R.id.star);
            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   startActivity(new Intent(PopRating.this,Feedback.class));
                }
            });
        }

    }
}
