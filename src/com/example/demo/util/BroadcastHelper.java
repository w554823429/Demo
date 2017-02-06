package com.example.demo.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class BroadcastHelper {
	public static boolean canResolveBroadcast(Context context, Intent intent) {
		PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
		return receivers != null && receivers.size() > 0;
	}
}
