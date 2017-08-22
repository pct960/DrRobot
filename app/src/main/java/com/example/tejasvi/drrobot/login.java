package com.example.tejasvi.drrobot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog loading;
    ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mAuth = FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(login.this);
        progressDialog.setMessage("Logging in...");
        final EditText signin_email=(EditText)findViewById(R.id.signin_email);
        final EditText signin_passwd=(EditText)findViewById(R.id.signin_password);

        Button btn = (Button) findViewById(R.id.signin_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x=0;
                progressDialog.show();
                EditText usernameEditText = (EditText) findViewById(R.id.signin_password);
                String sUsername = usernameEditText.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(signin_email.getText().toString()).matches()){
                    Toast.makeText(login.this, "Please enter a Valid E-Mail Address!",
                            Toast.LENGTH_LONG).show();
                }

                else if (sUsername.matches("")) {
                    Toast.makeText(login.this, "Enter your Password", Toast.LENGTH_SHORT).show();
                }

                else {
                    x=1;
                }

                if(x==1);
                {
                    mAuth.signInWithEmailAndPassword(signin_email.getText().toString(),signin_passwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(login.this,"Incorrect email or password", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {


                                progressDialog.dismiss();
                                Intent Intent_main_menu = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(Intent_main_menu);
                            }
                        }
                    });

                }}
        });



    }
}
