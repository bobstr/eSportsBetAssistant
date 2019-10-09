package com.robsth.e_sportsbetassistant;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;

public class MatchesListActivity extends AppCompatActivity {
    private TextView result;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_list);

        result = (TextView) findViewById(R.id.result);
        result.setMovementMethod(new ScrollingMovementMethod());


        getInfo();
    }

    private void getInfo() {

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while fetching matches data...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect("https://www.hltv.org/matches")
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                            .get();
                    String title = doc.title();
                    Elements matchTabs = doc.select("a.standard-box");

                    builder.append(title).append("\n");

                    for (Element tab : matchTabs) {
                        Document matchDetail = Jsoup.connect("https://www.hltv.org" + tab.attr("href"))
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                                .get();

                        Elements date = matchDetail.select("div.date");
                        Elements team1 = matchDetail.select("div.team1-gradient a div.teamName");
                        Elements team2 = matchDetail.select("div.team2-gradient a div.teamName");
                        Elements matchType = matchDetail.select("div.preformatted-text");

                        builder.append("\n").append(1 + matchTabs.indexOf(tab) + ".")
                                .append("\n")
                                .append("Date: ").append(date.text())
                                .append("\n")
                                .append("Team 1: ").append(team1.text())
                                .append("\n")
                                .append("Team 2: ").append(team2.text())
                                .append("\n")
                                .append("Match type: ").append(matchType.text())
                                .append("\n")
                                .append("Link: ").append(tab.attr("href"))
                                .append("\n");

                        if (matchTabs.indexOf(tab) >= 9) {
                            progress.dismiss();
                            break;
                        }
                    }

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(builder.toString());
                    }
                });
            }
        }).start();
    }
}


