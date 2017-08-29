package com.example.tejasvi.drrobot;

import android.content.Context;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class questions extends Fragment {
    // TODO: set 5 questions per view. make fb session to do this
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    View v;
    int question_count=2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_questions, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        recyclerView = (RecyclerView) v.findViewById(R.id.questions_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        listItems = new ArrayList<>();
        final String questions[]=getResources().getStringArray(R.array.questions);

        Button btn_next=(Button)v.findViewById(R.id.btn_questions_next);

        for (int i=0;i<2;i++)
        {
            ListItem listItem=new ListItem(questions[i],"Yes");
            listItems.add(listItem);
        }

        adapter = new MyAdapter(listItems, v.getContext());
        recyclerView.setAdapter(adapter);



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listItems.clear();

                for(int i=question_count;i<question_count+2;i++)
                {
                    if(question_count==questions.length)break;
                    ListItem listItem = new ListItem(questions[i], "Yes");
                    listItems.add(listItem);
                }

                question_count+=2;
                adapter = new MyAdapter(listItems, v.getContext());
                recyclerView.setAdapter(adapter);
            }
        });


        return v;

    }
}
