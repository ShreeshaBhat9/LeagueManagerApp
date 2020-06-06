package com.example.leaguemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements HomeFragment.FragmentHomeListener {

    int currentbutton = 1;
    String dataforteam, dataforgame;


    public void getReferee(View view) {
        return;
    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            String APItoken = "ab6b6e24da374a66a02e7061ef1eaba9";

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty ("X-Auth-Token", URLEncoder.encode(APItoken));
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();


                while(data!=-1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                Log.i("info",result);
                return result;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Log.i("info",s);

            try{

                JSONObject J = new JSONObject(s);

                String info = J.getString("matches");

                JSONArray arr = new JSONArray(info);

                //Log.i("EEEEEEEEEEEEEEEE",info);

                db.execSQL("CREATE TABLE IF NOT EXISTS games (id INTEGER PRIMARY KEY, matchday INT(2), hometeam VARCHAR, awayteam VARCHAR, homescore INT(2), awayscore INT(2), status VARCHAR);");

                db.execSQL("CREATE TABLE IF NOT EXISTS players (id INTEGER PRIMARY KEY, name VARCHAR, position VARCHAR, team varchar, goals INT(3), cards INT(3));");

                try {

                    for (int i = 0; i < arr.length(); i++) {

                        J = arr.getJSONObject(i);


                        String id = J.getString("id");

                        String status = J.getString("status");
                        String matchday = J.getString("matchday");

                        info = J.getString("homeTeam");
                        JSONObject J2 = new JSONObject(info);

                        String homename = J2.getString("name");

                        JSONArray arr2;

                        /*try {
                            info = J2.getString("lineup");

                            arr2 = new JSONArray(info);

                            for (int i2 = 0; i2 < arr2.length(); i2++) {
                                //Insert player
                                J2 = arr2.getJSONObject(i2);
                                String pid = J2.getString("id");
                                String pname = J2.getString("name");
                                String ppos = J2.getString("position");

                                String sql = "IF NOT EXISTS (SELECT * FROM players WHERE id = ?) BEGIN INSERT INTO players (id, name, position, team) VALUES(?,?,?,?) END";

                                SQLiteStatement s2 = db.compileStatement(sql);
                                s2.bindString(1, pid);
                                s2.bindString(2, pid);
                                s2.bindString(3, pname);
                                s2.bindString(4, ppos);
                                s2.bindString(5, homename);
                                s2.execute();


                            }
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }


                        try {
                            info = J2.getString("bench");
                            arr2 = new JSONArray(info);

                            for (int i2 = 0; i2 < arr2.length(); i2++) {
                                //Insert player
                                J2 = arr2.getJSONObject(i2);
                                String pid = J2.getString("id");
                                String pname = J2.getString("name");
                                String ppos = J2.getString("position");

                                String sql = "IF NOT EXISTS (SELECT * FROM players WHERE id = ?) BEGIN INSERT INTO players (id, name, position, team) VALUES(?,?,?,?) END";

                                SQLiteStatement s2 = db.compileStatement(sql);
                                s2.bindString(1, pid);
                                s2.bindString(2, pid);
                                s2.bindString(3, pname);
                                s2.bindString(4, ppos);
                                s2.bindString(5, homename);
                                s2.execute();
                            }


                            //Away team
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

*/
                        info = J.getString("awayTeam");
                        J2 = new JSONObject(info);

                        String awayname = J2.getString("name");
/*
                        try {

                            info = J2.getString("lineup");
                            arr2 = new JSONArray(info);

                            for (int i2 = 0; i2 < arr2.length(); i2++) {
                                //Insert player
                                J2 = arr2.getJSONObject(i2);
                                String pid = J2.getString("id");
                                String pname = J2.getString("name");
                                String ppos = J2.getString("position");

                                String sql = "IF NOT EXISTS (SELECT * FROM players WHERE id = ?) BEGIN INSERT INTO players (id, name, position, team) VALUES(?,?,?,?) END";

                                SQLiteStatement s2 = db.compileStatement(sql);
                                s2.bindString(1, pid);
                                s2.bindString(2, pid);
                                s2.bindString(3, pname);
                                s2.bindString(4, ppos);
                                s2.bindString(5, awayname);
                                s2.execute();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        try {
                            info = J2.getString("bench");
                            arr2 = new JSONArray(info);

                            for (int i2 = 0; i2 < arr2.length(); i2++) {
                                //Insert player
                                J2 = arr2.getJSONObject(i2);
                                String pid = J2.getString("id");
                                String pname = J2.getString("name");
                                String ppos = J2.getString("position");

                                String sql = "IF NOT EXISTS (SELECT * FROM players WHERE id = ?) BEGIN INSERT INTO players (id, name, position, team) VALUES(?,?,?,?) END";

                                SQLiteStatement s2 = db.compileStatement(sql);
                                s2.bindString(1, pid);
                                s2.bindString(2, pid);
                                s2.bindString(3, pname);
                                s2.bindString(4, ppos);
                                s2.bindString(5, awayname);
                                s2.execute();
                            }
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }*/
                        info = J.getString("score");
                        J2 = new JSONObject(info);

                        info = J2.getString("fullTime");
                        J2 = new JSONObject(info);

                        String homescore = J2.getString("homeTeam");
                        String awayscore = J2.getString("awayTeam");
/*
                        info = J.getString("goals");
                        arr2 = new JSONArray(info);

                        for(int i2=0;i2<arr2.length();i2++)
                        {
                            //Increment goals
                            J2 = arr2.getJSONObject(i2);
                            info = J2.getString("scorer");
                            J2 = new JSONObject(info);
                            String pid = J2.getString("id");

                            String sql = "UPDATE players SET goals = goals + 1 WHERE id = ?";
                            SQLiteStatement s2 = db.compileStatement(sql);
                            s2.bindString(1,pid);
                            s2.execute();

                        }

                        info = J.getString("bookings");
                        arr2 = new JSONArray(info);

                        for(int i2=0;i2<arr2.length();i2++)
                        {
                            //Increment bookings
                            J2 = arr2.getJSONObject(i2);
                            info = J2.getString("player");
                            J2 = new JSONObject(info);
                            String pid = J2.getString("id");

                            String sql = "UPDATE players SET cards = cards + 1 WHERE id = ?";
                            SQLiteStatement s2 = db.compileStatement(sql);
                            s2.bindString(1,pid);
                            s2.execute();

                        }
*/

                        String sql = "INSERT INTO games (id, matchday, hometeam, awayteam, homescore, awayscore, status) VALUES (?,?,?,?,?,?,?)";
                        SQLiteStatement s2 = db.compileStatement(sql);
                        s2.bindString(1,id);
                        s2.bindString(2,matchday);
                        s2.bindString(3,homename);
                        s2.bindString(4,awayname);
                        s2.bindString(5,homescore);
                        s2.bindString(6,awayscore);
                        s2.bindString(7,status);
                        s2.execute();

                    }

                    Log.i("done","dome");

                }
                catch(Exception e)
                {
                    Log.i("info","EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                    e.printStackTrace();
                }





            }catch(Exception e){
                e.printStackTrace();
            }



        }
    }




    @Override
    public void onInputHomeSent(String s) {

        if (s.charAt(0)=='R') {

            dataforgame = s;
            PlayersFragment P = new PlayersFragment();
            playersFragment = P;
            P.UpdateEditText(s);
            ((BottomNavigationView) this.findViewById(R.id.nav_view)).setSelectedItemId(R.id.nav_players);

        }

        else {

            dataforteam = s;
            TeamsFragment T = new TeamsFragment();
            teamsFragment = T;
            T.UpdateEditText(s);
            ((BottomNavigationView) this.findViewById(R.id.nav_view)).setSelectedItemId(R.id.nav_teams);
            Log.i("came","here");
        }

    }

    SQLiteDatabase db;




    Fragment homeFragment = null;
    Fragment playersFragment = null;
    Fragment liveFragment = null;
    Fragment teamsFragment = null;
    Fragment profileFragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;

                    switch(item.getItemId())
                    {
                        case R.id.nav_home:
                            selectedFragment = homeFragment;
                            break;
                        case R.id.nav_players:
                            if(playersFragment==null) {
                                selectedFragment = new PlayersFragment();
                                playersFragment = selectedFragment;
                            }
                            else
                            {
                                selectedFragment = playersFragment;
                                PlayersFragment TM = (PlayersFragment) selectedFragment;
                                TM.UpdateEditText(dataforgame);
                            }
                            break;
                       /* case R.id.nav_live:
                            if(liveFragment==null) {
                                selectedFragment = new LiveFragment();
                                liveFragment = selectedFragment;
                            }
                            else
                                selectedFragment = liveFragment;
                            break;*/
                        case R.id.nav_teams:
                            if(teamsFragment==null) {
                                selectedFragment = new TeamsFragment();
                                teamsFragment = selectedFragment;

                            }
                            else {
                                selectedFragment = teamsFragment;
                                TeamsFragment TM = (TeamsFragment) selectedFragment;
                                TM.UpdateEditText(dataforteam);
                            }
                            break;
                        /*case R.id.nav_profile:
                            if(profileFragment==null) {
                                selectedFragment = new ProfileFragment();
                                profileFragment = selectedFragment;
                            }
                            else
                                selectedFragment = profileFragment;
                            break;*/
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };

    public void standings(View view)
    {
        currentbutton = 1;
        Log.i("came","standingonclick");

        return;
    }
    public void fixtures(View view)
    {
        currentbutton = 2;
        return;
    }
    public void results(View view)
    {
        currentbutton = 3;
        Log.i("came","resultonclick");

        return;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = this.openOrCreateDatabase("Main",MODE_PRIVATE,null);















        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        homeFragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).commit();


    }
}
