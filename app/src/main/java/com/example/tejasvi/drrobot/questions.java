package com.example.tejasvi.drrobot;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class questions extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_questions, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.questions_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        listItems = new ArrayList<>();

        String questions[]=getResources().getStringArray(R.array.questions);

        for(String temp : questions)
        {
            ListItem listItem = new ListItem(temp, "Yes");
            listItems.add(listItem);
        }

        adapter = new MyAdapter(listItems, v.getContext());

        recyclerView.setAdapter(adapter);

        return v;

    }
}
