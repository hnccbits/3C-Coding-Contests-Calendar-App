package com.hnccbits.codingcontestscalendar.Screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hnccbits.codingcontestscalendar.R;

public class DevelopersActivity extends AppCompatActivity implements View.OnClickListener {
    //Declaration of Bottom Sheet Variable
    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetDialog bottomSheetDialog;
    View bottom_sheet,view;
    ImageView imgClose,profilepic;
    Button btnPingMe;

    LinearLayout layout1,layout2,layout3,layout_github,layout_linkedin,layout_email;
    TextView txtUserName,github,linkedin,email;
    String name="",github_url="",linkedin_url="",emailid="";
    int imageResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);
        layout1=findViewById(R.id.dev1);
        layout2=findViewById(R.id.dev2);
        layout3=findViewById(R.id.dev3);
//        showBottomSheetDialog();
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
    }

    private void showBottomSheetDialog() {
        bottom_sheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        view = getLayoutInflater().inflate(R.layout.bottom_sheet,null);
        bottomSheetDialog = new BottomSheetDialog(this);

        bottomSheetDialog.setContentView(view);
//        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent)); //it is depricated after API 21
        ((View) view.getParent()).setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

        txtUserName = view.findViewById(R.id.name);
        imgClose = view.findViewById(R.id.img_close);
        profilepic=view.findViewById(R.id.imageView);
        github=view.findViewById(R.id.github);
        linkedin=view.findViewById(R.id.linkedin);
        email=view.findViewById(R.id.email);
        layout_github=view.findViewById(R.id.layout_github);
        layout_linkedin=view.findViewById(R.id.layout_linkedin);
        layout_email=view.findViewById(R.id.layout_email);

        txtUserName.setText(name);
        github.setText(github_url);
        linkedin.setText(linkedin_url);
        email.setText(emailid);
        profilepic.setImageResource(imageResource);

        layout_github.setOnClickListener(this);
        layout_linkedin.setOnClickListener(this);
        layout_email.setOnClickListener(this);
        imgClose.setOnClickListener(v -> bottomSheetDialog.dismiss());


    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.dev1:
                name="Raj Sinha";
                github_url="https://github.com/RajSinha77";
                linkedin_url="https://www.linkedin.com/in/raj-sinha-bit/";
                emailid="rajsinha.bit@gmail.com";
                imageResource= R.drawable.raj;
                showBottomSheetDialog();
                bottomSheetDialog.show();

                break;
            case R.id.dev2:
                name="Anubhaw Sharma";
                github_url="https://github.com/Anubhaw19";
                linkedin_url="https://www.linkedin.com/in/anubhaw19/";
                emailid="anubhawapp@gmail.com";
                imageResource= R.drawable.anu;
                 showBottomSheetDialog();
                bottomSheetDialog.show();

                break;
            case R.id.dev3:
                name="Shivam Anand";
                github_url="https://github.com/anandshivam44";
                linkedin_url="https://www.linkedin.com/in/anandshivam44/";
                emailid="anand.shivam44@yahoo.com";
                imageResource= R.drawable.shiv;
                showBottomSheetDialog();
                bottomSheetDialog.show();
                break;
            case R.id.layout_github:
                Toast.makeText(this, "github", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(github_url)));
                break;
            case R.id.layout_linkedin:
                Toast.makeText(this, "linkedin", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(linkedin_url)));
                break;
            case R.id.layout_email:
                Toast.makeText(this, "email", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, emailid);
                startActivity(intent);
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
//                startActivity(Intent.createChooser(intent, "Send Email"));
                break;

        }


    }
}