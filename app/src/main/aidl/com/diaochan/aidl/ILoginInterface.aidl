// ILoginInterface.aidl
package com.diaochan.aidl;


interface ILoginInterface {
    
    // 登录
    void login();
    
    // 登录返回
    void loginCallback(boolean loginStatus,String loginUser);
}
