package com.ptw.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String username = getIntent().getStringExtra("username");
        TextView tvResultsTitle = findViewById(R.id.tvResultsTitle);
        if (username != null && !username.isEmpty()) {
            tvResultsTitle.setText("Your Results, " + username);
        }

        String resultsJson = getIntent().getStringExtra("results_json");
        LinearLayout layoutResults = findViewById(R.id.layoutResults);
        if (resultsJson != null) {
            try {
                JSONArray arr = new JSONArray(resultsJson);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String question = obj.getString("question");
                    JSONArray options = obj.getJSONArray("options");
                    int selected = obj.getInt("selected");
                    StringBuilder sb = new StringBuilder();
                    sb.append((i+1) + ". " + question + "\n");
                    for (int j = 0; j < options.length(); j++) {
                        sb.append((char)('A'+j) + ". " + options.getString(j));
                        if (j == selected) sb.append("  (Your answer)");
                        sb.append("\n");
                    }
                    TextView tv = new TextView(this);
                    tv.setText(sb.toString());
                    tv.setBackgroundResource(android.R.color.white);
                    tv.setPadding(24, 24, 24, 24);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 0, 24);
                    tv.setLayoutParams(lp);
                    layoutResults.addView(tv);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Button btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            // 返回主页
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }
} 