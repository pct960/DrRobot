package com.example.tejasvi.drrobot;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class settings extends Fragment {

    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        final View v = inflater.inflate(R.layout.activity_settings, container, false);

        ListView myListView = (ListView)v.findViewById(R.id.settings_list_view);
        final ArrayList<String> settings_Items = new ArrayList<String>();
        final ArrayAdapter<String> aa;
        aa = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1,settings_Items);
        myListView.setAdapter(aa);
        settings_Items.add(0,"Change Password");
        settings_Items.add(1,"Change Email");
        settings_Items.add(2,"Logout");
        aa.notifyDataSetChanged();

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position==2)
                {
                    new AlertDialog.Builder(view.getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Logout")
                            .setMessage("Are you sure you want to logout?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    mAuth.signOut();
                                    Intent intent = new Intent(v.getContext(), welcome.class);
                                    v.getContext().startActivity(intent);

                                }

                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                else if(position==1)
                {
                    Intent i=new Intent(v.getContext(),update_email.class);
                    v.getContext().startActivity(i);
                }
                else if(position==0)
                {
                    Intent i=new Intent(v.getContext(),update_password.class);
                    v.getContext().startActivity(i);
                }
            }
        });
        return v;
    }
}
