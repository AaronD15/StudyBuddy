package edu.usf.devices.mobile.studybuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateAccount extends AppCompatActivity {

    private EditText emailField, passwordField, usernameField, fnameField, lnameField;
    private String email, password, username, fname, lname;
    private Button createButton;

    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference users;

    String TAG = "CreateAccount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailField = (EditText) findViewById(R.id.create_email);
        passwordField = (EditText) findViewById(R.id.create_password);
        usernameField = (EditText) findViewById(R.id.create_username);
        fnameField = (EditText) findViewById(R.id.create_firstname);
        lnameField = (EditText) findViewById(R.id.create_lastname);
        createButton = (Button) findViewById(R.id.create_button);

        mAuth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance().getReference().child("users");



        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailField.getText().toString();
                password = passwordField.getText().toString();
                username = usernameField.getText().toString();
                fname = fnameField.getText().toString();
                lname = lnameField.getText().toString();

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

                        if (!task.isSuccessful()){
                            Toast.makeText(CreateAccount.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        }

                        users.child(username).child("email").setValue(email);
                        users.child(username).child("password").setValue(password);
                        users.child(username).child("firstname").setValue(fname);
                        users.child(username).child("lastname").setValue(lname);

                        hideProgressDialog();
                        sendEmailVerification();
                        finish();
                    }
                });

    }

    private void sendEmailVerification() {

        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Re-enable button

                        if (task.isSuccessful()){
                            Toast.makeText(CreateAccount.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(CreateAccount.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String username = usernameField.getText().toString();
        String fname = fnameField.getText().toString();
        String lname = lnameField.getText().toString();

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
