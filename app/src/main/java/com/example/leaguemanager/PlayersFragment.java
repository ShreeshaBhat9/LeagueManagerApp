package com.example.leaguemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PlayersFragment extends Fragment {

    String data;
    TextView E,Ref;
    String id;






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
                urlConnection.setRequestProperty("X-Auth-Token", URLEncoder.encode(APItoken));
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();


                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                Log.i("info", result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String noofmatches;
            String noofgoals;
            String homename;
            String homewins;
            String homedraws;
            String homeloss;
            String awayname;
            String awaywins;
            String awaydraws;
            String awayloss;

            Log.i("info",s);

            try {

                JSONObject J = new JSONObject(s);

                String info = J.getString("head2head");

                J = new JSONObject(info);

                noofmatches = J.getString("numberOfMatches");
                noofgoals = J.getString("totalGoals");

                JSONObject J2 = new JSONObject(J.getString("homeTeam"));

                homename = J2.getString("name");
                homewins = J2.getString("wins");
                homedraws = J2.getString("draws");
                homeloss = J2.getString("losses");

                J2 = new JSONObject(J.getString("awayTeam"));

                awayname = J2.getString("name");
                awaywins = J2.getString("wins");
                awaydraws = J2.getString("draws");
                awayloss = J2.getString("losses");

                Ref.setText("No of matches = "+noofmatches+"\nTotal goals = "+noofgoals + "\n\n" + homename + " vs " + awayname + "\n"+ "WINS : " +homewins + "\t" + awaywins + "\n" + "LOSS : " + homeloss + "\t" + awayloss + "\n" + "DRAWS: " +homedraws + "\t" + awaydraws);

                //Log.i("EEEEEEEEEEEEEEEE",info);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }












    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_players,container,false  );
        E = view.findViewById(R.id.textView2);
        Ref = view.findViewById(R.id.textView3);

        Button B = view.findViewById(R.id.button2);

        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(id!=null) {
                    DownloadTask task = new DownloadTask();
                    task.execute("http://api.football-data.org/v2/matches/" + id);
                }
            }
        });

        if(data!=null)
            showTeam();


        return view;
    }


    public void showTeam(){


        Log.i("data",data);

        //parse data and display

        String[] splited;
        String matchday;

        if(data.charAt(1)==' ') {
            splited = data.split(" ");
            matchday = splited[1];
        }

        else
        {
            matchday = data.substring(1,3);
        }

        splited = data.split(" vs ");
        String awayteam = splited[1];

        Log.i("data",matchday +"?"+ awayteam);


        SQLiteDatabase db = getActivity().openOrCreateDatabase("Main", Context.MODE_PRIVATE, null);

        String table = "games";
        String[] columns = null;
        String where = "matchday = ? and awayteam = ?";
        String[] args = {matchday,awayteam};

        Cursor c = db.query(table, columns, where, args, null, null, null);

        int s1i = c.getColumnIndex("homescore");
        int s2i = c.getColumnIndex("awayscore");
        int s3i = c.getColumnIndex("hometeam");
        int s4i = c.getColumnIndex("id");

        c.moveToFirst();

        id = c.getString(s4i);
        String hometeam =c.getString(s3i);
        String homescore = c.getString(s1i);
        String awayscore = c.getString(s2i);

        data ="Matchday "+matchday+"\n"+hometeam+" vs "+awayteam +"\n"+homescore+"-"+awayscore;

        E.setText(data);
        data = null;
    }




    public void UpdateEditText(String text)
    {
        if(text!=null)
            data = new String(text);
    }
}
