package edu.usf.devices.mobile.studybuddy;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
public class TabFragment4 extends Fragment{

    Button createButton;
    DatabaseReference groupData;
    ListView profileGroups;
    ArrayList<String> groups;
    Context context;

    public TabFragment4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_tab_fragment4, container, false);
        context = getContext();

        createButton = (Button) view.findViewById(R.id.createButton);
        profileGroups = (ListView) view.findViewById(R.id.profileGroups);

        groups = new ArrayList<>();
        groupData = FirebaseDatabase.getInstance().getReference().child("groups");
        groupData.orderByChild("Creator").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                groups.clear();

                for( DataSnapshot group : dataSnapshot.getChildren() ){
                    groups.add(group.getKey());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, groups);
                profileGroups.setAdapter(arrayAdapter);
                profileGroups.setEmptyView(view.findViewById(R.id.empty));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        createButton.setOnClickListener(new createGroupListener());
        profileGroups.setOnItemLongClickListener(new groupSelection());
    }

    class createGroupListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), CreateGroup.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    class groupSelection implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), GroupDetails.class);
            String data = profileGroups.getItemAtPosition(position).toString();
            intent.putExtra("GROUP_NAME", data);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            return true;
        }
    }

}
