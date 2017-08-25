package com.example.tejasvi.drrobot;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.StringTokenizer;

// TODO: getting symptoms dynamically, by iterating thru controls
public class diagnose extends Fragment {

    AutoCompleteTextView initsymptom;
    ArrayList<AutoCompleteTextView> chosensymptoms=new ArrayList<>();
    String symptom_list[];

    int count;
    int margin;

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_diagnose, container, false);
        count=1;
        margin=25;
        initsymptom=(AutoCompleteTextView)v.findViewById(R.id.symptoms1);
        chosensymptoms.add(initsymptom);
        symptom_list=getResources().getStringArray(R.array.symptom_list);
        ArrayAdapter list =new ArrayAdapter(v.getContext(),android.R.layout.select_dialog_item,symptom_list);
        initsymptom.setThreshold(1);
        initsymptom.setAdapter(list);
        Button btn_symptom=(Button)v.findViewById(R.id.add_symptom);
        btn_symptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_symptom_box();
            }
        });
        return v;
    }

    void add_symptom_box()
    {
        count++;

        if(count>=5)
        {
            Toast.makeText(v.getContext(), "You can only add a maximum of five symptoms at a time ", Toast.LENGTH_SHORT).show();
        }

        else
        {
            Button btn_symptom=(Button)v.findViewById(R.id.add_symptom);
            Button btn_diagnose=(Button)v.findViewById(R.id.diagnose);
            if(count>=3)btn_diagnose.setEnabled(true);
            final FirebaseDatabase database=FirebaseDatabase.getInstance();
            final DatabaseReference myRef=database.getReference();
            final FirebaseAuth mAuth=FirebaseAuth.getInstance();
            RelativeLayout rl=(RelativeLayout)v.findViewById(R.id.activity_diagnose);
            AutoCompleteTextView tv=new AutoCompleteTextView(v.getContext());
            final String symptom=tv.getText().toString();
            ArrayAdapter list =new ArrayAdapter(v.getContext(),android.R.layout.select_dialog_item,symptom_list);
            tv.setThreshold(1);
            tv.setAdapter(list);
            tv.setId(count);
            tv.setHint("Enter your symptom here...");
            if(tv.requestFocus()) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams absParams =
                    (RelativeLayout.LayoutParams)btn_symptom.getLayoutParams();
            p.addRule(RelativeLayout.BELOW,R.id.symptoms1);
            p.topMargin=margin;
            margin+=150;
            absParams.addRule(RelativeLayout.BELOW,R.id.symptoms1);
            absParams.topMargin=margin;

            RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams absParams1 =
                    (RelativeLayout.LayoutParams)btn_diagnose.getLayoutParams();
            p1.addRule(RelativeLayout.BELOW,R.id.symptoms1);
            p1.topMargin=margin;
            absParams1.addRule(RelativeLayout.BELOW,R.id.symptoms1);
            absParams1.topMargin=margin;

            tv.setLayoutParams(p);
            rl.addView(tv);
            chosensymptoms.add(tv);
            btn_symptom.setLayoutParams(absParams);
            btn_diagnose.setLayoutParams(absParams1);


            btn_diagnose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int i=0;i<chosensymptoms.size();i++)
                    {
                        String selection=chosensymptoms.get(i).getText().toString();
                        myRef.child("Session").child(mAuth.getCurrentUser().getUid()).child("Symptoms").child(selection).setValue(1);


                    }

                    getFragmentManager().beginTransaction()
                            .replace(((ViewGroup) getView().getParent()).getId(), new questions())
                            .addToBackStack(null)
                            .commit();
                }
            });

        }

    }
}
