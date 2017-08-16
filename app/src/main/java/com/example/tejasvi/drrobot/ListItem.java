package com.example.tejasvi.drrobot;

/**
 * Created by Tejasvi on 8/15/2017.
 */

public class ListItem
{

    String question;
    String response;

    public ListItem(String question, String response) {
        this.question = question;
        this.response = response;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
