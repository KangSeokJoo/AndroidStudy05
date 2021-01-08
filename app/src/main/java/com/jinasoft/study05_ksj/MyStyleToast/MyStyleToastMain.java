package com.jinasoft.study05_ksj.MyStyleToast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jinasoft.study05_ksj.R;

public class MyStyleToastMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_style_toast_main);
    }

    public void showNewToast(View view) {
        NewToast.makeText(this, "이것이 뉴 토스트", Toast.LENGTH_SHORT).show();
    }
}