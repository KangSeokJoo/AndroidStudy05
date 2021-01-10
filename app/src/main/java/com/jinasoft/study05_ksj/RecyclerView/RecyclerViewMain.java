package com.jinasoft.study05_ksj.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.jinasoft.study05_ksj.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewMain extends AppCompatActivity implements MyRecyclerAdapter.MyRecylerViewClickListener{

    private static final String TAG = RecyclerViewMain.class.getSimpleName();
    private MyRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // 레이아웃 매니져로 LinearLayoutManger를 설정
        RecyclerView.LayoutManager layoutManager; /*= new LinearLayoutManager(this); // 기본 카드뷰*/
//        layoutManager = new GridLayoutManager(this, 2); // 그리드 뷰로 보여줌
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // 수평으로 세로 방향으로 먼저 1, 2 오른쪽 3,4 로 뿌려준다
        recyclerView.setLayoutManager(layoutManager);;
        //표시할 임시 데이터
        List<CardItem> dataList = new ArrayList<>();
        dataList.add(new CardItem("첫번째 아이템", "안드로이드 보이라고 합니다"));
        dataList.add(new CardItem("두번째 아이템", "두 줄 입렫고 해봅니다 \n 두 줄"));
        dataList.add(new CardItem("세번째 아이템", "세줄 \n 두번째 줄\n 세줄"));
        dataList.add(new CardItem("네번째 아이템", "잘되는거 같다"));
        //어댑터 설정
        adapter = new MyRecyclerAdapter(dataList);
        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClicked(int position) {
        Log.d(TAG, "onItemClicked: "+ position);
    }

    @Override
    public void onShareButtonClicked(int position) {
        Log.d(TAG, "onShareButtonClicked: "+ position);
        //아이템 삭제
        adapter.addItem(position, new CardItem("추가 됨", "추가 됨"));
    }

    @Override
    public void onLearnMoreButtonClicked(int position) {
        Log.d(TAG, "onLearnMoreClicked: "+ position);
        adapter.removeItem(position);
    }
}