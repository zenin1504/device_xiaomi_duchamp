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
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.lineageos.settings.R;

public class AodFodOverlayView extends FrameLayout {
    private static final String KEY_POS_X      = "fod_pos_x";
    private static final String KEY_POS_Y      = "fod_pos_y";
    private static final String KEY_SHOW_ICON  = "show_icon";
    private static final int DEFAULT_POS_X     = 555;
    private static final int DEFAULT_POS_Y     = 2437;
    private static final int DEFAULT_SHOW      = 1;

    private final ImageView mIcon;

    public AodFodOverlayView(Context context) {
        super(context);
        inflate(context, R.layout.aod_fod_view, this);
        setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_FULLSCREEN
          | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        mIcon = findViewById(R.id.fod_icon);

        int x = Settings.Secure.getInt(
            context.getContentResolver(), KEY_POS_X, DEFAULT_POS_X
        );
        int y = Settings.Secure.getInt(
            context.getContentResolver(), KEY_POS_Y, DEFAULT_POS_Y
        );

        FrameLayout.LayoutParams lp =
            (FrameLayout.LayoutParams) mIcon.getLayoutParams();
        lp.leftMargin = x;
        lp.topMargin  = y;
        mIcon.setLayoutParams(lp);

        // initial show/hide
        int show = Settings.Secure.getInt(
            context.getContentResolver(), KEY_SHOW_ICON, DEFAULT_SHOW
        );
        mIcon.setVisibility(show == 1
            ? View.VISIBLE
            : View.GONE);
    }

    public WindowManager.LayoutParams getLayoutParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_SECURE_SYSTEM_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
          | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
          | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
          | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
          | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
          | WindowManager.LayoutParams.FLAG_FULLSCREEN,
            PixelFormat.TRANSLUCENT
        );
        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
        params.setTitle("AodFodOverlay");
        return params;
    }

    public void hideIcon() {
        mIcon.setVisibility(View.GONE);
    }

    public void showIcon() {
        mIcon.setVisibility(View.VISIBLE);
    }
}
