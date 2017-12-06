package edu.usf.devices.mobile.studybuddy;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CreateGroup extends AppCompatActivity {

    FloatingActionButton create;
    DatabaseReference groups;
    FirebaseUser user;
    ArrayList<String> schoolList;
    EditText titleField, classField, descriptionField;
    AutoCompleteTextView schoolField;
    String Title, School, Class, Description;
    String Address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.groupToolbar);
        setSupportActionBar(toolbar);

        titleField = (EditText) findViewById(R.id.groupTitle);
        classField = (EditText) findViewById(R.id.groupClass);
        descriptionField = (EditText) findViewById(R.id.groupDescription);
        create = (FloatingActionButton) findViewById(R.id.addGroup);
        schoolField = (AutoCompleteTextView) findViewById(R.id.groupSchool);

        schoolList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.schools_list)));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schoolList);
        schoolField.setAdapter(arrayAdapter);

        groups = FirebaseDatabase.getInstance().getReference().child("groups");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Title = titleField.getText().toString();
                School = schoolField.getText().toString();
                Class = classField.getText().toString();
                Description = descriptionField.getText().toString();
                Address = "TBA";
                if(!validateForm())
                    Toast.makeText(CreateGroup.this, "Invalid School entry", Toast.LENGTH_SHORT).show();
                else {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    Group newGroup = new Group(Title, School, user, Class, Description, Address);
                    newGroup.push();
                    Toast.makeText(CreateGroup.this, "Group created.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public boolean validateForm(){
        if(!TextUtils.isEmpty(School)){
            if(!schoolList.contains(School))
                return false;
        }
        return true;
    }
}
