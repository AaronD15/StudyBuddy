package edu.usf.devices.mobile.studybuddy;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TabFragment1 extends Fragment {

    private FloatingActionButton search;
    private DatabaseReference groupData;
    private EditText searchField;
    private Context context;
    private String searchText, searchOption;
    private ListView listedGroups;
    private Spinner spinner;
    private ProgressDialog progressDialog;
    private ArrayList<Group> groups;

    public TabFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_fragment1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        listedGroups = (ListView) view.findViewById(R.id.listedGroups);
        search = (FloatingActionButton)  view.findViewById(R.id.button);
        searchField = (EditText) view.findViewById(R.id.search_input);
        spinner = (Spinner) view.findViewById(R.id.search_spinner);

        groupData = FirebaseDatabase.getInstance().getReference().child("groups");
        groups = new ArrayList<>();

        setUpProgressDialog();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchText = searchField.getText().toString();
                searchOption = spinner.getSelectedItem().toString().toLowerCase();

                groupData.orderByChild(searchOption).equalTo(searchText).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        groups.clear();

                        Log.d("Snap", dataSnapshot.getKey());

                        progressDialogOn(true);
                        for (DataSnapshot group : dataSnapshot.getChildren()){
                            groups.add(group.getValue(Group.class));
                            Log.d("Children", group.getKey());
                        }

                        GroupListAdapter groupListAdapter = new GroupListAdapter(context, groups);
                        listedGroups.setAdapter(groupListAdapter);
                        progressDialogOn(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        listedGroups.setOnItemLongClickListener(new groupSelection());
    }

    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Searching...");
        progressDialog.setCancelable(false);
    }

    private void progressDialogOn(Boolean on){
        if(on){
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    private class groupSelection implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            GroupDetailsDialog dialog = GroupDetailsDialog.newInstance((Group)listedGroups.getItemAtPosition(position));
            FragmentManager fm = getActivity().getSupportFragmentManager();
            dialog.show(fm, "Fragment");
            return true;
        }
    }

}
