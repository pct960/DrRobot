package com.example.tejasvi.drrobot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by thbr on 25/8/17.
 */

public class learn extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.layout_learn, container, false);
         TextView learntv =(TextView) v.findViewById(R.id.learn_text_view);
        learntv.setText(Html.fromHtml(getString(R.string.learning_stuff)));
return v;
    }
}
