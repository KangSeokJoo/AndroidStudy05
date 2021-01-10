package com.jinasoft.study05_ksj.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jinasoft.study05_ksj.R;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{
    private final List<CardItem> mDataList;
    private MyRecylerViewClickListener mListener;
    public MyRecyclerAdapter(List<CardItem> dataList){
        mDataList = dataList;
    }

    //뷰 홀더를 생성하는 부분. 레이아웃을 만드는 부분
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }
    //뷰 홀더에 데이터를 설정하는 부분
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardItem item = mDataList.get(position);
        holder.title.setText(item.getTitle());
        holder.contents.setText(item.getContents());
        // 클릭 이벤트
        if (mListener != null){
            //현재 위치
            final int pos = position;
            holder.itemView.setOnClickListener(view -> {
                mListener.onItemClicked(pos);
            });
            holder.share.setOnClickListener(view ->{
                mListener.onShareButtonClicked(pos);
            });
            holder.more.setOnClickListener(view ->{
                mListener.onLearnMoreButtonClicked(pos);
            });

        }
    }
    //아이템의 수
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //각각의 아이템의 레퍼런스를 저장할 뷰 홀더 클래스
    //반드시 RecyclerView.ViewHolder를 상속해야 함
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView contents;
        Button share;
        Button more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_text);
            contents = itemView.findViewById(R.id.contents_text);
            share = (Button)itemView.findViewById(R.id.share_button);
            more = (Button)itemView.findViewById(R.id.more_button);
        }
    }
    public void setOnClickListener(MyRecylerViewClickListener listener){
        mListener = listener;
    }
    public interface MyRecylerViewClickListener{
        //아이템 전체 부분의 클릭
        void onItemClicked(int position);
        //Share 버튼 클릭
        void onShareButtonClicked(int position);
        //Learn More 버튼 클릭
        void onLearnMoreButtonClicked(int position);
    }
    public void removeItem(int position){
        mDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataList.size());
    }
    public void addItem(int position, CardItem item){
        mDataList.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mDataList.size());
    }
}
