package com.ant.jobgod.jobgod.module.launch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.ant.jobgod.jobgod.app.BasePresenter;
import com.ant.jobgod.jobgod.model.AccountModel;
import com.ant.jobgod.jobgod.model.callback.StatusCallback;
import com.ant.jobgod.jobgod.util.Utils;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.login.Loginable;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mr.Jude on 2015/6/6.
 */
public class UserLoginPresenter extends BasePresenter<UserLoginActivity> implements Loginable{
    private static final int REGISTER = 1243;

    UMSocialService mController;

    private CommUser user;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getView(), "1104599783",
                "PeKfWv4imECnmEGn");
        qqSsoHandler.addToSocialSDK();
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        UMWXHandler wxHandler = new UMWXHandler(getView(),"appId","appSecret");
        wxHandler.addToSocialSDK();
    }




    public void login(String tel,String password){
        getView().showProgress("登录中");
        AccountModel.getInstance().userLogin(tel, password, new StatusCallback() {

            @Override
            public void result(int status, String info) {
                getView().dismissProgress();
                switch (status) {
                    case 202:
                        getView().setNumberError();
                        break;
                    case 203:
                        getView().setPasswordError();
                        break;
                }
            }

            @Override
            public void success(String info) {
                login(getView(), new LoginListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(int i, CommUser commUser) {

                    }
                });
                getView().finish();
            }


        });
    }



    public void loginByQQ(){
        mController.doOauthVerify(getView(), SHARE_MEDIA.QQ, new SocializeListeners.UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                final String uid = value.getString("uid");
                mController.getPlatformInfo(getView(), SHARE_MEDIA.QQ, new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                        getView().showProgress("登录中");
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == 200 && info != null) {
                            AccountModel.getInstance().userLoginThroughQQ(uid, (String) info.get("profile_image_url"), (String) info.get("screen_name"), new StatusCallback() {
                                @Override
                                public void success(String info) {
                                    getView().finish();
                                }

                                @Override
                                public void result(int status, String info) {
                                    getView().dismissProgress();
                                }
                            });
                        } else {
                            Utils.Toast("发生错误：" + status);
                        }
                    }
                });
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }

            @Override
            public void onStart(SHARE_MEDIA platform) {
            }
        });
    }

    public void loginByWeiChat(){
        mController.doOauthVerify(getView(), SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                final String uid = value.getString("uid");
                mController.getPlatformInfo(getView(), SHARE_MEDIA.QQ, new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                        getView().showProgress("登录中");
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == 200 && info != null) {
                            StringBuilder sb = new StringBuilder();
                            Set<String> keys = info.keySet();
                            for (String key : keys) {
                                sb.append(key + "=" + info.get(key).toString() + "\r\n");
                            }
                            Utils.Log("TestData", sb.toString());
                        } else {
                            Utils.Log("TestData", "发生错误：" + status);
                        }
//
//                        if (status == 200 && info != null) {
//                            AccountModel.getInstance().userLoginThroughQQ(uid, (String) info.get("profile_image_url"), (String) info.get("screen_name"), new StatusCallback() {
//                                @Override
//                                public void success(String info) {
//                                    getView().finish();
//                                }
//
//                                @Override
//                                public void result(int status, String info) {
//                                    getView().dismissProgress();
//                                }
//                            });
//                        } else {
//                            Utils.Toast("发生错误：" + status);
//                        }
                    }
                });
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }

            @Override
            public void onStart(SHARE_MEDIA platform) {
            }
        });
    }
    public void loginBySina(){
        mController.doOauthVerify(getView(), SHARE_MEDIA.SINA, new SocializeListeners.UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                    Utils.Toast("授权成功:" + value.getString("uid"));
                } else {
                    Utils.Toast("授权失败");
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }

            @Override
            public void onStart(SHARE_MEDIA platform) {
            }
        });
    }
    public void gotoBiz(){
        Utils.Toast("未开发");
    }

    public void register(){
        getView().startActivityForResult(new Intent(getView(), UserRegisterActivity.class), REGISTER);
    }
    public void modifyPassword(){
        getView().startActivity(new Intent(getView(), ModifyPasswordActivity.class));
    }

    @Override
    protected void onResult(int requestCode, int resultCode, Intent data) {
        super.onResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if (requestCode == REGISTER && resultCode == Activity.RESULT_OK){
            String number = data.getStringExtra("number");
            String password = data.getStringExtra("password");
            if (number!=null&&password!=null)
                getView().setNumberAndPassword(number,password);
        }

    }


    @Override
    public void login(Context context, LoginListener loginListener) {
        user = new CommUser(AccountModel.getInstance().getUserAccount().getDetail().getId() + "");
        user.name = AccountModel.getInstance().getUserAccount().getDetail().getName();
        user.iconUrl = AccountModel.getInstance().getUserAccount().getDetail().getFace();
        user.source = com.umeng.comm.core.beans.Source.SINA;
        if (AccountModel.getInstance().getUserAccount().getDetail().getGender() == 0) {
            user.gender = CommUser.Gender.FEMALE;
        } else
            user.gender = CommUser.Gender.MALE;
        loginListener.onComplete(200, user);
    }

    @Override
    public void logout(Context context, LoginListener loginListener) {

    }

    @Override
    public boolean isLogined(Context context) {
        return false;
    }

}
