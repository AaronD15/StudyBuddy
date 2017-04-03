package edu.usf.devices.mobile.studybuddy;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Group {
    public String title;
    public String school;
    public String course;
    public String creatorName;
    public String creatorUid;
    public String desc;
    public String hash;
    public HashMap<String, String> members;

    public Group(){}

    public Group(String title, String school, FirebaseUser creator, String course, String desc){
        this.title = title;
        this.school = school;
        this.creatorName = creator.getDisplayName();
        this.creatorUid = creator.getUid();
        this.course = course;
        this.desc = desc;
        members = new HashMap<>();
    }

    public void push(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("groups").push();
        this.hash = ref.getKey();
        ref.setValue(this);
    }
}
