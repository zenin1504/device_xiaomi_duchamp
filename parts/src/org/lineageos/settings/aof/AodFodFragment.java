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

import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import org.lineageos.settings.R;

public class AodFodFragment extends PreferenceFragmentCompat {
    private static final String KEY_AOD_FOD      = "aod_fod";
    private static final String KEY_POS_X        = "fod_pos_x";
    private static final String KEY_POS_Y        = "fod_pos_y";
    private static final String KEY_SHOW_ICON    = "show_icon";
    private static final String KEY_AUTO_HIDE    = "auto_hide_icon";
    private static final String KEY_HIDE_DELAY   = "auto_hide_delay";

    private static final int DEFAULT_POS_X     = 555;
    private static final int DEFAULT_POS_Y     = 2437;
    private static final int DEFAULT_SHOW_ICON = 1;
    private static final int DEFAULT_AUTO_HIDE = 0;
    private static final int DEFAULT_DELAY_MIN = 2;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.aod_fod_settings, rootKey);

        // Main toggle
        SwitchPreferenceCompat mainSwitch = findPreference(KEY_AOD_FOD);
        if (mainSwitch != null) {
            mainSwitch.setChecked(
                Settings.Secure.getInt(
                    requireContext().getContentResolver(),
                    KEY_AOD_FOD, 0) == 1
            );
            mainSwitch.setOnPreferenceChangeListener((pref, newVal) -> {
                boolean on = (Boolean) newVal;
                Settings.Secure.putInt(
                    requireContext().getContentResolver(),
                    KEY_AOD_FOD, on ? 1 : 0
                );
                AodFodUtils.setAodFodEnabled(requireContext(), on);
                return true;
            });
        }

        // FOD Icon Customization Category
        // 1) Position X
        EditTextPreference prefX = findPreference(KEY_POS_X);
        if (prefX != null) {
            int x = Settings.Secure.getInt(
                requireContext().getContentResolver(),
                KEY_POS_X, DEFAULT_POS_X
            );
            prefX.setText(String.valueOf(x));
            prefX.setOnPreferenceChangeListener((pref, newVal) -> {
                try {
                    int v = Integer.parseInt((String) newVal);
                    Settings.Secure.putInt(
                        requireContext().getContentResolver(),
                        KEY_POS_X, v
                    );
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            });
        }

        // 2) Position Y
        EditTextPreference prefY = findPreference(KEY_POS_Y);
        if (prefY != null) {
            int y = Settings.Secure.getInt(
                requireContext().getContentResolver(),
                KEY_POS_Y, DEFAULT_POS_Y
            );
            prefY.setText(String.valueOf(y));
            prefY.setOnPreferenceChangeListener((pref, newVal) -> {
                try {
                    int v = Integer.parseInt((String) newVal);
                    Settings.Secure.putInt(
                        requireContext().getContentResolver(),
                        KEY_POS_Y, v
                    );
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            });
        }

        // 3) Show/Hide Icon
        SwitchPreferenceCompat showIcon = findPreference(KEY_SHOW_ICON);
        if (showIcon != null) {
            showIcon.setChecked(
                Settings.Secure.getInt(
                    requireContext().getContentResolver(),
                    KEY_SHOW_ICON, DEFAULT_SHOW_ICON) == 1
            );
            showIcon.setOnPreferenceChangeListener((pref, newVal) -> {
                boolean on = (Boolean) newVal;
                Settings.Secure.putInt(
                    requireContext().getContentResolver(),
                    KEY_SHOW_ICON, on ? 1 : 0
                );
                return true;
            });
        }

        // 4) Auto-hide toggle
        SwitchPreferenceCompat autoHide = findPreference(KEY_AUTO_HIDE);
        if (autoHide != null) {
            autoHide.setChecked(
                Settings.Secure.getInt(
                    requireContext().getContentResolver(),
                    KEY_AUTO_HIDE, DEFAULT_AUTO_HIDE) == 1
            );
            autoHide.setOnPreferenceChangeListener((pref, newVal) -> {
                boolean on = (Boolean) newVal;
                Settings.Secure.putInt(
                    requireContext().getContentResolver(),
                    KEY_AUTO_HIDE, on ? 1 : 0
                );
                return true;
            });
        }

        // 5) Auto-hide delay list
        ListPreference hideDelay = findPreference(KEY_HIDE_DELAY);
        if (hideDelay != null) {
            int delay = Settings.Secure.getInt(
                requireContext().getContentResolver(),
                KEY_HIDE_DELAY, DEFAULT_DELAY_MIN
            );
            hideDelay.setValue(String.valueOf(delay));
            hideDelay.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
            hideDelay.setOnPreferenceChangeListener((pref, newVal) -> {
                Settings.Secure.putInt(
                    requireContext().getContentResolver(),
                    KEY_HIDE_DELAY, Integer.parseInt((String) newVal)
                );
                return true;
            });
        }
    }
}
