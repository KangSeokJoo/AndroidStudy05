package com.jinasoft.study05_ksj.FireBaseChat;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFcmService extends FirebaseMessagingService {
    public static final  String TAG = MyFcmService.class.getSimpleName();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //여기서 FCM 메세지를 처리함
        Log.d(TAG,"메세지리시브드 아이디 : " + remoteMessage.getMessageId());
        Log.d(TAG,"메세지리시브드 데이터 : " + remoteMessage.getData());
        super.onMessageReceived(remoteMessage);
    }
}
