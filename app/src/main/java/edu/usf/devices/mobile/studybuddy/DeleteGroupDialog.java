package edu.usf.devices.mobile.studybuddy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class DeleteGroupDialog extends DialogFragment {

    Group group;
    Context context;
    Button delete, cancel;
    DatabaseReference ref;

    public DeleteGroupDialog() {
        // Required empty public constructor
    }

    public static DeleteGroupDialog newInstance(Group group) {
        DeleteGroupDialog frag = new DeleteGroupDialog();
        frag.setGroup(group);
        return frag;
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_group_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        delete = (Button)view.findViewById(R.id.deleteButton);
        cancel = (Button)view.findViewById(R.id.cancelButton);

        deleteClickListener click = new deleteClickListener();

        delete.setOnClickListener(click);
        cancel.setOnClickListener(click);
        ref = FirebaseDatabase.getInstance().getReference().child("groups").child(group.hash);
    }

    public void setGroup(Group group){
        this.group = group;
    }

    private class deleteClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.cancelButton:
                    getDialog().dismiss();
                    Log.d("HEY", "dismiss");
                    break;
                case R.id.deleteButton:
                    ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                // Delete group from creator's data
                                ref.getRoot().child("users").child(group.creatorUid).child("grouphash").child(group.hash).removeValue();

                                // Delete group from member's data
                                if (group.members != null && !group.members.isEmpty()){
                                    for (String member : group.members.values()) {
                                        ref = ref.getRoot().child("users").child(member).child("grouphash").child(group.hash);
                                        if (!ref.getKey().isEmpty())
                                            ref.removeValue();
                                    }
                                }
                                Toast.makeText(context, "Group successfully deleted.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed to delete group.", Toast.LENGTH_SHORT).show();
                            }
                            getDialog().dismiss();
                        }
                    });
                    Log.d("HEY", "delete");
                    break;
            }
        }
    }
}
