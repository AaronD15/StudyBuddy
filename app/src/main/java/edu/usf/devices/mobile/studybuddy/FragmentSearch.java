package edu.usf.devices.mobile.studybuddy;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentSearch extends Fragment {

    private SearchView searchBar;
    private Context context;
    private ListView listedGroups;
    private Spinner spinner;
    private ProgressDialog progressDialog;
    private ArrayList<Group> groups;
    GroupListAdapter groupListAdapter;

    public FragmentSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        searchBar = (SearchView)view.findViewById(R.id.searchBar);

        listedGroups = (ListView) view.findViewById(R.id.listedGroups);
        spinner = (Spinner) view.findViewById(R.id.search_spinner);

        DatabaseReference groupData = FirebaseDatabase.getInstance().getReference().child("groups");
        groups = new ArrayList<>();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchBar.setQueryHint(spinner.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        groupData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                groups.clear();

                Log.d("Snap", dataSnapshot.getKey());

                for (DataSnapshot group : dataSnapshot.getChildren()){
                    groups.add(group.getValue(Group.class));
                    Log.d("Children", group.getKey());
                }

                groupListAdapter = new GroupListAdapter(context, groups);
                listedGroups.setAdapter(groupListAdapter);
                searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(spinner.getSelectedItem().toString().equals("Class")){
                            groupListAdapter.filterByClass(newText);
                        } else if (spinner.getSelectedItem().toString().equals("School")){
                            groupListAdapter.filterBySchool(newText);
                        } else if (spinner.getSelectedItem().toString().equals("Title")){

                        } else if (spinner.getAdapter().toString().equals("Creator")){

                        }
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        listedGroups.setOnItemClickListener(new groupSelection());
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

    private class groupSelection implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            GroupDetailsDialog dialog = GroupDetailsDialog.newInstance((Group)listedGroups.getItemAtPosition(position));
            FragmentManager fm = getActivity().getSupportFragmentManager();
            dialog.show(fm, "Fragment");
        }
    }

}
