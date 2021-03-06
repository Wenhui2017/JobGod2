package com.ant.jobgod.jobgod.module.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ant.jobgod.jobgod.model.AccountModel;
import com.ant.jobgod.jobgod.module.main.UserMainActivity;
import com.jude.beam.bijection.Presenter;

/**
 * Created by Mr.Jude on 2015/7/1.
 */
public class LaunchPresenter extends Presenter<LaunchActivity> {
    @Override
    protected void onCreate(LaunchActivity view,Bundle savedState) {
        super.onCreate(view,savedState);
        new Handler().postDelayed(() -> {
                getView().startActivity(new Intent(getView(), AccountModel.getInstance().isUser() ? UserMainActivity.class : UserMainActivity.class));
                getView().finish();
        },0);

    }
}
