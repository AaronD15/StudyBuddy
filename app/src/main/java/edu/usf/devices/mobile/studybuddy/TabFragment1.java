package edu.usf.devices.mobile.studybuddy;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
    private String searchText, searchOption;
    private ListView listedGroups;
    private Spinner spinner;
    private ProgressDialog progressDialog;
    private ArrayList<String> groups;

    public TabFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);
        listedGroups = (ListView) view.findViewById(R.id.listedGroups);
        search = (FloatingActionButton)  view.findViewById(R.id.button);
        searchField = (EditText) view.findViewById(R.id.search_input);

        groupData = FirebaseDatabase.getInstance().getReference().child("groups");
        groups = new ArrayList<>();

        setUpProgressDialog();

        spinner = (Spinner) view.findViewById(R.id.search_spinner);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchText = searchField.getText().toString();
                searchOption = spinner.getSelectedItem().toString();

                groupData.orderByChild(searchOption).equalTo(searchText).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        groups.clear();

                        progressDialogOn(true);
                        for (DataSnapshot group : dataSnapshot.getChildren()){
                            groups.add(group.getKey());
                            Log.d("Children", group.getKey());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, groups);
                        listedGroups.setAdapter(arrayAdapter);
                        progressDialogOn(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
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

    class groupSelection implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), GroupDetails.class);
            String data = listedGroups.getItemAtPosition(position).toString();
            intent.putExtra("GROUP_NAME", data);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            return true;
        }
    }

}
