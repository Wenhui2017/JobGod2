package com.ant.jobgod.jobgod.model;

import android.content.Context;

import com.android.http.RequestManager;
import com.android.http.RequestMap;
import com.ant.jobgod.jobgod.config.API;
import com.ant.jobgod.jobgod.model.bean.AccountData;
import com.ant.jobgod.jobgod.model.bean.UserAccountData;
import com.ant.jobgod.jobgod.model.callback.DataCallback;
import com.ant.jobgod.jobgod.model.callback.StatusCallback;
import com.ant.jobgod.jobgod.util.FileManager;
import com.ant.jobgod.jobgod.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Mr.Jude on 2015/6/12.
 * 关于账户的信息。主要用于账户类型：商家／用户，和权限管理。
 * 会发送AccountInfo事件
 */
public class AccountModel extends AbsModel{
    private static final String ACCOUNTFILE = "account";
    public static AccountModel getInstance() {
        return getInstance(AccountModel.class);
    }

    public boolean isUser = true;
    public UserAccountData userAccountData;

    @Override
    protected void onAppCreate(Context ctx) {
        super.onAppCreate(ctx);
        userAccountData = (UserAccountData) Utils.readObjectFromFile(FileManager.getInstance().getChild(FileManager.Dir.Object,ACCOUNTFILE));
        if (userAccountData !=null) {
            applyToken(userAccountData.getTokenApp());
            updateAccountData();
        } else {
            applyToken("");
        }
    }

    public boolean isUser(){
        return isUser;
    }

    public AccountData getAccount(){
        if (isUser)
            return userAccountData;
        else
            return null;
    }

    public UserAccountData getUserAccount() {
        return userAccountData;
    }


    public void updateAccountData(){
        RequestManager.getInstance().post(API.URL.GetUserData, null, new DataCallback<UserAccountData>() {
            @Override
            public void success(String info, UserAccountData data) {
                setUserAccountData(data);
            }
        });
    }

    public void setUserAccountData(UserAccountData userAccountData){
        isUser = true;
        this.userAccountData = userAccountData;
        saveAccount();
        applyToken(userAccountData.getTokenApp());
        publicEvent(userAccountData);
        RongYunModel.getInstance().connectRongYun(userAccountData.getRongToken());
    }

    public void saveAccount(){
        if (isUser){
            Utils.writeObjectToFile(userAccountData,FileManager.getInstance().getChild(FileManager.Dir.Object, ACCOUNTFILE));
        }else{

        }
    }


    private void applyToken(String token){
        HashMap<String, String> map = new HashMap();
        JSONObject json = new JSONObject();
        try {
            json.put("token", token);
            json.put("type", "android");
            json.put("version",Utils.getAppVersionCode()+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("token", json.toString());
        RequestManager.getInstance().setHeader(map);
        Utils.Log("setToken:" + json.toString());
    }


    public void userRegister(String name,String tel,String password,String verify,StatusCallback callback){
        RequestMap params = new RequestMap();
        params.put("name",name);
        params.put("tel",tel);
        params.put("pass",Utils.MD5(password.getBytes()));
        params.put("code", verify);
        RequestManager.getInstance().post(API.URL.Register,params,callback);
    }

    public void isRegistered(String tel,StatusCallback callback){
        RequestMap params = new RequestMap();
        params.put("tel",tel);
        RequestManager.getInstance().post(API.URL.IsRegistered,params,callback);
    }

    public void userLogin(String tel,String password,StatusCallback callback){
        RequestMap params = new RequestMap();
        params.put("tel", tel);
        params.put("pass", Utils.MD5(password.getBytes()));
        RequestManager.getInstance().post(API.URL.Login, params, callback.add(new DataCallback<UserAccountData>() {
            @Override
            public void success(String info, UserAccountData data) {
                setUserAccountData(data);
            }

            @Override
            public void error(String errorInfo) {

            }
        }));
    }

    public void modifyPassword(String tel,String password,String verify,StatusCallback callback){
        RequestMap params = new RequestMap();
        params.put("tel",tel);
        params.put("newP",Utils.MD5(password.getBytes()));
        params.put("code",verify);
        RequestManager.getInstance().post(API.URL.ModifyPassword, params, callback);
    }


}
