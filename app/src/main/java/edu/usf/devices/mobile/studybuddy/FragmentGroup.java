package edu.usf.devices.mobile.studybuddy;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGroup extends Fragment{

    Button createButton;
    DatabaseReference uref, gref;
    ListView profileGroups;
    ArrayList<Group> groups;
    Context context;

    public FragmentGroup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_group, container, false);
        context = getContext();

        createButton = (Button) view.findViewById(R.id.createButton);
        profileGroups = (ListView) view.findViewById(R.id.profileGroups);
        groups = new ArrayList<>();

        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = "";
            if (user != null)
                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            uref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("grouphash");
            gref = uref.getRoot().child("groups");

            // Listener on user's grouphash data
            uref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    groups.clear();

                    final GroupListAdapter groupListAdapter = new GroupListAdapter(context, groups);
                    profileGroups.setAdapter(groupListAdapter);
                    profileGroups.setEmptyView(view.findViewById(R.id.empty));

                    // Traverse through all the group hashkeys
                    for( DataSnapshot data : dataSnapshot.getChildren()){

                        // Find the corresponding group hashkeys within the "groups" reference
                        gref.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Add group to the group list
                                if(dataSnapshot.exists()) {
                                    groups.add(dataSnapshot.getValue(Group.class));
                                    groupListAdapter.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        } catch (Exception e ){
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        createButton.setOnClickListener(new createGroupListener());
        profileGroups.setOnItemClickListener(new groupSelection());
        profileGroups.setOnItemLongClickListener(new deleteGroup());
    }

    private class createGroupListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), CreateGroup.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    private class groupSelection implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            GroupDetailsDialog dialog = GroupDetailsDialog.newInstance((Group)profileGroups.getItemAtPosition(position));
            FragmentManager fm = getActivity().getSupportFragmentManager();
            dialog.show(fm, "Fragment");
        }
    }

    private class deleteGroup implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            DeleteGroupDialog dialog = DeleteGroupDialog.newInstance((Group)profileGroups.getItemAtPosition(position));
            FragmentManager fm = getActivity().getSupportFragmentManager();
            dialog.show(fm, "Fragment");
            return true;
        }
    }
}
