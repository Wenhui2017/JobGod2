package com.ant.jobgod.jobgod.model.callback;

import com.android.http.RequestManager;
import com.ant.jobgod.jobgod.util.Utils;

/**
 * Created by Mr.Jude on 2015/6/13.
 */
class LinkCallback implements RequestManager.RequestListener {
    private LinkCallback link;
    public LinkCallback add(LinkCallback other){
        other.setLink(this);
        return other;
    }
    private void setLink(LinkCallback link){
        this.link = link;
    }

    @Override
    public void onRequest() {
        if (link != null)
        link.onRequest();
    }

    @Override
    public void onSuccess(String s) {
        Utils.Log("fuck");
        if (link != null)
        link.onSuccess(s);
    }

    @Override
    public void onError(String s) {
        Utils.Log("error");
        if (link != null)
        link.onError(s);
    }
}
