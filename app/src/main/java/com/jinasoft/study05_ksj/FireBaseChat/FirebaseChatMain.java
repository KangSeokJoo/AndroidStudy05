package com.jinasoft.study05_ksj.FireBaseChat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jinasoft.study05_ksj.R;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirebaseChatMain extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>mFireBaseAdapter; // 메세지 읽어오는 변수
    private static final String MESSAGE_CHILD = "messages"; // 메세지 보내는 변수 스트링
    private DatabaseReference mDatabaseReference; // 메세지 보내는 변수
    private EditText mMessageEditText;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private static final String TAG = FirebaseChatMain.class.getSimpleName();

    //Firebae 인스턴스
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                firebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = "";
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            case R.id.crash_menu:
                throw new RuntimeException("치명적인 버그");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Google
    private GoogleApiClient mGoogleApiClient;

    //사용자 이름과 사진
    private String mUsername;
    private String mPhotoUrl;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "구글 플레이 서비스 에러", Toast.LENGTH_SHORT).show();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        CircleImageView messengerImageView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            messageImageView = itemView.findViewById(R.id.messageImageView);
            messengerTextView = itemView.findViewById(R.id.messengerTextView);
            messengerImageView = itemView.findViewById(R.id.messengerImageView);
        }
    }
    private RecyclerView mMessageRecyclearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_chat_main);
        //Firebase 실시간 데이터베이스 초기화
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mMessageEditText = findViewById(R.id.message_edit);
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null){
            //인증이 안 되었다면 인증 화면으로 이동
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }else {
            mUsername = firebaseUser.getDisplayName();
            if (firebaseUser.getPhotoUrl() != null){
                mPhotoUrl = firebaseUser.getPhotoUrl().toString();
            }
        }

        mMessageRecyclearView = findViewById(R.id.message_recycler_view);

        //보내기 버튼
        findViewById(R.id.send_btn).setOnClickListener(view -> {
            ChatMessage chatMessage = new ChatMessage(mMessageEditText.getText().toString(), mUsername, mPhotoUrl, null);
            mDatabaseReference.child(MESSAGE_CHILD).push().setValue(chatMessage);
            mMessageEditText.setText("");
        });

        // 쿼리 수행 위치
        Query query = mDatabaseReference.child(MESSAGE_CHILD);
        //옵션
        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
        .setQuery(query, ChatMessage.class)
        .build();

        //어댑터
        mFireBaseAdapter = new FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(MessageViewHolder messageViewHolder, int i, ChatMessage chatMessage) {
                messageViewHolder.messageTextView.setText(chatMessage.getText());
                messageViewHolder.messengerTextView.setText(chatMessage.getName());
                if (chatMessage.getPhotoUrl() == null){
                    messageViewHolder.messageImageView.setImageDrawable(ContextCompat.getDrawable(FirebaseChatMain.this, R.drawable.ic_baseline_account_circle_24));
                }else {
                    Glide.with(FirebaseChatMain.this)
                            .load(chatMessage.getPhotoUrl())
                            .into(messageViewHolder.messengerImageView);
                }
            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_message, parent, false);
                return new MessageViewHolder(view);
            }
        };

        //리사이클러뷰에 레이아웃 매니저와 어댑터 설정
        mMessageRecyclearView.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecyclearView.setAdapter(mFireBaseAdapter);

//        //파이어 베이스 리모트 폰피그 초기화
//        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        //파이어 베이스 리모트 폰피그 설정
//        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings = new FirebaseRemoteConfigSettings.Builder
//                .setDeveloperModeEnable(true)
//                .bulid();
//        //인터넷 연결이 안 되었을 때 기본값 정의
//        Map<String, Object>defaultConfiMap = new HashMap<>();
//        defaultConfiMap.put("message_lenghth", 10L);
//        //설정과 기본값 설정
//        mFirebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
//        mFirebaseRemoteConfig.setDefaults(defaultConfiMap);
//        //원격 구성 가져오기
//        fetchConfig();
//    }
//    //원격 구성 가져오기
//    private void fetchConfig() {
//        long cacheExpiration = 3600; //1시간
//        // 개발자 모드라면 0초 하기
//        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
//            cacheExpiration = 0;
//        }
//        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                mFirebaseRemoteConfig.activateFetched();
//                applyRetrievedLengthLimit();
//            }
//        }).addOnSuccessListener(new OnFailureListener() {
//
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                //원격 구성 가져오기 실패
//                Log.d(TAG,"원격 구성 가져오는데 실패했습니다."+ e.getMessage());
//                applyRetrievedLengthLimit();
//            }
//        });
//    }
//
//    private void applyRetrievedLengthLimit() {
//        Long messageLength = mFirebaseRemoteConfig.getLong("message_length");
//        mMessageEditText.setFilters(new InputFilter[]{
//                new InputFilter.LengthFilter(messageLength.intValue())
//        }); Log.d(TAG , "메세지길이 : " + messageLength);   //오류가 있다..

        //Firebase 초대 기능의 지원을 중단했으며 2020년 1월 24일부터는 지원이 전면 종료됩니다.
    }

    @Override
    protected void onStart() {
        //파이어베이스리사이클어댑터 실시간 쿼리 시작
        super.onStart();
        mFireBaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //파이어베이스리사키을어댑터 실시간 쿼리 중지
        mFireBaseAdapter.stopListening();
    }
}