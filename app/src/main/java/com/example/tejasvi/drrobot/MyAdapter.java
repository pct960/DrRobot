package com.example.tejasvi.drrobot;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Tejasvi on 07-Feb-17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ListItem listItem = listItems.get(position);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();

        holder.question.setText(listItem.getQuestion());

        holder.response.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int id=holder.response.getCheckedRadioButtonId();

                RadioButton answer=(RadioButton)group.findViewById(id);

                if(answer.getText().equals("Yes"))
                {
                    //SEND REPLY
                    //myRef.child("Session").child(mAuth.getCurrentUser().getUid()).child("Questions").child(holder.question.getText().toString()).setValue(answer.getText());
                }
                else if (answer.getText().equals("No"))
                {
                    Toast.makeText(context, "You clicked no", Toast.LENGTH_SHORT).show();
                    //SEND REPLY
                    //myRef.child("Session").child(mAuth.getCurrentUser().getUid()).child("Questions").child(holder.question.getText().toString()).setValue(answer.getText());
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      public RadioGroup response;
        public TextView question;

        public ViewHolder(View itemView) {
            super(itemView);
           response = (RadioGroup) itemView.findViewById(R.id.response);
            int id=response.getCheckedRadioButtonId();
            question=(TextView)itemView.findViewById(R.id.question);
        }
    }
}
