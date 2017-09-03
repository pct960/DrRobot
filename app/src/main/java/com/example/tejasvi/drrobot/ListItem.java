package com.example.tejasvi.drrobot;

/**
 * Created by Tejasvi on 8/15/2017.
 */

public class ListItem
{

    static String question;
    static String response;

    public ListItem(String quest, String resp) {
        question = quest;
        response = resp;
    }

    public static String getQuestion() {
        return question;
    }

    public static void setQuestion(String quest) {
        question = quest;
    }

    public static String getResponse() {
        return response;
    }

    public static void setResponse(String resp) {
        response = resp;
    }
}
