/**
 * Summary: Ӧ����ʹ�õ�WebChromeClient����
 * Version 1.0
 * Date: 13-11-8
 * Time: ����2:31
 * Copyright: Copyright (c) 2013
 */

package com.mayu.SafeWebViewBridge;

import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class InjectedChromeClient extends WebChromeClient {
    private final String TAG = "InjectedChromeClient";
    private JsCallJava mJsCallJava;
    private boolean mIsInjectedJS;

    public InjectedChromeClient (String injectedName, Class injectedCls) {
        mJsCallJava = new JsCallJava(injectedName, injectedCls);
    }

    public InjectedChromeClient (JsCallJava jsCallJava) {
        mJsCallJava = jsCallJava;
    }

    // ����Alert�¼�
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        result.confirm();
        return true;
    }

    @Override
    public void onProgressChanged (WebView view, int newProgress) {
        //ΪʲôҪ������ע��JS
        //1 OnPageStarted��ע���п���ȫ��ע�벻�ɹ�������ҳ��ű������нӿ��κ�ʱ�򶼲�����
        //2 OnPageFinished��ע�룬��Ȼ��󶼻�ȫ��ע��ɹ����������ʱ���п���̫����ҳ���ڳ�ʼ�����ýӿں���ʱ��ȴ�ʱ�����
        //3 �ڽ��ȱ仯ʱע�룬�պÿ������������������еõ�һ�����д���
        //Ϊʲô�ǽ��ȴ���25%�Ž���ע�룬��Ϊ�Ӳ��Կ���ֻ�н��ȴ����������ҳ��������õ����ˢ�¼��أ���֤100%ע��ɹ�
        if (newProgress <= 25) {
            mIsInjectedJS = false;
        } else if (!mIsInjectedJS) {
            view.loadUrl(mJsCallJava.getPreloadInterfaceJS());
            mIsInjectedJS = true;
            Log.d(TAG, " inject js interface completely on progress " + newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        result.confirm(mJsCallJava.call(view, message));
        return true;
    }
}
