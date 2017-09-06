package com.example.tejasvi.drrobot;

import android.content.Context;
import android.os.IBinder;
import android.provider.ContactsContract;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    ArrayList<String> chosensymptoms=new ArrayList<String>();

    static Map<String,String> symptom_question=new ConcurrentHashMap<>();
    static Map<String,String> disease_list=new ConcurrentHashMap<>();
    static Map<String,String> symptom_column=new ConcurrentHashMap<>();
    static Map<String,String> disease_row=new ConcurrentHashMap<>();

    static ArrayList<String> symptoms=new ArrayList<>();
    static Map<String,Integer> common_symptoms =new ConcurrentHashMap<>();
    static boolean positive=false;
    static ArrayList<String> elimination_list=new ArrayList<>();
    static Stack<String> priority_stack=new Stack<>();

    static Map<String,String> positive_disease_list=new ConcurrentHashMap<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    View v;
    int question_count=2;
    void  responce()
    {

    }

    void init()throws Exception
    {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("symptom-question.json"));
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
        int row=-1,j;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        database=new String[16][47];

        br = new BufferedReader(new FileReader("myfile.csv"));

        try {

            while ((line = br.readLine()) != null) {

                String[] in = line.split(cvsSplitBy);
                j = -1;row++;int len=in.length;
                for (int k=0;k<len;k++)
                    database[row][++j] = in[k];

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for(int i=0;i<15;i++)
        {
            disease_row.put(database[i][0],String.valueOf(i));
        }

        for(int i=1;i<49;i++)
        {
            symptom_column.put(database[0][i],String.valueOf(i));
            symptom_question.put(database[0][i],jsonObject.get(database[0][i]).toString());
        }

    }

   void accept_input()
    {
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("Session/"+mAuth.getCurrentUser().getUid());


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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void logic()throws Exception
    {
        int count=0,threshold=symptoms.size()-1;

        for(int i=0;i<15;i++)
        {
            for(String symptom_name:symptoms)
            {
                if(database[i][Integer.parseInt(symptom_column.get(symptom_name))].equals("1"))
                {
                    count++;
                }
            }

            if(count>=threshold)
            {
                disease_list.put(database[i][0],"0");
            }
            count=0;
        }

        for (int j = 1; j < 49; j++)
        {
            if (symptoms.contains(database[0][j])) continue;

            count = 0;

            for (String disease : disease_list.keySet())
            {
                if (database[Integer.parseInt(disease_row.get(disease))][j].equals("1"))
                {
                    count++;
                }
            }

            if ((!symptoms.contains(database[0][j])) && (count > 1)) common_symptoms.put(database[0][j], count);
        }

        common_symptoms = sortByValueAsc(common_symptoms);

        for (Map.Entry<String, Integer> entry : common_symptoms.entrySet())
            priority_stack.push(entry.getKey());

        while (disease_list.size()>1)
        {
            fire_question();
        }

        System.out.print("It appears that you have ");

        for(String s: disease_list.keySet())
        {
            System.out.print(s);
        }

    }

     void fire_question()throws Exception
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        if(priority_stack.isEmpty())
        {
            positive=true;
            positive_questions();

        }

        else
        {
            String symptom=priority_stack.pop();
            String question=symptom_question.get(symptom);
            elimination_list.add(symptom);

            System.out.println(question);

            int response=Integer.parseInt(br.readLine());

            if(response==0)initiate_strike(symptom);

            else if (response==1)return;

        }

    }

    void initiate_strike(String symptom)
    {
        for(String s : disease_list.keySet())
        {
            if(database[Integer.parseInt(disease_row.get(s))][Integer.parseInt(symptom_column.get(symptom))].equals("1"))
            {
                int strike_count=Integer.parseInt(disease_list.get(s));

                strike_count++;

                if(strike_count==3)
                {
                    disease_list.remove(s);
                }
                else
                {
                    disease_list.put(s,String.valueOf(strike_count));
                }

            }
        }
    }

   void positive_questions()throws Exception
    {
        for(String s:disease_list.keySet())
        {
            for(int j=1;j<49;j++)
            {
                if((database[Integer.parseInt(disease_row.get(s))][j].equals("1"))&&(!elimination_list.contains(database[0][j])))
                {
                    elimination_list.add(database[0][j]);
                    priority_stack.push(database[0][j]);
                    fire_question();
                    if(disease_list.get(s)==null)j=50;
                }
            }
        }

        disease_list=sortByValueAsc(disease_list);

        int count=0;

        for(Map.Entry<String,String> entry:disease_list.entrySet())
        {
            count++;

            if(count>1)
            {
                disease_list.remove(entry.getKey());
            }
        }

    }

    public  <K, V extends Comparable<? super V>> Map<K, V>
    sortByValueAsc(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
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
    public static questions newInstance(Bundle bundle) {
        questions myFragment = new questions();

        myFragment.setArguments(bundle);

        return myFragment
                ;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.activity_questions, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        recyclerView = (RecyclerView) v.findViewById(R.id.questions_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        listItems = new ArrayList<>();
        final String questions[]=getResources().getStringArray(R.array.questions);

        Bundle arguments = getArguments();
        if(arguments != null)
        {
            //hala
        }
        String[] lst=arguments.getStringArray("symptoms");
        Toast.makeText(v.getContext(), lst.toString(), Toast.LENGTH_SHORT).show();
        Button btn_next=(Button)v.findViewById(R.id.btn_questions_next);

       try
       {
           init();
           accept_input();
           logic();
       }
       catch (Exception e)
       {

       }

       for(String s : symptoms)
       {
           Toast.makeText(v.getContext(), s, Toast.LENGTH_SHORT).show();
       }


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
