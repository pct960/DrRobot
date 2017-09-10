package com.example.tejasvi.drrobot;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    TreeMap<String,Double> map=new TreeMap<String,Double>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_result, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.result_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        listItems = new ArrayList<>();

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("Diagnose/"+mAuth.getCurrentUser().getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot node : dataSnapshot.getChildren())
                {
                    map.put(node.getKey(),Double.parseDouble(node.getValue().toString()));
                }
                go();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

    void go()
    {
        NavigableMap<String,Double> navigableMap=map.descendingMap();

        for(Map.Entry<String,Double> entry : navigableMap.entrySet())
        {
            listItems.clear();
            Result_ListItem listItem=new Result_ListItem(entry.getKey(),"Probability : "+Double.parseDouble(entry.getValue().toString()));
            listItems.add(listItem);
        }

        adapter = new Result_MyAdapter(listItems, v.getContext());
        recyclerView.setAdapter(adapter);
    }
}
