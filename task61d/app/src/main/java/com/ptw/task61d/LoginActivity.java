package com.ptw.task61d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);
        EditText etUsername = findViewById(R.id.etUsername);

        btnLogin.setOnClickListener(v -> {
            // 登录逻辑，成功后跳转主页
            String username = etUsername.getText().toString();
            SharedPreferences sp = getSharedPreferences("user_profile", MODE_PRIVATE);
            String interest = sp.getString("interest", "Algorithms");
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("interest", interest);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }
} 