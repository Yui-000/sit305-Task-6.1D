package com.ptw.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TaskDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        String username = getIntent().getStringExtra("username");
        String interest = getIntent().getStringExtra("interest");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        String url = "http://10.0.2.2:5001/getQuiz?topic=" + interest;
        Request request = new Request.Builder().url(url).build();
        ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(TaskDetailActivity.this, "API error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        JSONObject obj = new JSONObject(body);
                        JSONArray quizArr = obj.getJSONArray("quiz");
                        final int[] realCount = {0};
                        runOnUiThread(() -> {
                            LinearLayout layoutQuestions = findViewById(R.id.layoutQuestions);
                            layoutQuestions.removeAllViews();
                            resultList.clear();
                            for (int i = 0; i < quizArr.length(); i++) {
                                try {
                                    JSONObject qObj = quizArr.getJSONObject(i);
                                    String question = qObj.getString("question");
                                    JSONArray options = qObj.getJSONArray("options");
                                    if (question.contains("[Your question here]")) continue;
                                    realCount[0]++;
                                    TextView qTitle = new TextView(TaskDetailActivity.this);
                                    qTitle.setText(realCount[0] + ". " + question);
                                    qTitle.setTextSize(16);
                                    layoutQuestions.addView(qTitle);
                                    RadioGroup radioGroup = new RadioGroup(TaskDetailActivity.this);
                                    ArrayList<String> optionList = new ArrayList<>();
                                    for (int j = 0; j < options.length(); j++) {
                                        RadioButton rb = new RadioButton(TaskDetailActivity.this);
                                        rb.setText(options.getString(j));
                                        radioGroup.addView(rb);
                                        optionList.add(options.getString(j));
                                    }
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("question", question);
                                    map.put("options", optionList);
                                    map.put("selected", -1);
                                    resultList.add(map);
                                    int idx = resultList.size() - 1;
                                    radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                                        int checkedIndex = group.indexOfChild(group.findViewById(checkedId));
                                        resultList.get(idx).put("selected", checkedIndex);
                                    });
                                    layoutQuestions.addView(radioGroup);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(TaskDetailActivity.this, "Parse error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }
        });
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("results_json", new org.json.JSONArray(resultList).toString());
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }
} 