package edu.usf.devices.mobile.studybuddy;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateGroup extends AppCompatActivity {

    FloatingActionButton create;
    DatabaseReference groups;

    EditText titleField, schoolField, classField, descriptionField;
    String Title, School, Class, Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.groupToolbar);
        setSupportActionBar(toolbar);

        titleField = (EditText) findViewById(R.id.groupTitle);
        schoolField = (EditText) findViewById(R.id.groupSchool);
        classField = (EditText) findViewById(R.id.groupClass);
        descriptionField = (EditText) findViewById(R.id.groupDescription);
        create = (FloatingActionButton) findViewById(R.id.addGroup);

        groups = FirebaseDatabase.getInstance().getReference().child("groups");


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Title = titleField.getText().toString();
                School = schoolField.getText().toString();
                Class = classField.getText().toString();
                Description = descriptionField.getText().toString();

                DatabaseReference ref = groups.child(Title);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                ref.child("Creator").setValue(user.getUid());
                ref.child("School").setValue(School);
                ref.child("Class").setValue(Class);
                ref.child("Description").setValue(Description);

                HashMap<String, String> members = new HashMap<>();
                members.put(user.getDisplayName(), user.getUid());

                ref.child("Members").setValue(members);

                Toast.makeText(CreateGroup.this, "Group created.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
