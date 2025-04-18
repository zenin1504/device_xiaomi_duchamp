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

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import vendor.xiaomi.hw.touchfeature.ITouchFeature;

public class AodFodUtils {
    private static final String KEY_ENABLED    = "aod_fod_enabled";
    private static final int    MODE_FOD       = 10;
    private static final int    MODE_AOD       = 11;
    private static final int    MODE_FOD_ICON  = 16;
    private static ITouchFeature mTouchFeature;

    public static void setAodFodEnabled(Context ctx, boolean on) {
        Settings.Secure.putInt(
            ctx.getContentResolver(), KEY_ENABLED, on ? 1 : 0
        );
        try {
            if (mTouchFeature == null) {
                IBinder b = android.os.ServiceManager.waitForDeclaredService(
                    ITouchFeature.DESCRIPTOR + "/default"
                );
                mTouchFeature = ITouchFeature.Stub.asInterface(b);
            }
            mTouchFeature.setTouchMode(0, MODE_FOD,      on ? 1 : 0);
            mTouchFeature.setTouchMode(0, MODE_AOD,      on ? 1 : 0);
            mTouchFeature.setTouchMode(0, MODE_FOD_ICON, on ? 1 : 0);
        } catch (Exception ignored) { }

        Intent svc = new Intent(ctx, AodFodService.class);
        if (on) {
            ctx.startService(svc);
        } else {
            ctx.stopService(svc);
        }
    }

    public static boolean isEnabled(Context ctx) {
        return Settings.Secure.getInt(
            ctx.getContentResolver(), KEY_ENABLED, 0
        ) == 1;
    }

    public static void restoreState(Context ctx) {
        setAodFodEnabled(ctx, isEnabled(ctx));
    }
}
