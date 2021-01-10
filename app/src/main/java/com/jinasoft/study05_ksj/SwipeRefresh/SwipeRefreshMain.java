package com.jinasoft.study05_ksj.SwipeRefresh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.jinasoft.study05_ksj.R;

public class SwipeRefreshMain extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh_main);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        //색 설정
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN);
        //리스너 설정
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        //갱신 처리 시작
        //예제에서는 단순히 2초 후에 처리가 종료된 것으로 함
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //갱신 처리가 끝나면 인디케이터를 비표시
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(SwipeRefreshMain.this, "로딩이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }, 2000); // 딜레이밀리스를 안주면 빨간줄
    }
}