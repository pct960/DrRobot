package com.example.tejasvi.drrobot;

/**
 * Created by Tejasvi on 9/10/2017.
 */

public class Result_ListItem
{
    String disease,probability;

    public Result_ListItem(String disease, String probability)
    {
        this.disease = disease;
        this.probability = probability;
    }

    public String getDisease() {
        return disease;
    }

    public String getProbability() {
        return probability;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }
}
