package com.jinasoft.study05_ksj.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinasoft.study05_ksj.R;

public class EventMain extends AppCompatActivity {

    private TextView textView1;
    private View view;
    private EditText editText1;
    private EditText editText2;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_main);
        textView1 = (TextView) findViewById(R.id.EventAct_TV1);
        view = findViewById(R.id.EventAct_View);
        editText1 = (EditText)findViewById(R.id.EventAct_EDT1);
        editText2 = (EditText)findViewById(R.id.EventAct_EDT2);
        textView2 = (TextView) findViewById(R.id.EventAct_TV2);
        // 클릭
        setClickEvent();
        //포커스 변경
        setFocusEvent();
        //키
        setKeyEvent();
        //터치
        setTouchEvent();
    }

    private void setTouchEvent() {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN :
                        Toast.makeText(EventMain.this, "터치 다운", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE :
                        textView2.setText("터치 정보 :" + motionEvent.toString());
                        break;
                    case MotionEvent.ACTION_UP :
                        Toast.makeText(EventMain.this, "터치 업", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    private void setKeyEvent() {
        View.OnKeyListener keyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                //엔터키를 뗄 때 토스트 표시
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == keyEvent.ACTION_UP){
                    Toast.makeText(EventMain.this, "뒤로가기를 눌렀습니다.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        };
        View.OnKeyListener keyListener2 = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                //엔터키를 뗄 때 토스트 표시
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == keyEvent.ACTION_UP){
                    Toast.makeText(EventMain.this, "뒤로가기를 눌렀습니다.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        };
        editText1.setOnKeyListener(keyListener);
        editText2.setOnKeyListener(keyListener2);
    }

    private void setFocusEvent() {
        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //포커스를 가지면 배경색을 빨간색으로
                if (b){
                    view.setBackgroundColor(Color.LTGRAY);
                }else {
                    view.setBackgroundColor(Color.DKGRAY);
                }
            }
        };
        editText1.setOnFocusChangeListener(focusChangeListener);
        editText2.setOnFocusChangeListener(focusChangeListener);
    }

    private void setClickEvent(){
        textView1.setOnClickListener(view -> {
            Toast.makeText(this, "클릭", Toast.LENGTH_SHORT).show();
        });
        //롱클릭
        textView1.setOnLongClickListener(view -> {
            Toast.makeText(this, "롱 클릭", Toast.LENGTH_SHORT).show();
            //이벤트 소비
            return true;
        });
    }


}