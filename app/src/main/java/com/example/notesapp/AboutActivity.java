package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView linkedinContributor1 = findViewById(R.id.linkedinContributor1);
        TextView githubContributor1 = findViewById(R.id.githubContributor1);
        TextView instagramContributor1 = findViewById(R.id.instagramContributor1);

        setupLink(linkedinContributor1, "https://www.linkedin.com/in/butkua/");
        setupLink(githubContributor1, "https://github.com/butkua123");
        setupLink(instagramContributor1, "https://www.instagram.com/utku_irl/");

        // Repeat for Contributor 2
        TextView linkedinContributor2 = findViewById(R.id.linkedinContributor2);
        TextView githubContributor2 = findViewById(R.id.githubContributor2);
        TextView instagramContributor2 = findViewById(R.id.instagramContributor2);

        setupLink(linkedinContributor2, "https://www.linkedin.com/in/bilgekagant/");
        setupLink(githubContributor2, "https://github.com/bilgekagant");
        setupLink(instagramContributor2, "https://www.instagram.com/bilgekagant/");
    }

    private void setupLink(TextView textView, String url) {
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}
