/*
 * Copyright (C) 2024 Paranoid Android
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.lineageos.settings.thermal

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import org.lineageos.settings.R
import org.lineageos.settings.utils.FileUtils

class ThermalProfileTileService : TileService() {
    companion object {
        private const val THEMRAL_PROFILE_PATH = "/sys/class/thermal/thermal_message/sconfig"
        private const val THEMRAL_PROFILE_DEFAULT = 0
        private const val THEMRAL_PROFILE_MPERFORMANCE = 6
        private const val THEMRAL_PROFILE_MBATTERY = 1
        private const val THEMRAL_PROFILE_MGAME = 19
    }

    private fun updateUI(profile: Int) {
        val tile = qsTile
        tile.label = getString(R.string.thermalprofile_title)
        tile.subtitle = when (profile) {
            THEMRAL_PROFILE_DEFAULT -> getString(R.string.thermalprofile_default)
            THEMRAL_PROFILE_MPERFORMANCE -> getString(R.string.thermalprofile_performance)
            THEMRAL_PROFILE_MBATTERY -> getString(R.string.thermalprofile_battery)
            THEMRAL_PROFILE_MGAME -> getString(R.string.thermalprofile_game)
            else -> getString(R.string.thermalprofile_unknown)
        }
        tile.state = Tile.STATE_ACTIVE
        tile.updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        updateUI(FileUtils.readLineInt(THEMRAL_PROFILE_PATH))
    }

    override fun onStopListening() {
        super.onStopListening()
    }

    override fun onClick() {
        super.onClick()
        val currentThermalProfile = FileUtils.readLineInt(THEMRAL_PROFILE_PATH)
        val newThermalProfile = when (currentThermalProfile) {
            THEMRAL_PROFILE_DEFAULT -> THEMRAL_PROFILE_MPERFORMANCE
            THEMRAL_PROFILE_MPERFORMANCE -> THEMRAL_PROFILE_MBATTERY
            THEMRAL_PROFILE_MBATTERY -> THEMRAL_PROFILE_MGAME
            THEMRAL_PROFILE_MGAME -> THEMRAL_PROFILE_DEFAULT
            else -> THEMRAL_PROFILE_DEFAULT
        }
        FileUtils.writeLine(THEMRAL_PROFILE_PATH, newThermalProfile)
        updateUI(newThermalProfile)
    }
}
