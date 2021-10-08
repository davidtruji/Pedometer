package com.dtsoftware.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent bootIntent = new Intent(context, StepsCounterService.class);
        context.startService(bootIntent);
    }
}
