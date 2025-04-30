package com.ptw.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String username = getIntent().getStringExtra("username");
        String interest = getIntent().getStringExtra("interest");
        TextView tvName = findViewById(R.id.tvProfileName);
        TextView tvInterest = findViewById(R.id.tvProfileInterest);
        tvName.setText("Username: " + (username == null ? "" : username));
        tvInterest.setText("Interest: " + (interest == null ? "" : interest));
        Button btnEditInterest = findViewById(R.id.btnEditInterest);
        btnEditInterest.setOnClickListener(v -> {
            Intent intent = new Intent(this, InterestsActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }
} 