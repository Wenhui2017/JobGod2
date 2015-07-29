package com.ant.jobgod.jobgod.module.job;

import android.content.Intent;
import android.os.Bundle;

import com.ant.jobgod.jobgod.app.BasePresenter;
import com.ant.jobgod.jobgod.model.AccountModel;
import com.ant.jobgod.jobgod.model.JobModel;
import com.ant.jobgod.jobgod.model.bean.JobDetail;
import com.ant.jobgod.jobgod.model.callback.DataCallback;
import com.ant.jobgod.jobgod.model.callback.StatusCallback;
import com.ant.jobgod.jobgod.module.launch.UserLoginActivity;
import com.ant.jobgod.jobgod.util.Utils;

/**
 * Created by alien on 2015/7/10.
 */
public class JobDetailReleasePresenter extends BasePresenter<JobDetailReleaseActivity> {

    private int id;
    private JobDetail mJob;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        id = getView().getIntent().getIntExtra("id",0);
        JobModel.getInstance().getJobDetail(id, new DataCallback<JobDetail>() {
            @Override
            public void success(String info, JobDetail data) {
                getView().setData(mJob = data);
                getView().setRelateJobData(data.getRelative());
            }
        });
    }

    /**
     * 收藏兼职
     */
    public void collect(){
        if(AccountModel.getInstance().getUserAccount()==null){
            Utils.Toast("请先登录");
            getView().startActivity(new Intent(getView(), UserLoginActivity.class));
            return;
        }
        if (mJob.isCollected())
            JobModel.getInstance().unCollect(id, new StatusCallback() {
                @Override
                public void success(String info) {
                    Utils.Toast("取消收藏");
                    getView().setIsCollected(!mJob.isCollected());
                    }
            });
        else
            JobModel.getInstance().collect(id, new StatusCallback() {
                @Override
                public void success(String info) {
                    Utils.Toast("收藏成功");
                    mJob.setCollected(!mJob.isCollected());
                }
            });

    }

    @Override
    protected void onCreateView(JobDetailReleaseActivity view) {
        super.onCreateView(view);
        if (mJob!=null) getView().setData(mJob);

    }

    public void toCommentActivity(){
        Intent intent=new Intent(getView(),CommentActivity.class);
        intent.putExtra("id",id);
        getView().startActivity(intent);
    }
}
