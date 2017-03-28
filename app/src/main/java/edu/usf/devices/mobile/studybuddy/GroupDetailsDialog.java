package edu.usf.devices.mobile.studybuddy;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupDetailsDialog extends DialogFragment {

    Group group;
    TextView classView, schoolView;
    ListView membersList;
    Toolbar toolbar;
    ArrayList<String> members;

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

        toolbar = (Toolbar)view.findViewById(R.id.toolbarGroupDetails);
        toolbar.setTitle(group.title);

        schoolView = (TextView)view.findViewById(R.id.groupDetailsSchoolView);
        classView = (TextView)view.findViewById(R.id.groupDetailsClassView);

        membersList = (ListView)view.findViewById(R.id.groupDetailsMembers);
        members = new ArrayList<>();

        for(String key : group.members.keySet()){
            members.add(key);
        }

        schoolView.setText(group.school);
        classView.setText(group.course);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, members);
        membersList.setAdapter(arrayAdapter);
        membersList.setEmptyView(view.findViewById(R.id.empty));

    }
}
