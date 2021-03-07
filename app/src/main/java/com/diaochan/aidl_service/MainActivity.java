package com.diaochan.aidl_service;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.diaochan.aidl.ILoginInterface;

public class MainActivity extends AppCompatActivity {

    private static final String NAME = "diaochan";
    private static final String PWD = "89757";

    private EditText nameEt;
    private EditText pwdEt;

    private ILoginInterface iLogin;//AIDL
    private boolean isStartRemote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        nameEt = findViewById(R.id.nameEt);
        pwdEt = findViewById(R.id.pwdEt);


        initBindService();
    }

    private void initBindService() {
        Intent intent = new Intent();
        intent.setAction("BindA_Action");
        intent.setPackage("com.diaochan.aidl_client");
        bindService(intent, conn, BIND_AUTO_CREATE);

        isStartRemote = true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isStartRemote) {
            unbindService(conn);
            isStartRemote = false;
        }
    }

    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iLogin = ILoginInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void startLogin(View view) {
        final String name = nameEt.getText().toString();
        final String pwd = pwdEt.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //模拟登陆的dialog
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("登录");
        dialog.setMessage("登录中...");
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isLoginStatus = false;
                        if (NAME.equals(name) && PWD.equals(pwd)) {
                            isLoginStatus = true;
                            Toast.makeText(MainActivity.this, "QQ登录成功~！", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "QQ登录失败~！", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                        try {
                            //告知Client结果
                            if (iLogin != null) {
                                iLogin.loginCallback(isLoginStatus, name);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
}