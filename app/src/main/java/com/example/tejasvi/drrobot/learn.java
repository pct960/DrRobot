package com.example.tejasvi.drrobot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


/**
 * Created by thbr on 25/8/17.
 */

public class learn extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.layout_learn, container, false);
        WebView learnWebView = (WebView) v.findViewById(R.id.learn_web_view);
        learnWebView.loadDataWithBaseURL(null, getString(R.string.learning_stuff), "text/html", "utf-8", null);

return v;
    }
}
