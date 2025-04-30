package com.ptw.task61d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ToggleButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {
    private final List<ToggleButton> selected = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            // 保存兴趣，跳转主页
            String selectedInterest = selected.isEmpty() ? "Algorithms" : selected.get(0).getText().toString();
            // 保存到本地
            SharedPreferences sp = getSharedPreferences("user_profile", MODE_PRIVATE);
            sp.edit().putString("interest", selectedInterest).apply();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("interest", selectedInterest);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        GridLayout grid = findViewById(R.id.gridInterests);
        String[] interests = {"Algorithms", "Data Structures", "Web Development", "Testing"};
        for (int i = 0; i < 8; i++) {
            ToggleButton btn = new ToggleButton(this);
            btn.setTextOn(interests[i % interests.length]);
            btn.setTextOff(interests[i % interests.length]);
            btn.setText(interests[i % interests.length]);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = GridLayout.LayoutParams.WRAP_CONTENT;
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
            lp.setMargins(8, 8, 8, 8);
            btn.setLayoutParams(lp);
            btn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (selected.size() >= 10) {
                        buttonView.setChecked(false);
                        Toast.makeText(this, "最多选择10个兴趣", Toast.LENGTH_SHORT).show();
                    } else {
                        selected.add(btn);
                    }
                } else {
                    selected.remove(btn);
                }
            });
            grid.addView(btn);
        }
    }
} 