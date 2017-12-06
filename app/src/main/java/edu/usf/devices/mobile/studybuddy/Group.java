package edu.usf.devices.mobile.studybuddy;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;

public class Group implements Parcelable{
    public String title;
    public String school;
    public String course;
    public String creatorName;
    public String creatorUid;
    public String desc;
    public String hash;
    public String address;
    public HashMap<String, String> members;

    public Group(){}

    public Group(String title, String school, FirebaseUser creator, String course, String desc, String address){
        this.title = title;
        this.school = school;
        this.creatorName = creator.getDisplayName();
        this.creatorUid = creator.getUid();
        this.course = course;
        this.desc = desc;
        this.address = address;
        members = new HashMap<>();
    }

    public Group(Parcel in){
        this.title = in.readString();
        this.school = in.readString();
        this.course = in.readString();
        this.creatorName = in.readString();
        this.creatorUid = in.readString();
        this.desc = in.readString();
        this.hash = in.readString();
        this.address = in.readString();

        //Bundle b = in.readBundle(getClass().getClassLoader());

        /*try {
            members = (HashMap<String, String>) b.getSerializable("HASHMAP");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void push(){
        // Push the group to the groups reference
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("groups").push();
        this.hash = ref.getKey();
        ref.setValue(this);


        // Push the group's hash value to the userdata
        ref.getRoot().child("users").child(creatorUid).child("grouphash").child(hash).setValue(true);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(school);
        dest.writeString(course);
        dest.writeString(creatorName);
        dest.writeString(creatorUid);
        dest.writeString(desc);
        dest.writeString(hash);
        dest.writeString(address);

        //Bundle b = new Bundle();
        //b.putSerializable("HASHMAP", members);
        //dest.writeBundle(b);
    }

    public static final Parcelable.Creator<Group> CREATOR
            = new Parcelable.Creator<Group>() {
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };


}
