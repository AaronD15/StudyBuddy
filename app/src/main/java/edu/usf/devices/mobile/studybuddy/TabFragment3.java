package edu.usf.devices.mobile.studybuddy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment3 extends Fragment {

    TextView name, school, major, year;

    public TabFragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tab_fragment3, container, false);

        name = (TextView) view.findViewById(R.id.nameView);
        school = (TextView) view.findViewById(R.id.schoolView);
        major = (TextView) view.findViewById(R.id.majorView);
        year = (TextView) view.findViewById(R.id.yearView);

        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        users.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String firstname = (String) dataSnapshot.child("firstname").getValue();
                String lastname = (String) dataSnapshot.child("lastname").getValue();

                name.setText(firstname + " " + lastname);

                school.setText((String) dataSnapshot.child("school").getValue());
                major.setText((String) dataSnapshot.child("major").getValue());
                year.setText((String) dataSnapshot.child("year").getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }
}
