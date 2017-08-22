package com.example.tejasvi.drrobot;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class update_email extends AppCompatActivity {

    FirebaseAuth mAuth;
    private String m_Text = "";
    ProgressDialog progressDialog;
    boolean flag = false;
    EditText email;
    FirebaseUser user;
    AuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Email...");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        email = (EditText) findViewById(R.id.upd_email);
        flag = false;
        Button btn = (Button) findViewById(R.id.update_email_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (email.getText().toString().equals("") == false) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("You will need to sign in again to change your Email-ID");
                    final EditText input = new EditText(v.getContext());
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.show();
                            m_Text = input.getText().toString();
                            go();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(update_email.this, "Enter an Email-ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void go4() {
        progressDialog.dismiss();
        Toast.makeText(this, "Email Updated", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Update Email-ID")
                .setMessage("Email-ID has been successfully updated!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                })
                .show();


    }

    void go1() {
        mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(Update_Email.this, "Reauthenticated", Toast.LENGTH_SHORT).show();
                go2();
            }
        });
    }

    void go2() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Email").setValue(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(Update_Email.this, "Changed in database", Toast.LENGTH_SHORT).show();
                go3();
            }
        });


    }

    void go3() {
        user.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(Update_Email.this, "Email Updated", Toast.LENGTH_SHORT).show();
                go4();
            }
        });
    }

    void go() {
        credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), m_Text);
        //Toast.makeText(this, "Credentials received", Toast.LENGTH_SHORT).show();
        go1();
    }
}
