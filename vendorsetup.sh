# 1. Apply Git Changes (Source Modification)
echo "Applying source modifications..."

# Udfps patching for duchamp
echo "Patching udfps in duchamp frameworks/base..."
cd frameworks/base
git fetch https://github.com/The-Clover-Project/frameworks_base 15-qpr2-peridot
git cherry-pick 830933794473c660dd50b8f0daf9dd29e0fa9fb9 e7eb9e4a46c76062d9af59cacac43b4479b7748d
cd ../../

# Nuke refresh rate selector
echo "Nuking refresh rate selector so no one mess with it..."
cd  packages/apps/Settings
git fetch https://github.com/zenin1504/packages_apps_Settings
git cherry-pick 1b4f825efb744efa185f0af588f39e47837371c0
cd ../../../

# Hardware Compatibility Fixes
echo "Fetching and applying hardware compatibility fixes for hardware/lineage/compat..."
cd hardware/lineage/compat
git fetch https://github.com/xiaomi-mt6897-duchamp/android_hardware_lineage_compat
git cherry-pick 9a046ea7e5899adc38ab04fe24eb34859fe4d779
cd ../../../

# Hardware Legacy Modifications
echo "Fetching and applying fixes for hardware/libhardware_legacy..."
cd hardware/libhardware_legacy
git fetch https://android.googlesource.com/platform/hardware/libhardware_legacy 54bb5d03278152e696c7bff4607278790ac73057
git cherry-pick 54bb5d03278152e696c7bff4607278790ac73057
cd ../../

# Add WPA3 fix patch
echo "Patching WPA3 to work on duchamp device..."
cd external/wpa_supplicant_8
git fetch https://github.com/xiaomi-mt6897-duchamp/android_external_wpa_supplicant_8
git cherry-pick cc88629c6c5c2c2353bf87efef9b5c9c5bf32bee
cd ../../

echo "All operations completed successfully!"
