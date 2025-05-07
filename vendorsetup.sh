# 1. Apply Git Changes (Source Modification)
echo "Applying source modifications..."

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

# Nuke refresh rate selector
echo "Nuking refresh rate selector so no one mess with it..."
cd  packages/apps/Settings
git fetch https://github.com/zenin1504/android_packages_apps_Settings
git cherry-pick a657aaded67d201115194acf88c349dad4fd8ffd
cd ../../../

# Add WPA3 fix patch
echo "Patching WPA3 to work on duchamp device..."
cd external/wpa_supplicant_8
git fetch https://github.com/xiaomi-mt6897-duchamp/android_external_wpa_supplicant_8
git cherry-pick cc88629c6c5c2c2353bf87efef9b5c9c5bf32bee
cd ../../

echo "All operations completed successfully!"
