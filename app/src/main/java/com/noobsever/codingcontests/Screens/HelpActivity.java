package com.noobsever.codingcontests.Screens;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.noobsever.codingcontests.Adapters.HelpAdapter;
import com.noobsever.codingcontests.Models.HelpObject;
import com.noobsever.codingcontests.R;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {

    private static final String TAG = "HelpActivity";
    Toolbar toolbar;
    TextView faqTextView;
    RecyclerView helpRecyclerView;
    ArrayList<HelpObject> helpObjectArrayList;
    HelpAdapter helpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        toolbar = findViewById(R.id.app_bar);
        faqTextView = findViewById(R.id.faq_text_view);
        helpRecyclerView = findViewById(R.id.help_recycler_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("FAQs");

        helpObjectArrayList = new ArrayList<>();    //This ArrayList contains question,answers and an ArrowId which determines the View Visibility
        helpObjectArrayList.add(new HelpObject("Why this app?","To remind coders about different coding contests ",0));
        //helpObjectArrayList.add(new HelpObject("What is the tech stack?","Android, Java, Retrofit, Firebase",0));
        helpObjectArrayList.add(new HelpObject("Is it open source project?","Yes, you can visit HnCC website for more details",0));
        helpObjectArrayList.add(new HelpObject("Is the app free to use?","Yes it's completely free",0));
        helpObjectArrayList.add(new HelpObject("How many coding platforms does the app support","The app supports 8 coding platforms",0));
        helpObjectArrayList.add(new HelpObject("Can I make any changes to the app","yes send a request in the github",0));

        helpAdapter = new HelpAdapter(this,helpObjectArrayList);
        helpRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        helpRecyclerView.setHasFixedSize(true);
        helpRecyclerView.setAdapter(helpAdapter);
    }

}