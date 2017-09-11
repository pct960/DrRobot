package com.example.tejasvi.drrobot;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class result extends Fragment
{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Result_ListItem> listItems;
    View v;
    TreeMap<Double,String> map=new TreeMap<Double, String>(Collections.<Double>reverseOrder());

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.activity_result, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.result_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        listItems = new ArrayList<>();

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("Diagnosis/"+mAuth.getCurrentUser().getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot node : dataSnapshot.getChildren())
                {
                    map.put(Double.parseDouble(node.getValue().toString()),node.getKey());
                }
                go();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button btn=(Button)v.findViewById(R.id.btn_result_ok);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction()
                        .replace(((ViewGroup) getView().getParent()).getId(), new diagnose())
                        .disallowAddToBackStack()
                        .commit();
            }
        });

        //HANDLES KEYBACK
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK){

                    getFragmentManager().beginTransaction()
                            .replace(((ViewGroup) getView().getParent()).getId(), new diagnose())
                            .disallowAddToBackStack()
                            .commit();

                    return true;

                }

                return false;
            }
        });


        return v;
    }

    void go()
    {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();

        //NavigableMap<String,Double> navigableMap=map.descendingMap();

        for(Map.Entry<Double,String> entry : map.entrySet())
        {
            double prob=Double.parseDouble(entry.getKey().toString())*100;
            int probability=(int)Math.round(prob);
            Result_ListItem listItem=new Result_ListItem(entry.getValue().trim(),"Probability : "+probability+"%");
            listItems.add(listItem);
        }

        adapter = new Result_MyAdapter(listItems, v.getContext());
        recyclerView.setAdapter(adapter);

        myRef.child("Diagnosis").child(mAuth.getCurrentUser().getUid()).removeValue();
    }
}
