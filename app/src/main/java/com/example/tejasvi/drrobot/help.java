package com.example.tejasvi.drrobot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by thbr on 25/8/17.
 */

public class help extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.layout_help, container, false);
        return v;
    }
}
