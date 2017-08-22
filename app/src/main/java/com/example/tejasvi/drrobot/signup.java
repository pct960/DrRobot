package com.example.tejasvi.drrobot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class signup extends AppCompatActivity {

    private String android_id;
    private FirebaseAuth mAuth;

    String location;
    String latlng;

    private ProgressDialog loading;
    ProgressDialog progressDialog;

    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing up...");


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                location = place.getName().toString();
                String temp = place.getLatLng().toString();
                latlng = temp.substring(10, temp.length() - 1);


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Toast.makeText(signup.this, status.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(12.737055, 77.558144),
                new LatLng(13.168002, 77.593849)));


        Button btn = (Button) findViewById(R.id.submit_main_user_info);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                EditText usernameEditText = (EditText) findViewById(R.id.uitxt_name);
                String sUsername = usernameEditText.getText().toString();

                EditText usermobileEditText = (EditText) findViewById(R.id.uitxt_mob);
                String smobile = usermobileEditText.getText().toString();


                EditText passwd1 = (EditText) findViewById(R.id.uitxt_pass1);
                String pass1 = passwd1.getText().toString();

                EditText passwd2 = (EditText) findViewById(R.id.uitxt_pass2);
                String pass2 = passwd2.getText().toString();


                final EditText email2 = (EditText) findViewById(R.id.uitxt_email);


                if (sUsername.matches("")) {
                    Toast.makeText(signup.this, "Enter a user name", Toast.LENGTH_SHORT).show();
                } else if (smobile.matches("") /*|| smobile.length() != 10*/) {
                    Toast.makeText(signup.this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email2.getText().toString()).matches()) {
                    Toast.makeText(signup.this, "Please enter a Valid E-Mail Address!",
                            Toast.LENGTH_LONG).show();
                } else if (!pass1.equals(pass2)) {
                    Toast.makeText(signup.this, "Passwords don't match",
                            Toast.LENGTH_LONG).show();
                } else {
                    x = 1;
                }

                if (x == 1) {
                    startsignup();
                }
            }

        });
    }

    private void startsignup() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final EditText name = (EditText) findViewById(R.id.uitxt_name);
        final EditText mob = (EditText) findViewById(R.id.uitxt_mob);
        final EditText email = (EditText) findViewById(R.id.uitxt_email);
        final EditText passwd = (EditText) findViewById(R.id.uitxt_pass2);
        final Spinner gender = (Spinner) findViewById(R.id.gender);


        mAuth.createUserWithEmailAndPassword(email.getText().toString(), passwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(signup.this, "Error signing up", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Name").setValue(name.getText().toString());

                    myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Mobile").setValue(mob.getText().toString());

                    myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Email").setValue(email.getText().toString());

                    myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Gender").setValue(gender.getSelectedItem().toString());

                    myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Address").setValue(location);

                }
            }

        });

        progressDialog.dismiss();
        Intent Intent_main_menu = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(Intent_main_menu);
    }
}