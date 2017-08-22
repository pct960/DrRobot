package com.example.tejasvi.drrobot;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

// TODO: getting symptoms dynamically, by iterating thru controls
public class diagnose extends Fragment {

    AutoCompleteTextView initsymptom;
    ArrayList<String> chosensymptoms=new ArrayList<>();
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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            chosensymptoms = bundle.getStringArrayList("symptoms");
        }
        chosensymptoms.add(initsymptom.getText().toString());
        symptom_list=getResources().getStringArray(R.array.symptom_list);
        ArrayAdapter list =new ArrayAdapter(v.getContext(),android.R.layout.select_dialog_item,symptom_list);
        initsymptom.setThreshold(1);
        initsymptom.setAdapter(list);
        Button btn=(Button)v.findViewById(R.id.add_symptom);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_symptom_box();
            }
        });
        return v;
    }

    void add_symptom_box()
    {
        if(count==5)
        {
            Toast.makeText(v.getContext(), "You can only add a maximum of five symptoms at a time ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            count++;

            RelativeLayout rl=(RelativeLayout)v.findViewById(R.id.activity_diagnose);
            Button btn=(Button)v.findViewById(R.id.add_symptom);
            AutoCompleteTextView tv=new AutoCompleteTextView(v.getContext());
            ArrayAdapter list =new ArrayAdapter(v.getContext(),android.R.layout.select_dialog_item,symptom_list);
            tv.setThreshold(1);
            tv.setAdapter(list);
            chosensymptoms.add(tv.getText().toString());
            tv.setId(count);
            tv.setHint("Enter your symptom here...");
            if(tv.requestFocus()) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams absParams =
                    (RelativeLayout.LayoutParams)btn.getLayoutParams();
            p.addRule(RelativeLayout.BELOW,R.id.symptoms1);
            p.topMargin=margin;
            margin+=150;
            absParams.addRule(RelativeLayout.BELOW,R.id.symptoms1);
            absParams.topMargin=margin;
            tv.setLayoutParams(p);
            rl.addView(tv);
            btn.setLayoutParams(absParams);

        }

    }
}
