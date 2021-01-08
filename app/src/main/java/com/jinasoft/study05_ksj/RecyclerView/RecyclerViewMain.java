package com.jinasoft.study05_ksj.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.jinasoft.study05_ksj.R;

public class RecyclerViewMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // 레이아웃 매니져로 LinearLayoutManger를 설정
    }
}