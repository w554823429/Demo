package com.example.demo.impl;

import java.util.Collections;
import java.util.List;

import com.example.demo.shortcutbadger.Badger;
import com.example.demo.shortcutbadger.ShortcutBadgeException;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by wuxuejian on 2016/10/9.
 * �������� -- ֪ͨ��״̬�� -- Ӧ�ýǱ���� �п���Ӧ��
 */
public class ZukHomeBadger implements Badger {

    private final Uri CONTENT_URI = Uri.parse("content://com.android.badge/badge");

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws ShortcutBadgeException {
        Bundle extra = new Bundle();
        extra.putInt("app_badge_count", badgeCount);
        context.getContentResolver().call(CONTENT_URI, "setAppBadgeCount", null, extra);
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Collections.singletonList("com.zui.launcher");
    }
}
