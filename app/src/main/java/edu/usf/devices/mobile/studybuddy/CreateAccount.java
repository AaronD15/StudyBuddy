package edu.usf.devices.mobile.studybuddy;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateAccount extends AppCompatActivity {

    private EditText emailField, passwordField, verifypassField, usernameField, fnameField, lnameField, schoolField, majorField;
    private String email, password, verifypass, username, fname, lname, school, year, major;
    private Button createButton;

    private ProgressDialog mProgressDialog;
    private Spinner spinner;

    private FirebaseAuth mAuth;
    private DatabaseReference users;

    String TAG = "CreateAccount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailField = (EditText) findViewById(R.id.create_email);
        passwordField = (EditText) findViewById(R.id.create_password);
        verifypassField = (EditText) findViewById(R.id.create_verifypass);
        usernameField = (EditText) findViewById(R.id.create_username);
        fnameField = (EditText) findViewById(R.id.create_firstname);
        lnameField = (EditText) findViewById(R.id.create_lastname);
        schoolField = (EditText) findViewById(R.id.create_school);
        majorField = (EditText) findViewById(R.id.create_major);
        createButton = (Button) findViewById(R.id.create_button);

        // Spinner set up
        spinner = (Spinner) findViewById(R.id.create_year);
        spinner.setPrompt("Select a year...");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.year_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance().getReference().child("users");



        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailField.getText().toString();
                password = passwordField.getText().toString();
                verifypass = verifypassField.getText().toString();
                username = usernameField.getText().toString();
                fname = fnameField.getText().toString();
                lname = lnameField.getText().toString();
                school = schoolField.getText().toString();
                year = spinner.getSelectedItem().toString();
                major = majorField.getText().toString();

                createAccount(email, password);
            }
        });

    }

    public void createAccount(String mEmail, String mPassword) {
        Log.d(TAG, "createAccount: " + email);
        if (!validateForm()){
            return;
        }

        showProgressDialog();

            mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Toast.makeText(CreateAccount.this, R.string.auth_failed, Toast.LENGTH_LONG).show();
                                hideProgressDialog();
                            } else {
                                try {
                                    String uid = mAuth.getCurrentUser().getUid();

                                mAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build());
                                users.child(uid).child("username").setValue(username);
                                users.child(uid).child("email").setValue(email);
                                users.child(uid).child("firstname").setValue(fname);
                                users.child(uid).child("lastname").setValue(lname);
                                users.child(uid).child("school").setValue(school);
                                users.child(uid).child("year").setValue(year);
                                users.child(uid).child("major").setValue(major);

                                hideProgressDialog();
                                sendEmailVerification();
                                finish();
                                } catch (NullPointerException e){
                                    Toast.makeText(CreateAccount.this, "Error getting User's Information", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    }

    private void sendEmailVerification() {

        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        try {
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Re-enable button

                            if (task.isSuccessful()) {
                                Toast.makeText(CreateAccount.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "sendEmailVerification", task.getException());
                                Toast.makeText(CreateAccount.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (NullPointerException e){
            Toast.makeText(CreateAccount.this, "Error checking user verification.", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean validateForm() {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        if (TextUtils.isEmpty(verifypass)) {
            verifypassField.setError("Required");
            valid = false;
        } else if (!verifypass.equals(password)) {
            passwordField.setError("Passwords do not match");
            verifypassField.setError("Passwords do not match");
            Toast.makeText(CreateAccount.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            verifypassField.setError(null);
        }

        if (TextUtils.isEmpty(username)) {
            usernameField.setError("Required.");
            valid = false;
        } else {
            usernameField.setError(null);
        }

        if (TextUtils.isEmpty(fname)) {
            fnameField.setError("Required.");
            valid = false;
        } else {
            fnameField.setError(null);
        }

        if (TextUtils.isEmpty(lname)) {
            lnameField.setError("Required.");
            valid = false;
        } else {
            lnameField.setError(null);
        }

        if(TextUtils.isEmpty(school)) {
            schoolField.setError("Required.");
            valid = false;
        } else {
            schoolField.setError(null);
        }

        if(TextUtils.isEmpty(year)) {
            TextView errorText = (TextView) spinner.getSelectedView();
            errorText.setError("");
            valid = false;
        } else {
            TextView errorText = (TextView) spinner.getSelectedView();
            errorText.setError(null);
        }

        if(TextUtils.isEmpty(major)) {
            majorField.setError("Required.");
            valid = false;
        } else {
            majorField.setError(null);
        }

        return valid;
    }

    public void showProgressDialog() {
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

}
