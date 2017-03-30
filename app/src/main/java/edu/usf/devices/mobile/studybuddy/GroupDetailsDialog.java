package edu.usf.devices.mobile.studybuddy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;

public class GroupDetailsDialog extends DialogFragment {

    Context context;
    Group group;
    TextView classView, schoolView;
    ListView membersList;
    Toolbar toolbar;
    HashMap<String, Object> userJoin;
    DatabaseReference ref;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> members;
    Drawable icon;
    ImageButton iconView;

    public static GroupDetailsDialog newInstance(Group group) {
        GroupDetailsDialog frag = new GroupDetailsDialog();
        frag.setGroup(group);
        return frag;
    }

    public void setGroup(Group group){
        this.group = group;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_group_details_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        toolbar = (Toolbar)view.findViewById(R.id.toolbarGroupDetails);
        toolbar.setTitle(group.title);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        schoolView = (TextView)view.findViewById(R.id.groupDetailsSchoolView);
        classView = (TextView)view.findViewById(R.id.groupDetailsClassView);

        membersList = (ListView)view.findViewById(R.id.groupDetailsMembers);
        members = new ArrayList<>();


        members.addAll(group.members.keySet());


        schoolView.setText(group.school);
        classView.setText(group.course);

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, members);
        membersList.setAdapter(arrayAdapter);
        membersList.setEmptyView(view.findViewById(R.id.empty));

        if(user != null){
            // Toolbar button setup
            if(!group.creatorUid.equals(user.getUid())){

                iconView = new ImageButton(context);
                iconView.setBackground(null);

                if(members.contains(user.getDisplayName())){
                    buttonState(2);
                } else {
                    buttonState(1);
                }
                toolbar.addView(iconView, new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, GravityCompat.END));
            }
        }

    }

    public void joinGroup(){
        ref = FirebaseDatabase.getInstance().getReference().child("groups").child(group.hash).child("members");

        ref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    userJoin = new HashMap<>();
                    if (user != null) {
                        userJoin.put(user.getDisplayName(), user.getUid());
                        ref.updateChildren(userJoin);
                        if(!members.contains(user.getDisplayName())) {
                            members.add(user.getDisplayName());
                            arrayAdapter.notifyDataSetChanged();
                        }
                        buttonState(2);
                        Toast.makeText(context, "You have joined " + group.title, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Could not join group.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void leaveGroup(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if( user != null && user.getDisplayName() != null ) {
            ref = FirebaseDatabase.getInstance().getReference().child("groups").child(group.hash).child("members").child(user.getDisplayName());
            ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        Toast.makeText(context, "You have left " + group.title, Toast.LENGTH_SHORT).show();

                        if(members.contains(user.getDisplayName())){
                            members.remove(user.getDisplayName());
                            arrayAdapter.notifyDataSetChanged();
                        }
                        buttonState(1);
                    }
                }
            });
        }
    }

    public void buttonState (int state) {
        switch(state){
            case 0:

                break;
            case 1:
                icon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_button, null);
                iconView.setImageDrawable(icon);
                iconView.setOnClickListener(new joinGroupButtonListener());
                break;
            case 2:
                icon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_minus_button, null);
                iconView.setImageDrawable(icon);
                iconView.setOnClickListener(new leaveGroupButtonListener());
                break;
        }
    }

    class joinGroupButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            joinGroup();
        }
    }

    class leaveGroupButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            leaveGroup();
        }
    }
}
