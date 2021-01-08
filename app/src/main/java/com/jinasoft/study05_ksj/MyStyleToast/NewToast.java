package com.jinasoft.study05_ksj.MyStyleToast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jinasoft.study05_ksj.R;

public class NewToast extends Toast {
    public NewToast(Context context) {
        super(context);
    }
    public static Toast makeText(Context context, String message, int duration) {
        Toast toast = new Toast(context);
        //Toast의 레이아웃
        View customview = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        // Toast에 표시될 TextView
        TextView textView = (TextView) customview.findViewById(R.id.ToastAct_TV);
        textView.setText("새로운 토스트");
        // Toast에 레이아웃 설정
        toast.setView(customview);
        toast.setDuration(Toast.LENGTH_SHORT);
        //위치조정
        toast.setGravity(Gravity.CENTER, 0, -300);
        return toast;
    }
}
