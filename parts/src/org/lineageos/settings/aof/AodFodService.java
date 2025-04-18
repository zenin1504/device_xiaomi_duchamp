/*
 * Copyright (C) 2025 kenway214
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.aof;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.WindowManager;

public class AodFodService extends Service {
    private AodFodOverlayView mOverlayView;
    private WindowManager      mWindowManager;
    private Handler            mHandler;
    private Runnable           mHideRunnable;

    private final BroadcastReceiver mScreenReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctx, Intent intent) {
            String a = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(a)) {
                showOverlay();
                setAodState(true);
            } else {
                removeOverlay();
                setAodState(false);
            }
        }
    };

    @Override
    public void onCreate() {
        mHandler = new Handler(Looper.getMainLooper());
        mHideRunnable = () -> {
            if (mOverlayView != null) {
                mOverlayView.hideIcon();
            }
        };

        IntentFilter f = new IntentFilter();
        f.addAction(Intent.ACTION_SCREEN_OFF);
        f.addAction(Intent.ACTION_SCREEN_ON);
        f.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenReceiver, f);

        // if already off at startup
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm != null && !pm.isInteractive()) {
            showOverlay();
            setAodState(true);
        }
    }

    private void showOverlay() {
        if (mOverlayView != null) return;
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mOverlayView    = new AodFodOverlayView(this);
        mWindowManager.addView(
            mOverlayView, mOverlayView.getLayoutParams()
        );

        // schedule auto-hide
        int autoHide = Settings.Secure.getInt(
            getContentResolver(), "auto_hide_icon", 0
        );
        if (autoHide == 1) {
            int mins = Settings.Secure.getInt(
                getContentResolver(), "auto_hide_delay", 2
            );
            mHandler.postDelayed(
                mHideRunnable,
                mins * 60 * 1000L
            );
        }
    }

    private void removeOverlay() {
        if (mOverlayView == null) return;
        mHandler.removeCallbacks(mHideRunnable);
        mWindowManager.removeView(mOverlayView);
        mOverlayView = null;
    }

    private void setAodState(boolean on) {
        Settings.Secure.putInt(
            getContentResolver(), "doze_always_on", on ? 1 : 0
        );
        sendBroadcast(new Intent("com.android.systemui.doze.pulse"));
    }

    @Override
    public void onDestroy() {
        removeOverlay();
        setAodState(false);
        unregisterReceiver(mScreenReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
