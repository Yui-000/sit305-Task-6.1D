package com.ptw.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import android.util.Log;
import android.widget.Toast;
import android.content.SharedPreferences;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {
    static class Task {
        String title;
        String desc;
        Task(String t, String d) { title = t; desc = d; }
    }
    static class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
        List<Task> tasks;
        View.OnClickListener onClick;
        TaskAdapter(List<Task> t, View.OnClickListener c) { tasks = t; onClick = c; }
        static class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDesc;
            Button btnEnter;
            TaskViewHolder(View v) {
                super(v);
                tvTitle = v.findViewById(R.id.tvTaskTitle);
                tvDesc = v.findViewById(R.id.tvTaskDesc);
                btnEnter = v.findViewById(R.id.btnEnterTask);
            }
        }
        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            Task task = tasks.get(position);
            holder.tvTitle.setText(task.title);
            holder.tvDesc.setText(task.desc);
            holder.btnEnter.setOnClickListener(onClick);
            holder.btnEnter.setTag(position);
        }
        @Override
        public int getItemCount() { return tasks.size(); }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            final String username = getIntent().getStringExtra("username");
            String topicRaw = getIntent().getStringExtra("interest");
            if (topicRaw == null || topicRaw.isEmpty()) {
                SharedPreferences sp = getSharedPreferences("user_profile", MODE_PRIVATE);
                topicRaw = sp.getString("interest", "Algorithms");
            }
            final String topic = (topicRaw == null || topicRaw.isEmpty()) ? "Algorithms" : topicRaw;
            TextView tvHello = findViewById(R.id.tvHello);
            if (username != null && !username.isEmpty()) {
                tvHello.setText("Hello, " + username);
            }
            RecyclerView rvTasks = findViewById(R.id.rvTasks);
            rvTasks.setLayoutManager(new LinearLayoutManager(this));
            List<Task> taskList = new ArrayList<>();
            taskList.add(new Task("Generated Task 1", "Click to generate your personalized quiz"));
            TaskAdapter adapter = new TaskAdapter(taskList, v -> {
                int pos = (int) v.getTag();
                Intent intent = new Intent(this, TaskDetailActivity.class);
                intent.putExtra("task_id", pos);
                intent.putExtra("username", username);
                intent.putExtra("interest", topic);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            });
            rvTasks.setAdapter(adapter);
            TextView tvTaskDue = findViewById(R.id.tvTaskDue);
            tvTaskDue.setText("You have " + taskList.size() + " task" + (taskList.size() == 1 ? "" : "s") + " due");
            ImageView ivAvatar = findViewById(R.id.ivAvatar);
            ivAvatar.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("interest", topic);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "onCreate error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
} 