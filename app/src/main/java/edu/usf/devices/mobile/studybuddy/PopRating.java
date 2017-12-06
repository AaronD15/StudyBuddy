package edu.usf.devices.mobile.studybuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    String grouphash;
    String address;
    FloatingActionButton addBtn;
    //DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_rating);

        addBtn = (FloatingActionButton)findViewById(R.id.AddBtn);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        Bundle extras = getIntent().getBundleExtra("BUNDLE");
        if(extras!=null) {
            String value = extras.getString("my text");
            TextView editText = (TextView) findViewById(R.id.tittle);
            editText.setText(value);
            address = extras.getString("my address");
            TextView editTex = (TextView) findViewById(R.id.address);
            editTex.setText(address);
            grouphash = extras.getString("grouphash");
            //Log.d("Group", group.title);
            //ratings
            // \DatabaseReference ratingRef = mRootRef.child("places");
            //RatingBar ratingBar = (RatingBar) findViewById(R.id.pop_ratingbar);
            //ratingBar.setRating(4.0f);
            //Displaying groups
            //  DatabaseReference groupData = FirebaseDatabase.getInstance().getReference().child("groups");


            FloatingActionButton rate = (FloatingActionButton) findViewById(R.id.star);

            if (grouphash.equals("DNE")) {
                addBtn.setVisibility(View.GONE);
            } else {

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("groups").child(grouphash).child("place").setValue(address)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(PopRating.this, "Place has been set.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }
        }

    }
}
