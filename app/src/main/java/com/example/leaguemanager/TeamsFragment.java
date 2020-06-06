package com.example.leaguemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TeamsFragment extends Fragment {

    TextView E, D;
    String data = null;
    WebView webView;


    public void showTeam(){
        if(data.charAt(0)==' ')
        data =Character.toString(data.charAt(1));
            else

        {
            String[] splited = data.split(" ");
            data = splited[0];
        }

        SQLiteDatabase db = getActivity().openOrCreateDatabase("Main", Context.MODE_PRIVATE, null);

            Log.e("info","START"+data);

        String table = "teams";
        String[] columns = null;
        String where = "standing = ?";
        String[] args = {data};

        Cursor c = db.query(table, columns, where, args, null, null, null);


        int s1i = c.getColumnIndex("standing");
        int s2i = c.getColumnIndex("name");
        int s3i = c.getColumnIndex("gp");
        int s4i = c.getColumnIndex("won");
        int s5i = c.getColumnIndex("draw");
        int s6i = c.getColumnIndex("lost");
        int s7i = c.getColumnIndex("points");
        int s8i = c.getColumnIndex("image");

            c.moveToFirst();

        data =c.getString(s2i);
        E.setText(data);

        String url = c.getString(s8i);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(url);


        data ="Rank in league : "+c.getString(s1i)+"\nGames played: "+c.getString(s3i)+"\nGames Won: "+c.getString(s4i)+"\nGames drawn: "+c.getString(s5i)+"\nGames lost: "+c.getString(s6i)+"\nPoints: "+c.getString(s7i);
            D.setText(data);
         data = null;
}



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_teams,container,false  );
        E = v.findViewById(R.id.teamsEditText);
        D = v.findViewById(R.id.dataTextView);
        webView = v.findViewById(R.id.webView);

        if(data!=null)
            showTeam();

        return v;


    }
    public void UpdateEditText(String text)
    {
        if(text!=null)
            data = new String(text);
    }



}
