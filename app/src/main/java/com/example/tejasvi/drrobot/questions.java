package com.example.tejasvi.drrobot;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import org.json.simple.parser.JSONParser;

public class questions extends Fragment {

    static String database[][];
    static Map<String, String> symptom_question = new ConcurrentHashMap<>();
    static Map<String, String> disease_list = new ConcurrentHashMap<>();
    static Map<String, String> symptom_column = new ConcurrentHashMap<>();
    static Map<String, String> disease_row = new ConcurrentHashMap<>();

    static ArrayList<String> symptoms = new ArrayList<>();
    static Map<String, Integer> common_symptoms = new ConcurrentHashMap<>();
    static boolean positive = false;
    static ArrayList<String> elimination_list = new ArrayList<>();
    static Stack<String> priority_stack = new Stack<>();

    static Map<String,Integer> hits=new ConcurrentHashMap<>();
    static Map<String,Integer> total_symptoms=new ConcurrentHashMap<>();
    static Map<String,Double> hit_ratio=new ConcurrentHashMap<>();

    static Map<String, String> positive_disease_list = new ConcurrentHashMap<>();
    // TODO: set 5 questions per view. make fb session to do this
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    ProgressDialog progressDialog;
    View v;
    int question_count = 2;
    int response;

    String csv;
    String present_symptom;
    String present_question;
    int lastyes=0;
    int lastno=0;

    public questions()
    {
        symptom_question.clear();
        disease_list.clear();
        symptoms.clear();
        common_symptoms.clear();
        positive=false;
        elimination_list.clear();
        priority_stack.clear();
        hits.clear();
        total_symptoms.clear();
        hit_ratio.clear();
        lastno=0;
        lastyes=0;
    }

    void logic() {
        Log.d("baba",symptoms.toString());
        int count = 0, threshold = symptoms.size() - 2;

        for (int i = 1; i < 16; i++) {
            for (String symptom_name : symptoms) {
                if (database[i][Integer.parseInt(symptom_column.get(symptom_name))].equals("1")) {
                    count++;
                }
            }

            if (count >= threshold) {
                disease_list.put(database[i][0], "0");
            }
            count = 0;
        }

        for (int j = 1; j < 47; j++) {
            if (symptoms.contains(database[0][j])) continue;

            count = 0;

            for (String disease : disease_list.keySet()) {
                if (database[Integer.parseInt(disease_row.get(disease))][j].equals("1")) {
                    count++;
                }
            }

            if ((!symptoms.contains(database[0][j])) && (count > 1))
                common_symptoms.put(database[0][j], count);
        }

        common_symptoms = sortByValueAsc(common_symptoms);

        for (Map.Entry<String, Integer> entry : common_symptoms.entrySet())
            priority_stack.push(entry.getKey());

        count=0;

        for (String s : disease_list.keySet())
        {
            count=0;
            for (int j = 1; j < 47; j++)
            {
                if (database[Integer.parseInt(disease_row.get(s))][j].equals("1")) count++;

                total_symptoms.put(s,count);
            }
        }

        for (String s : disease_list.keySet()) {
            for (int j = 1; j < 47; j++) {
                if ((database[Integer.parseInt(disease_row.get(s))][j].equals("1")) && (symptoms.contains(database[0][j]))) {
                    int hit_count = 0;

                    if (hits.get(s) != null) hit_count = hits.get(s);

                    hit_count++;

                    hits.put(s, hit_count);

                    double ratio = (hit_count) / Double.parseDouble(total_symptoms.get(s).toString());

                    hit_ratio.put(s, ratio);
                }
            }
        }

        fire_question();
    }

    void initiate_hit(String symptom)
    {
        for (String s : disease_list.keySet()) {
            if (database[Integer.parseInt(disease_row.get(s))][Integer.parseInt(symptom_column.get(symptom))].equals("1")) {


                int hit_count=0;

                if(hits.get(s)!=null) hit_count=hits.get(s);

                hit_count++;

                hits.put(s,hit_count);

                double ratio=(hit_count)/Double.parseDouble(total_symptoms.get(s).toString());

                hit_ratio.put(s,ratio);

            }
        }
    }

    void fire_question()
    {
        Log.d("baba",priority_stack.toString()+" "+priority_stack.size());
        Log.d("baba",disease_list.toString());
        Log.d("baba","------------------------------------------------------------------------------");

        if(disease_list.isEmpty())
        {
            Toast.makeText(v.getContext(), "It looks like you have a very unnatural set of symptoms. Kindly visit a doctor", Toast.LENGTH_LONG).show();

            final FirebaseDatabase database=FirebaseDatabase.getInstance();
            final DatabaseReference myRef=database.getReference();
            final FirebaseAuth mAuth=FirebaseAuth.getInstance();

            myRef.child("Session").child(mAuth.getCurrentUser().getUid()).removeValue();

            getFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), new diagnose())
                    .disallowAddToBackStack()
                    .commit();
        }
        else if(priority_stack.isEmpty())   //for just 1 disease
        {
            Log.d("baba","BARRAMUNDI");
            final FirebaseDatabase database=FirebaseDatabase.getInstance();
            final DatabaseReference myRef=database.getReference();
            final FirebaseAuth mAuth=FirebaseAuth.getInstance();

            myRef.child("Session").child(mAuth.getCurrentUser().getUid()).removeValue();

            for(Map.Entry<String,String>map : disease_list.entrySet())
            myRef.child("Diagnosis").child(mAuth.getCurrentUser().getUid()).child(map.getKey()).setValue("1");

            getFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), new result())
                    .disallowAddToBackStack()
                    .commit();
        }

        else if(positive&&(!priority_stack.isEmpty()))
        {
            present_symptom = priority_stack.pop();
            present_question = symptom_question.get(present_symptom);
            Toast.makeText(v.getContext(), "Symptom is "+present_symptom, Toast.LENGTH_SHORT).show();

            listItems.clear();
            ListItem listItem=new ListItem(present_question.trim(),"Yes");
            listItems.add(listItem);

            progressDialog.dismiss();

            adapter = new MyAdapter(listItems, v.getContext());
            recyclerView.setAdapter(adapter);
        }
         else {
            present_symptom = priority_stack.pop();
            present_question = symptom_question.get(present_symptom);
            elimination_list.add(present_symptom);
            Toast.makeText(v.getContext(), "Symptom is "+present_symptom, Toast.LENGTH_SHORT).show();

            listItems.clear();
            ListItem listItem=new ListItem(present_question.trim(),"Yes");
            listItems.add(listItem);

            progressDialog.dismiss();
            adapter = new MyAdapter(listItems, v.getContext());
            recyclerView.setAdapter(adapter);

        }

    }

    void getResponse()
    {
        if(ListItem.getResponse().equals("Yes"))
        {
            response=1;
            lastyes=0;
            lastno++;
        }
        else
        {
            response=0;
            lastno=0;
            lastyes++;
        }
        if(lastno>15&&priority_stack.isEmpty()) {//i.e. continuous yes's
            final FirebaseDatabase database=FirebaseDatabase.getInstance();
            final DatabaseReference myRef=database.getReference();
            final FirebaseAuth mAuth=FirebaseAuth.getInstance();

            myRef.child("Diagnosis").child(mAuth.getCurrentUser().getUid()).child("Everything?? - Go see a doctor fast!").setValue("1");
            myRef.child("Session").child(mAuth.getCurrentUser().getUid()).removeValue();

            getFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), new result())
                    .disallowAddToBackStack()
                    .commit();


        }
        else if(lastyes>15&&priority_stack.isEmpty()){
            final FirebaseDatabase database=FirebaseDatabase.getInstance();
            final DatabaseReference myRef=database.getReference();
            final FirebaseAuth mAuth=FirebaseAuth.getInstance();


            myRef.child("Diagnosis").child(mAuth.getCurrentUser().getUid()).child("Nothing? - Have you missed out a symptom ?").setValue("100");
            myRef.child("Session").child(mAuth.getCurrentUser().getUid()).removeValue();
            getFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), new result())
                    .disallowAddToBackStack()
                    .commit();
            }
        }


    void initiate_strike(String symptom) {
        for (String s : disease_list.keySet()) {
            if (database[Integer.parseInt(disease_row.get(s))][Integer.parseInt(symptom_column.get(symptom))].equals("1")) {
                int strike_count = Integer.parseInt(disease_list.get(s));

                int strike_estimate=(int)Math.ceil(0.4*total_symptoms.get(database[Integer.parseInt(disease_row.get(s))][0]));
                int max_strike=(3>strike_estimate)?3:strike_estimate;

                if(symptom.equals("Confusion")||symptom.equals("Paralysis"))
                {
                    strike_count+=2;
                }
                else
                {
                    strike_count++;
                }

                if (strike_count == max_strike)
                {
                    disease_list.remove(s);
                    hit_ratio.remove(s);
                    hits.remove(s);
                    //Toast.makeText(v.getContext(), s+" has been eliminated", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(v.getContext(), disease_list.toString(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    disease_list.put(s, String.valueOf(strike_count));
                }

            }
        }
    }

    void positive_questions(){
        for (String s : disease_list.keySet()) {
            for (int j = 1; j < 47; j++) {
                if ((database[Integer.parseInt(disease_row.get(s))][j].equals("1")) && (!elimination_list.contains(database[0][j]))) {
                    elimination_list.add(database[0][j]);
                    priority_stack.push(database[0][j]);
                    if (disease_list.get(s) == null) j = 50;
                }
            }
        }

    }

    void cleanUp()
    {
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=database.getReference();
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();

        for(Map.Entry<String,Double> map : hit_ratio.entrySet())
        {
            myRef.child("Diagnosis").child(mAuth.getCurrentUser().getUid()).child(map.getKey()).setValue(map.getValue());
        }

    }

    public <K, V extends Comparable<? super V>> Map<K, V>
    sortByValueAsc(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_questions, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressDialog=new ProgressDialog(v.getContext());
        progressDialog.setMessage("Loading Questions...");
        progressDialog.show();
        recyclerView = (RecyclerView) v.findViewById(R.id.questions_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        listItems = new ArrayList<>();
        final String questions[] = getResources().getStringArray(R.array.questions);

        Button btn_next = (Button) v.findViewById(R.id.btn_questions_next);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference("Symptom-Question");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot node : dataSnapshot.getChildren()) {
                    symptom_question.put(node.getKey(), node.getValue().toString());
                }
                go();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference myRef1=db.getReference();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (priority_stack.isEmpty())
                {
                    getResponse();
                    if (response == 0) initiate_strike(present_symptom);
                    else if(response==1)initiate_hit(present_symptom);

                    if(positive)
                    {
                        progressDialog.setMessage("Getting Result...");
                        cleanUp();
                        myRef1.child("Session").child(mAuth.getCurrentUser().getUid()).removeValue();
                        getFragmentManager().beginTransaction()
                                .replace(((ViewGroup) getView().getParent()).getId(), new result())
                                .disallowAddToBackStack()
                                .commit();
                    }
                    else
                    {
                        positive = true;
                        positive_questions();
                    }
                }
                else
                {
                    progressDialog.setMessage("Loading Questions...");
                    getResponse();
                    if (response == 0) initiate_strike(present_symptom);
                    else if(response==1)initiate_hit(present_symptom);
                    fire_question();
                }
            }
        });

        return v;

    }

    void go() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("Initialise");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot node : dataSnapshot.getChildren()) {
                    csv = node.getValue().toString();
                }
                go1();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void go1()
    {
        database=new String[16][47];
        String array[] = csv.split(",");

        int row = -1, column = -1;

        for (int i = 0; i < array.length; i++) {
            if (i % 47 == 0){
                row++;
                column = 0;
            } else
                column++;

            database[row][column] = array[i];

        }

        for(int i=1;i<16;i++)
        {
            disease_row.put(database[i][0],String.valueOf(i));
        }

        for(int i=1;i<47;i++)
        {
            symptom_column.put(database[0][i],String.valueOf(i));
        }

        go2();
    }

    void go2()
    {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("Session/"+mAuth.getCurrentUser().getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot node : dataSnapshot.getChildren())
                {
                    if(node.getKey().equals("Symptoms"))
                    {
                        for(DataSnapshot node1 : node.getChildren())
                        {
                            String symptom=node1.getKey();
                            symptoms.add(symptom);
                            elimination_list.add(symptom);
                        }
                    }
                }

                logic();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
