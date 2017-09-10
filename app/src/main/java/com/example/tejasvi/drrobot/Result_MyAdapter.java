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

public class Result_MyAdapter extends RecyclerView.Adapter<Result_MyAdapter.ViewHolder> {

    private List<Result_ListItem> listItems;
    private Context context;

    public Result_MyAdapter(List<Result_ListItem> listItems, Context context) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Result_ListItem listItem = listItems.get(position);

        holder.disease.setText(listItem.getDisease());
        holder.probability.setText(listItem.getProbability());

    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView disease;
        public TextView probability;

        public ViewHolder(View itemView) {
            super(itemView);
            disease = (TextView) itemView.findViewById(R.id.disease);
            probability=(TextView)itemView.findViewById(R.id.probability);
        }
    }
}
