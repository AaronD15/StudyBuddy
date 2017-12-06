package edu.usf.devices.mobile.studybuddy;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {

    TextView name, school, major, year;
    ArrayAdapter<String> arrayAdapter;
    ListView coursesView;
    FloatingActionButton inputCoursesButton;
    ArrayList<String> courses;
    Context context;
    String userID;
    DatabaseReference ref;
    String item;
    public static final int GET_FROM_GALLERY = 1;
    ImageView img;

    public FragmentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = (TextView) view.findViewById(R.id.nameView);
        school = (TextView) view.findViewById(R.id.schoolView);
        major = (TextView) view.findViewById(R.id.majorView);
        year = (TextView) view.findViewById(R.id.yearView);
        coursesView = (ListView) view.findViewById(R.id.coursesView);
        inputCoursesButton = (FloatingActionButton) view.findViewById(R.id.inputCoursesButton);
        inputCoursesButton.setOnClickListener(new inputCourseButtonListener());
        context = getContext();

        coursesView.setEmptyView(view.findViewById(R.id.emptyCourses));

        ref = FirebaseDatabase.getInstance().getReference("users");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        courses = new ArrayList<>();

        ref.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String firstname = (String) dataSnapshot.child("firstname").getValue();
                String lastname = (String) dataSnapshot.child("lastname").getValue();

                name.setText(firstname + " " + lastname);

                school.setText((String) dataSnapshot.child("school").getValue());
                major.setText((String) dataSnapshot.child("major").getValue());
                year.setText((String) dataSnapshot.child("year").getValue());

                courses.clear();

                DataSnapshot courseData = dataSnapshot.child("courses");
                for (DataSnapshot course : courseData.getChildren()){
                    courses.add(course.getValue(String.class));
                }

                arrayAdapter = new ArrayAdapter<>(context, R.layout.centered_listview, courses);
                coursesView.setAdapter(arrayAdapter);
                coursesView.setOnItemLongClickListener(new deleteLongClickListener());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        img = (ImageView) view.findViewById(R.id.ProfileImage);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Image clicked",Toast.LENGTH_SHORT).show();

                //Intent created and called to access the gallery
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                intent.setType("image/*");

                startActivityForResult(intent, GET_FROM_GALLERY );
                Toast.makeText(getContext(),"Fini",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getContext(),"Start",Toast.LENGTH_SHORT).show();

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK ) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;


            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePath,null,null,null);
            cursor.moveToFirst();
            String imgPath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(imgPath,options);
            Drawable draw = new BitmapDrawable(bitmap);

            //bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), selectedImage);
            //img.setImageBitmap(bitmap);
            img.setBackground(draw);
            Toast.makeText(getContext(),"New Profile Image Set!",Toast.LENGTH_SHORT).show();

            cursor.close();
        }

    }

    private class deleteLongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            item = (String)parent.getItemAtPosition(position);


            DatabaseReference course = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("courses");

            if(courses.contains(item)){
                courses.remove(item);
            }
            course.setValue(courses).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            });
            Toast.makeText(context, "Class removed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private class inputCourseButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            InputCourseDialog dialog = new InputCourseDialog();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            dialog.show(fm, "Fragment");
        }
    }
}
