package com.diaochan.aidl_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.diaochan.aidl.ILoginInterface;


public class LoginService extends Service {

    private static final String TAG = "diaochan >>>";
    
    @Override
    public IBinder onBind(Intent intent) {
        return new ILoginInterface.Stub() {
            @Override
            public void login() {
                Log.e(TAG,"BinderB_LoginService");
                
                //Client请求后拉活Service,这时需要开启Activity
                // 单向通信，真实项目中；跨进程都是双向绑定的
                serviceStartActivity();
            }

            @Override
            public void loginCallback(boolean loginStatus, String loginUser) throws RemoteException {

            }
        };
    }

    private void serviceStartActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
