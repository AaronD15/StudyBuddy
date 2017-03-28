package edu.usf.devices.mobile.studybuddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class InputCourseDialog extends DialogFragment {

    EditText courseName, courseProf, courseSchool, courseNum;
    Toolbar toolbar;
    Button inputCourse, cancel;
    DatabaseReference ref;

    public InputCourseDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_course_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Assigning all views
        courseName = (EditText)view.findViewById(R.id.inputCourseName);
        courseProf = (EditText)view.findViewById(R.id.inputCourseProf);
        courseSchool = (EditText)view.findViewById(R.id.inputCourseSchool);
        courseNum = (EditText)view.findViewById(R.id.inputCourseNum);
        inputCourse = (Button)view.findViewById(R.id.inputCourseBtn);
        cancel = (Button)view.findViewById(R.id.inputCourseCancel);
        toolbar = (Toolbar)view.findViewById(R.id.inputCourseToolbar);

        toolbar.setTitle("Add Course");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("courses");

        inputCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> course = new HashMap<>();
                course.put(courseName.getText().toString(), courseProf.getText().toString());
                ref.updateChildren(course);
                getDialog().dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

    }

}
