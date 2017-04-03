package edu.usf.devices.mobile.studybuddy;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment3 extends Fragment {

    TextView name, school, major, year;
    ArrayAdapter<String> arrayAdapter;
    ListView coursesView;
    FloatingActionButton inputCoursesButton;
    ArrayList<String> courses;
    Context context;
    String userID;
    DatabaseReference ref;
    String item;

    public TabFragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_tab_fragment3, container, false);

        name = (TextView) view.findViewById(R.id.nameView);
        school = (TextView) view.findViewById(R.id.schoolView);
        major = (TextView) view.findViewById(R.id.majorView);
        year = (TextView) view.findViewById(R.id.yearView);
        coursesView = (ListView) view.findViewById(R.id.coursesView);
        inputCoursesButton = (FloatingActionButton) view.findViewById(R.id.inputCoursesButton);
        inputCoursesButton.setOnClickListener(new inputCourseButtonListener());
        context = getContext();

        coursesView.setEmptyView(view.findViewById(R.id.emptyCourses));

        ref = FirebaseDatabase.getInstance().getReference("users");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        courses = new ArrayList<>();

        ref.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String firstname = (String) dataSnapshot.child("firstname").getValue();
                String lastname = (String) dataSnapshot.child("lastname").getValue();

                name.setText(firstname + " " + lastname);

                school.setText((String) dataSnapshot.child("school").getValue());
                major.setText((String) dataSnapshot.child("major").getValue());
                year.setText((String) dataSnapshot.child("year").getValue());

                courses.clear();

                DataSnapshot courseData = dataSnapshot.child("courses");
                for (DataSnapshot course : courseData.getChildren()){
                    courses.add(course.getValue(String.class));
                }

                arrayAdapter = new ArrayAdapter<>(context, R.layout.centered_listview, courses);
                coursesView.setAdapter(arrayAdapter);
                coursesView.setOnItemLongClickListener(new deleteLongClickListener());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return view;
    }

    private class deleteLongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            item = (String)parent.getItemAtPosition(position);


            DatabaseReference course = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("courses");

            if(courses.contains(item)){
                courses.remove(item);
            }
            course.setValue(courses).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            });
            Toast.makeText(context, "Class removed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private class inputCourseButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            InputCourseDialog dialog = new InputCourseDialog();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            dialog.show(fm, "Fragment");
        }
    }
}
