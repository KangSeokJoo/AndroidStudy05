package com.jinasoft.study05_ksj.RealmDB;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinasoft.study05_ksj.R;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmDBMain extends AppCompatActivity implements RealmChangeListener<Realm> {

    private Realm realm;
    private TextView mResultText;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //오류 이후로 추가문 init
        Realm.init(getApplicationContext());

        setContentView(R.layout.activity_realm_db_main);
        realm = Realm.getDefaultInstance();

        mResultText = (TextView)findViewById(R.id.result_text);
        mEmail = (EditText)findViewById(R.id.email_edit);
        mPassword = (EditText)findViewById(R.id.password_edit);
        mNewPassword = (EditText)findViewById(R.id.new_password_edit);

        realm.addChangeListener(this);
        showResult();
    }

    private void showResult(){
        RealmResults<User> userList = realm.where(User.class).findAll();
        mResultText.setText(userList.toString());
    }

    public void SignIn(View view) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        User user = realm.where(User.class)
                .equalTo("email", email)
                .equalTo("password", password)
                .findFirst();
        if (user != null){
            Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show();
        }
    }

    public void SignUp(View view) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm.where(User.class).equalTo("email", mEmail.getText().toString()).count() > 0) {
                    realm.cancelTransaction();
                }
                User user = realm.createObject(User.class);
                user.setEmail(mEmail.getText().toString());
                user.setPassword(mPassword.getText().toString());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(RealmDBMain.this, "성공", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(RealmDBMain.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updatePassword(View view) {
        final User user = realm.where(User.class).equalTo("email", mEmail.getText().toString()).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.setPassword(mNewPassword.getText().toString());
            }
        });
    }

    public void deleteAccount(View view) {
        final RealmResults<User> results = realm.where(User.class).equalTo("email", mEmail.getText().toString()).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onChange(Realm realm) {
        showResult();
    }
}