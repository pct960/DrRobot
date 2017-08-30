package com.example.tejasvi.drrobot;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.json.simple.parser.JSONParser;
/**
 * Created by thbr on 30/8/17.
 */

public class Brain
{
    static String database[][];
    static Map<String,String> symptom_question=new ConcurrentHashMap<>();
    static Map<String,String> disease_list=new ConcurrentHashMap<>();
    static Map<String,String> symptom_column=new ConcurrentHashMap<>();
    static Map<String,String> disease_row=new ConcurrentHashMap<>();

    static ArrayList<String> symptoms=new ArrayList<>();
    static Map<String,Integer> common_symptoms =new ConcurrentHashMap<>();
    static boolean positive=false;
    static ArrayList<String> elimination_list=new ArrayList<>();
    static Stack<String> priority_stack=new Stack<>();

    static Map<String,String> positive_disease_list=new ConcurrentHashMap<>();

    static void init()throws Exception
    {
        database = new String[][]{{"","Fever", "Chills","Cough","Muscle Ache","Sneezing","Headache","Fatigue","Congestion","Bloody Diarrhoea",//9
                "Abdominal Pain","Inability To Empty Bowels","Cramps","Indigestion","Vomiting","Flatulence","Nausea","Yellowing Of Eyes",//17
                "Loss Of Appetite","Dark Urine","Itching","Swelling","Body Pain","Rashes","Easy Bruising","Sore Throat","Sweating",//26
                "Shivering","Fast Heart Rate","Diarrhoea","Abdominal Tenderness","Decreased Body Weight","Retching","Dehydration",//32
                "Dry Mouth","Hunger","Stomach Pain","Dizziness","Tiredness","Weakness","Redness Of Eyes","Itching Of Eyes",
                "Discharge From Eyes","Tearing Up Of Eyes","Runny Nose","Paralysis","Confusion","Coughing Blood","Night Sweats",
                "Coughing Phlegm"},
                {"Influenza","1","1","1","1","1","1","1","1","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0",
                        "0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","1","0","0","0","0","0"},
                {"Dysentery","1","0","0","0","0","0","0","0","1","1","1","1","1","1","1","1","0","0","0","0","0","0","0","0","0","0","0",
                        "0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
                {"Hepatitis","0","0","0","0","0","0","1","0","0","1","0","0","0","0","0","0","1","1","1","1","1","0","0","0","0","0","0",
                        "0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
                {"Dengue","1","1","0","1","0","1","1","0","0","1","0","0","0","1","0","1","0","1","0","0","0","0","1","1","1","0","0",
                        "0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
                {"Malaria","1","1","0","1","0","1","1","0","0","0","0","0","0","0","0","1","0","0","0","0","0","0","0","0","0","1","1",
                        "1","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
                {"Typhoid","1","1","0","0","0","1","1","0","0","1","1","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0",
                        "0","1","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
                {"Appendicitis","1","1","0","0","0","0","0","0","0","1","0","0","0","1","0","1","0","1","0","0","0","0","0","0","0","0","0",
                        "0","0","1","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
                {"Gastroenteritis","1","0","0","1","0","0","0","0","0","0","0","0","0","1","0","0","1","0","0","0","0","0","0","0","0","0","0",
                        "0","0","0","1","1","0","0","1","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
                {"Cholera","1","0","0","0","0","0","0","0","0","0","0","0","0","1","0","0","0","0","0","0","0","0","0","0","0","0","0",
                        "0","1","0","0","1","1","1","1","0","0","1","0","0","0","0","0","0","0","0","0","0"},
                {"Common Cold","1","1","1","1","1","1","1","1","0","0","0","0","0","0","0","0","0","1","0","0","0","0","0","0","1","0","0",
                        "0","0","0","0","0","0","0","0","1","0","0","0","0","0","0","1","0","0","0","0","1"},
                {"Food Poisoning","1","0","0","0","0","0","1","0","0","0","0","0","1","1","0","0","0","1","0","0","0","0","0","0","0","0","0",
                        "0","1","0","0","1","0","0","1","1","0","1","0","0","0","0","0","0","0","0","0","0"},
                {"Gastritis","0","0","0","0","0","0","0","0","0","0","0","0","1","1","0","0","0","1","0","0","0","0","0","0","0","0","0",
                        "0","0","0","0","0","0","0","1","1","0","0","0","0","0","0","0","0","0","0","0","0"},
                {"Tuberculosis","1","1","1","0","0","0","1","0","0","0","0","0","0","0","0","0","0","1","0","0","0","0","0","0","0","0","0",//27
                        "0","0","1","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","1","1","0"},
                {"Encephalitis","1","0","0","1","0","1","1","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0",
                        "0","0","0","0","0","0","0","0","0","0","1","0","0","0","0","0","1","1","0","0","0"},
                {"Conjunctivitis","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0",
                        "0","0","0","0","0","0","0","0","0","0","0","1","1","1","1","0","0","0","0","0","0"}};

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("symptom-question.json"));
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;

        for(int i=0;i<15;i++)
        {
            disease_row.put(database[i][0],String.valueOf(i));
        }

        for(int i=1;i<49;i++)
        {
            symptom_column.put(database[0][i],String.valueOf(i));
            symptom_question.put(database[0][i],jsonObject.get(database[0][i]).toString());
        }

    }


    public static void main(String[] args) throws Exception
    {
        init();
        accept_input();
        logic();

    }

    static void accept_input()throws Exception
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter atleast 3 symptoms. Enter done when finished");

        String s="";

        while(!s.equalsIgnoreCase("done"))
        {
            s=br.readLine();
            if(s.equalsIgnoreCase("done"))break;

            else
            {
                symptoms.add(s);
                elimination_list.add(s);
            }

        }
    }

    static void logic()throws Exception
    {
        int count=0,threshold=symptoms.size()-1;

        for(int i=0;i<15;i++)
        {
            for(String symptom_name:symptoms)
            {
                if(database[i][Integer.parseInt(symptom_column.get(symptom_name))].equals("1"))
                {
                    count++;
                }
            }

            if(count>=threshold)
            {
                disease_list.put(database[i][0],"0");
            }
            count=0;
        }

        for (int j = 1; j < 49; j++)
        {
            if (symptoms.contains(database[0][j])) continue;

            count = 0;

            for (String disease : disease_list.keySet())
            {
                if (database[Integer.parseInt(disease_row.get(disease))][j].equals("1"))
                {
                    count++;
                }
            }

            if ((!symptoms.contains(database[0][j])) && (count > 1)) common_symptoms.put(database[0][j], count);
        }

        common_symptoms = sortByValueAsc(common_symptoms);

        for (Map.Entry<String, Integer> entry : common_symptoms.entrySet())
            priority_stack.push(entry.getKey());

        while (disease_list.size()>1)
        {
            fire_question();
        }

        System.out.print("It appears that you have ");

        for(String s: disease_list.keySet())
        {
            System.out.print(s);
        }

    }

    static void fire_question()throws Exception
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        if(priority_stack.isEmpty())
        {
            positive=true;
            positive_questions();

        }

        else
        {
            String symptom=priority_stack.pop();
            String question=symptom_question.get(symptom);
            elimination_list.add(symptom);

            System.out.println(question);

            int response=Integer.parseInt(br.readLine());

            if(response==0)initiate_strike(symptom);

            else if (response==1)return;

        }

    }

    static void initiate_strike(String symptom)
    {
        for(String s : disease_list.keySet())
        {
            if(database[Integer.parseInt(disease_row.get(s))][Integer.parseInt(symptom_column.get(symptom))].equals("1"))
            {
                int strike_count=Integer.parseInt(disease_list.get(s));

                strike_count++;

                if(strike_count==3)
                {
                    disease_list.remove(s);
                }
                else
                {
                    disease_list.put(s,String.valueOf(strike_count));
                }

            }
        }
    }

    static void positive_questions()throws Exception
    {
        for(String s:disease_list.keySet())
        {
            for(int j=1;j<49;j++)
            {
                if((database[Integer.parseInt(disease_row.get(s))][j].equals("1"))&&(!elimination_list.contains(database[0][j])))
                {
                    elimination_list.add(database[0][j]);
                    priority_stack.push(database[0][j]);
                    fire_question();
                    if(disease_list.get(s)==null)j=50;
                }
            }
        }

        disease_list=sortByValueAsc(disease_list);

        int count=0;

        for(Map.Entry<String,String> entry:disease_list.entrySet())
        {
            count++;

            if(count>1)
            {
                disease_list.remove(entry.getKey());
            }
        }

    }

    static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAsc(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())/*Collections.reverseOrder()*/
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    
}
