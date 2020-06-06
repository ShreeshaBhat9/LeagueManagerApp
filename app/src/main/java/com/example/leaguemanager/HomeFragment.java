package com.example.leaguemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    int buttonselected;


    private FragmentHomeListener listener;

    public interface FragmentHomeListener {
        void onInputHomeSent(String s);
    }


    public class Team
    {
        String standing;
        String name;
        String gp;
        String won;
        String lost;
        String draw;
        String points;
        String res;

        void fillparam(String s1, String s2, String s3, String s4, String s5, String s6, String s7) {
            standing = s1;
            name = s2;
            gp = s3;
            won = s4;
            draw = s5;
            lost = s6;
            points = s7;
        }

        String toS() {

            if(name.length()>20)
                name = name.substring(0,20);
            res = String.format("%1$2s",standing)+ " " + name + " - " + String.format("%1$3s",points) ;
            return res;

        }
    }




    public class Fixture
    {
        String mday;
        String home;
        String away;

        void fillparam(String s1, String s2, String s3) {
            mday = s1;
            home = s2;
            away = s3;
            }

        String toS() {

            String res = String.format("%1$2s",mday)+ " " + home + " vs " + away;
            return res;

        }
    }




    ListView LV;
    SQLiteDatabase db;
    ArrayList<String> AL, fixture, result;
    String prev="";






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View V = inflater.inflate(R.layout.fragment_home,container,false  );
        LV = V.findViewById(R.id.homeListView);
        AL = new ArrayList<String>();
        fixture = new ArrayList<String>();
        result = new ArrayList<String>();

        db = getActivity().openOrCreateDatabase("Main", Context.MODE_PRIVATE,null);

        Cursor c = db.rawQuery("SELECT * FROM teams", null);

        int s1i = c.getColumnIndex("standing");
        int s2i = c.getColumnIndex("name");
        int s3i = c.getColumnIndex("gp");
        int s4i = c.getColumnIndex("won");
        int s5i = c.getColumnIndex("draw");
        int s6i = c.getColumnIndex("lost");
        int s7i = c.getColumnIndex("points");

        Team T = new Team();

        c.moveToFirst();

        while(!c.isAfterLast()){

            T.fillparam(c.getString(s1i), c.getString(s2i), c.getString(s3i), c.getString(s4i), c.getString(s5i), c.getString(s6i), c.getString(s7i));
            AL.add(T.toS());
            //Log.i("team name",c.getString(s2i));

            c.moveToNext();

        }

        c = db.rawQuery("SELECT * FROM games WHERE status <> 'FINISHED'", null);

        int f1i = c.getColumnIndex("matchday");
        int f2i = c.getColumnIndex("hometeam");
        int f3i = c.getColumnIndex("awayteam");

        Fixture F = new Fixture();

        c.moveToFirst();

        prev="";

        while(!c.isAfterLast()){

            F.fillparam(c.getString(f1i), c.getString(f2i), c.getString(f3i));

            if(prev.equals(""))
                prev = c.getString(f1i);

            if(!prev.equals(c.getString(f1i)))
            {
                prev = c.getString(f1i);
                fixture.add("\n");
            }



            fixture.add(F.toS());

            c.moveToNext();

        }

        c = db.rawQuery("SELECT * FROM games WHERE status = 'FINISHED'", null);

        int r1i = c.getColumnIndex("matchday");
        int r2i = c.getColumnIndex("hometeam");
        int r3i = c.getColumnIndex("awayteam");

        Fixture Re = new Fixture();

        c.moveToFirst();

        prev = "";

        while(!c.isAfterLast()){

            Re.fillparam(c.getString(r1i), c.getString(r2i), c.getString(r3i));

            if(prev.equals(""))
                prev = c.getString(r1i);

            if(!prev.equals(c.getString(r1i)))
            {
                prev = c.getString(r1i);
                result.add("\n");
            }

            result.add(Re.toS());


            c.moveToNext();

        }







        Button standingButton = V.findViewById(R.id.button6);
        standingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonselected = 1;
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, AL);
                LV.setAdapter(arrayAdapter);
            }
        });

        final Button fixturesButton = V.findViewById(R.id.button5);
        fixturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonselected =2;
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fixture);
                LV.setAdapter(arrayAdapter);
            }
        });

        Button resultsButton = V.findViewById(R.id.button7);
        resultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonselected = 3;
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, result);
                LV.setAdapter(arrayAdapter);
            }
        });


        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(buttonselected == 1){

                    String S = (String)parent.getItemAtPosition(position);
                    listener.onInputHomeSent( S );
                    Log.i("came",S);


                }

                else if(buttonselected == 2) {

                }

                else if(buttonselected == 3) {
                    String S = "R" + (String)parent.getItemAtPosition(position);
                    listener.onInputHomeSent( S );
                    Log.i("came",S);

                }

            }
        });

        buttonselected = 1;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, AL);
        LV.setAdapter(arrayAdapter);

        return V;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof FragmentHomeListener)
        {
            listener = (FragmentHomeListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + "must implement homelistener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
