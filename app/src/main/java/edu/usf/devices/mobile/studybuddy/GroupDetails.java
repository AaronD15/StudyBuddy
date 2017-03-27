package edu.usf.devices.mobile.studybuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupDetails extends AppCompatActivity {

    String Title;
    Toolbar toolbar;
    TextView classView, schoolView;
    DatabaseReference ref;
    ListView membersList;
    ArrayList<String> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            Title = extras.getString("GROUP_NAME");
        }

        schoolView = (TextView)findViewById(R.id.groupDetailsSchoolView);
        classView = (TextView) findViewById(R.id.groupDetailsClassView);
        toolbar = (Toolbar) findViewById(R.id.toolbarGroupDetails);
        toolbar.setTitle(Title);
        setSupportActionBar(toolbar);

        membersList = (ListView) findViewById(R.id.groupDetailsMembers);
        members = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference().child("groups").child(Title);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                schoolView.setText(dataSnapshot.child("School").getValue().toString());
                classView.setText(dataSnapshot.child("Class").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child("groups").child(Title).child("Members");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot member : dataSnapshot.getChildren()){
                    members.add(member.getKey());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(GroupDetails.this, android.R.layout.simple_list_item_1, members);
                membersList.setAdapter(arrayAdapter);
                membersList.setEmptyView(findViewById(R.id.empty));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
