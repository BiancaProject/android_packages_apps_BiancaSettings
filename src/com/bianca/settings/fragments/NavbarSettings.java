/*
 * Copyright (C) 2018 The Superior OS Project
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

package com.bianca.settings.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.Handler;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;

import com.android.internal.util.bianca.BiancaUtils;

import com.android.settings.R;
import com.bianca.support.preferences.SystemSettingSwitchPreference;
import com.bianca.support.preferences.SecureSettingSwitchPreference;
import com.bianca.support.preferences.SystemSettingListPreference;


public class NavbarSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_NAVIGATION_BAR_ENABLED = "force_show_navbar";
    private static final String KEY_SWAP_NAVIGATION_KEYS = "swap_navigation_keys";
    private static final String KEY_GESTURE_SYSTEM = "gesture_system_navigation";
    private static final String KEY_LAYOUT_SETTINGS = "layout_settings";

    private static final String KEY_BACK_LONG_PRESS_ACTION = "back_key_long_press";
    private static final String KEY_BACK_LONG_PRESS_CUSTOM_APP = "back_key_long_press_custom_app";
    private static final String KEY_BACK_DOUBLE_TAP_ACTION = "back_key_double_tap";
    private static final String KEY_BACK_DOUBLE_TAP_CUSTOM_APP = "back_key_double_tap_custom_app";
    private static final String KEY_HOME_LONG_PRESS_ACTION = "home_key_long_press";
    private static final String KEY_HOME_LONG_PRESS_CUSTOM_APP = "home_key_long_press_custom_app";
    private static final String KEY_HOME_DOUBLE_TAP_ACTION = "home_key_double_tap";
    private static final String KEY_HOME_DOUBLE_TAP_CUSTOM_APP = "home_key_double_tap_custom_app";
    private static final String KEY_APP_SWITCH_LONG_PRESS = "app_switch_key_long_press";
    private static final String KEY_APP_SWITCH_LONG_PRESS_CUSTOM_APP = "app_switch_key_long_press_custom_app";
    private static final String KEY_APP_SWITCH_DOUBLE_TAP = "app_switch_key_double_tap";
    private static final String KEY_APP_SWITCH_DOUBLE_TAP_CUSTOM_APP = "app_switch_key_double_tap_custom_app";
    private static final String KEY_MENU_LONG_PRESS_ACTION = "menu_key_long_press";
    private static final String KEY_MENU_DOUBLE_TAP_ACTION = "menu_key_double_tap";
    private static final String KEY_CAMERA_LONG_PRESS_ACTION = "camera_key_long_press";
    private static final String KEY_CAMERA_DOUBLE_TAP_ACTION = "camera_key_double_tap";
    private static final String KEY_ASSIST_LONG_PRESS_ACTION = "assist_key_long_press";
    private static final String KEY_ASSIST_DOUBLE_TAP_ACTION = "assist_key_double_tap";

    // Edge gesture action
    private static final String KEY_LONG_BACK_SWIPE_TIMEOUT = "long_back_swipe_timeout";
    private static final String KEY_BACK_SWIPE_EXTENDED = "back_swipe_extended";
    private static final String KEY_BACK_GESTURE_HAPTIC = "back_gesture_haptic";
    private static final String KEY_LEFT_SWIPE_ACTIONS = "left_swipe_actions";
    private static final String KEY_RIGHT_SWIPE_ACTIONS = "right_swipe_actions";
    private static final String KEY_LEFT_SWIPE_APP_ACTION = "left_swipe_app_action";
    private static final String KEY_RIGHT_SWIPE_APP_ACTION = "right_swipe_app_action";
    private static final String KEY_LEFT_VERTICAL_SWIPE_ACTIONS = "left_vertical_swipe_actions";
    private static final String KEY_RIGHT_VERTICAL_SWIPE_ACTIONS = "right_vertical_swipe_actions";
    private static final String KEY_LEFT_VERTICAL_SWIPE_APP_ACTION = "left_vertical_swipe_app_action";
    private static final String KEY_RIGHT_VERTICAL_SWIPE_APP_ACTION = "right_vertical_swipe_app_action";

    private static final String KEY_CATEGORY_HOME          = "home_key";
    private static final String KEY_CATEGORY_BACK          = "back_key";
    private static final String KEY_CATEGORY_MENU          = "menu_key";
    private static final String KEY_CATEGORY_ASSIST        = "assist_key";
    private static final String KEY_CATEGORY_APP_SWITCH    = "app_switch_key";
    private static final String KEY_CATEGORY_CAMERA        = "camera_key";
    private static final String KEY_CATEGORY_LEFT_SWIPE    = "left_swipe";
    private static final String KEY_CATEGORY_RIGHT_SWIPE   = "right_swipe";
    private static final String KEY_CATEGORY_LEFT_VERTICAL_SWIPE    = "left_vertical_swipe";
    private static final String KEY_CATEGORY_RIGHT_VERTICAL_SWIPE   = "right_vertical_swipe";

    private static final int KEY_MASK_HOME = 0x01;
    private static final int KEY_MASK_BACK = 0x02;
    private static final int KEY_MASK_MENU = 0x04;
    private static final int KEY_MASK_ASSIST = 0x08;
    private static final int KEY_MASK_APP_SWITCH = 0x10;
    private static final int KEY_MASK_CAMERA = 0x20;

    private ListPreference mBackLongPress;
    private ListPreference mBackDoubleTap;
    private ListPreference mHomeLongPress;
    private ListPreference mHomeDoubleTap;
    private ListPreference mAppSwitchLongPress;
    private ListPreference mAppSwitchDoubleTap;
    private ListPreference mMenuLongPress;
    private ListPreference mMenuDoubleTap;
    private ListPreference mCameraLongPress;
    private ListPreference mCameraDoubleTap;
    private ListPreference mAssistLongPress;
    private ListPreference mAssistDoubleTap;
    private ListPreference mLeftSwipeActions;
    private ListPreference mRightSwipeActions;
    private ListPreference mLeftVerticalSwipeActions;
    private ListPreference mRightVerticalSwipeActions;
    private SystemSettingListPreference mTimeout;

    private SwitchPreference mNavigationBar;
    private SystemSettingSwitchPreference mSwapHardwareKeys;
    private SystemSettingSwitchPreference mNavigationArrowKeys;
    private SystemSettingSwitchPreference mExtendedSwipe;
    private SystemSettingSwitchPreference mBackGesture;

    private PreferenceCategory homeCategory;
    private PreferenceCategory backCategory;
    private PreferenceCategory menuCategory;
    private PreferenceCategory assistCategory;
    private PreferenceCategory appSwitchCategory;
    private PreferenceCategory cameraCategory;
    private PreferenceCategory leftSwipeCategory;
    private PreferenceCategory rightSwipeCategory;
    private PreferenceCategory leftVerticalSwipeCategory;
    private PreferenceCategory rightVerticalSwipeCategory;

    private Preference mAppSwitchLongPressCustomApp;
    private Preference mAppSwitchDoubleTapCustomApp;
    private Preference mBackLongPressCustomApp;
    private Preference mBackDoubleTapCustomApp;
    private Preference mHomeLongPressCustomApp;
    private Preference mHomeDoubleTapCustomApp;
    private Preference mGestureSystemNavigation;
    private Preference mLayoutSettings;
    private Preference mLeftSwipeAppSelection;
    private Preference mRightSwipeAppSelection;
    private Preference mLeftVerticalSwipeAppSelection;
    private Preference mRightVerticalSwipeAppSelection;

    private int deviceKeys;

    private boolean mIsNavSwitchingMode = false;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.bianca_settings_navbar);
        final PreferenceScreen prefSet = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        deviceKeys = getResources().getInteger(
                com.android.internal.R.integer.config_deviceHardwareKeys);

        int backKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnBackKeyBehavior);
        int backKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnBackKeyBehavior);
        int homeKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnHomeKeyBehavior);
        int homeKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnHomeKeyBehavior);
        int AppSwitchKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnAppSwitchKeyBehavior);
        int AppSwitchKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnAppSwitchKeyBehavior);
        int MenuKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnMenuKeyBehavior);
        int MenuKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnMenuKeyBehavior);
        int CameraKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnCameraKeyBehavior);
        int CameraKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnCameraKeyBehavior);
        int AssistKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnAssistKeyBehavior);
        int AssistKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnAssistKeyBehavior);

        boolean hasMenu = (deviceKeys & KEY_MASK_MENU) != 0;
        boolean hasAssist = (deviceKeys & KEY_MASK_ASSIST) != 0;
        boolean hasCamera = (deviceKeys & KEY_MASK_CAMERA) != 0;

        homeCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_HOME);
        backCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_BACK);
        menuCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_MENU);
        assistCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_ASSIST);
        appSwitchCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_APP_SWITCH);
        cameraCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_CAMERA);
        leftSwipeCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_LEFT_SWIPE);
        rightSwipeCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_RIGHT_SWIPE);
        leftVerticalSwipeCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_LEFT_VERTICAL_SWIPE);
        rightVerticalSwipeCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_RIGHT_VERTICAL_SWIPE);

        mBackLongPressCustomApp = (Preference) findPreference(KEY_BACK_LONG_PRESS_CUSTOM_APP);
        mBackDoubleTapCustomApp = (Preference) findPreference(KEY_BACK_DOUBLE_TAP_CUSTOM_APP);
        mHomeLongPressCustomApp = (Preference) findPreference(KEY_HOME_LONG_PRESS_CUSTOM_APP);
        mHomeDoubleTapCustomApp = (Preference) findPreference(KEY_HOME_DOUBLE_TAP_CUSTOM_APP);
        mAppSwitchLongPressCustomApp = (Preference) findPreference(KEY_APP_SWITCH_LONG_PRESS_CUSTOM_APP);
        mAppSwitchDoubleTapCustomApp = (Preference) findPreference(KEY_APP_SWITCH_DOUBLE_TAP_CUSTOM_APP);

        mGestureSystemNavigation = (Preference) findPreference(KEY_GESTURE_SYSTEM);

        mSwapHardwareKeys = (SystemSettingSwitchPreference) findPreference(KEY_SWAP_NAVIGATION_KEYS);
        mBackGesture = (SystemSettingSwitchPreference) findPreference(KEY_BACK_GESTURE_HAPTIC);

        mLayoutSettings = (Preference) findPreference(KEY_LAYOUT_SETTINGS);
        if (BiancaUtils.isGestureNavbar()) {
            prefSet.removePreference(mLayoutSettings);
        }

        mNavigationBar = (SwitchPreference) findPreference(KEY_NAVIGATION_BAR_ENABLED);
        mNavigationBar.setChecked(isNavbarVisible());
        mNavigationBar.setOnPreferenceChangeListener(this);

        mBackLongPress = (ListPreference) findPreference(KEY_BACK_LONG_PRESS_ACTION);
        int backlongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_BACK_LONG_PRESS_ACTION, backKeyLongPress, UserHandle.USER_CURRENT);
        mBackLongPress.setValue(String.valueOf(backlongpress));
        mBackLongPress.setSummary(mBackLongPress.getEntry());
        mBackLongPress.setOnPreferenceChangeListener(this);

        mBackDoubleTap = (ListPreference) findPreference(KEY_BACK_DOUBLE_TAP_ACTION);
        int backdoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_BACK_DOUBLE_TAP_ACTION, backKeyDoubleTap, UserHandle.USER_CURRENT);
        mBackDoubleTap.setValue(String.valueOf(backdoubletap));
        mBackDoubleTap.setSummary(mBackDoubleTap.getEntry());
        mBackDoubleTap.setOnPreferenceChangeListener(this);

        mHomeLongPress = (ListPreference) findPreference(KEY_HOME_LONG_PRESS_ACTION);
        int homelongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_HOME_LONG_PRESS_ACTION, homeKeyLongPress, UserHandle.USER_CURRENT);
        mHomeLongPress.setValue(String.valueOf(homelongpress));
        mHomeLongPress.setSummary(mHomeLongPress.getEntry());
        mHomeLongPress.setOnPreferenceChangeListener(this);

        mHomeDoubleTap = (ListPreference) findPreference(KEY_HOME_DOUBLE_TAP_ACTION);
        int homedoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_HOME_DOUBLE_TAP_ACTION, homeKeyDoubleTap, UserHandle.USER_CURRENT);
        mHomeDoubleTap.setValue(String.valueOf(homedoubletap));
        mHomeDoubleTap.setSummary(mHomeDoubleTap.getEntry());
        mHomeDoubleTap.setOnPreferenceChangeListener(this);

        mAppSwitchLongPress = (ListPreference) findPreference(KEY_APP_SWITCH_LONG_PRESS);
        int appswitchlongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_APP_SWITCH_LONG_PRESS_ACTION, AppSwitchKeyLongPress, UserHandle.USER_CURRENT);
        mAppSwitchLongPress.setValue(String.valueOf(appswitchlongpress));
        mAppSwitchLongPress.setSummary(mAppSwitchLongPress.getEntry());
        mAppSwitchLongPress.setOnPreferenceChangeListener(this);

        mAppSwitchDoubleTap = (ListPreference) findPreference(KEY_APP_SWITCH_DOUBLE_TAP);
        int appswitchdoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_APP_SWITCH_DOUBLE_TAP_ACTION, AppSwitchKeyDoubleTap, UserHandle.USER_CURRENT);
        mAppSwitchDoubleTap.setValue(String.valueOf(appswitchdoubletap));
        mAppSwitchDoubleTap.setSummary(mAppSwitchDoubleTap.getEntry());
        mAppSwitchDoubleTap.setOnPreferenceChangeListener(this);

        mMenuLongPress = (ListPreference) findPreference(KEY_MENU_LONG_PRESS_ACTION);
        int menulongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_MENU_LONG_PRESS_ACTION, MenuKeyLongPress, UserHandle.USER_CURRENT);
        mMenuLongPress.setValue(String.valueOf(menulongpress));
        mMenuLongPress.setSummary(mMenuLongPress.getEntry());
        mMenuLongPress.setOnPreferenceChangeListener(this);

        mMenuDoubleTap = (ListPreference) findPreference(KEY_MENU_DOUBLE_TAP_ACTION);
        int menudoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_MENU_DOUBLE_TAP_ACTION, MenuKeyDoubleTap, UserHandle.USER_CURRENT);
        mMenuDoubleTap.setValue(String.valueOf(menudoubletap));
        mMenuDoubleTap.setSummary(mMenuDoubleTap.getEntry());
        mMenuDoubleTap.setOnPreferenceChangeListener(this);

        mCameraLongPress = (ListPreference) findPreference(KEY_CAMERA_LONG_PRESS_ACTION);
        int cameralongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_CAMERA_LONG_PRESS_ACTION, CameraKeyLongPress, UserHandle.USER_CURRENT);
        mCameraLongPress.setValue(String.valueOf(cameralongpress));
        mCameraLongPress.setSummary(mCameraLongPress.getEntry());
        mCameraLongPress.setOnPreferenceChangeListener(this);

        mCameraDoubleTap = (ListPreference) findPreference(KEY_CAMERA_DOUBLE_TAP_ACTION);
        int cameradoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_CAMERA_DOUBLE_TAP_ACTION, CameraKeyDoubleTap, UserHandle.USER_CURRENT);
        mCameraDoubleTap.setValue(String.valueOf(cameradoubletap));
        mCameraDoubleTap.setSummary(mCameraDoubleTap.getEntry());
        mCameraDoubleTap.setOnPreferenceChangeListener(this);

        mAssistLongPress = (ListPreference) findPreference(KEY_ASSIST_LONG_PRESS_ACTION);
        int assistlongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_ASSIST_LONG_PRESS_ACTION, AssistKeyLongPress, UserHandle.USER_CURRENT);
        mAssistLongPress.setValue(String.valueOf(assistlongpress));
        mAssistLongPress.setSummary(mAssistLongPress.getEntry());
        mAssistLongPress.setOnPreferenceChangeListener(this);

        mAssistDoubleTap = (ListPreference) findPreference(KEY_ASSIST_DOUBLE_TAP_ACTION);
        int assistdoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_ASSIST_DOUBLE_TAP_ACTION, AssistKeyDoubleTap, UserHandle.USER_CURRENT);
        mAssistDoubleTap.setValue(String.valueOf(assistdoubletap));
        mAssistDoubleTap.setSummary(mAssistDoubleTap.getEntry());
        mAssistDoubleTap.setOnPreferenceChangeListener(this);

        int leftSwipeActions = Settings.System.getIntForUser(resolver,
                Settings.System.LEFT_LONG_BACK_SWIPE_ACTION, 0,
                UserHandle.USER_CURRENT);
        mLeftSwipeActions = (ListPreference) findPreference(KEY_LEFT_SWIPE_ACTIONS);
        mLeftSwipeActions.setValue(Integer.toString(leftSwipeActions));
        mLeftSwipeActions.setSummary(mLeftSwipeActions.getEntry());
        mLeftSwipeActions.setOnPreferenceChangeListener(this);

        int rightSwipeActions = Settings.System.getIntForUser(resolver,
                Settings.System.RIGHT_LONG_BACK_SWIPE_ACTION, 0,
                UserHandle.USER_CURRENT);
        mRightSwipeActions = (ListPreference) findPreference(KEY_RIGHT_SWIPE_ACTIONS);
        mRightSwipeActions.setValue(Integer.toString(rightSwipeActions));
        mRightSwipeActions.setSummary(mRightSwipeActions.getEntry());
        mRightSwipeActions.setOnPreferenceChangeListener(this);

        mLeftSwipeAppSelection = (Preference) findPreference(KEY_LEFT_SWIPE_APP_ACTION);
        boolean isAppSelection = Settings.System.getIntForUser(resolver,
                Settings.System.LEFT_LONG_BACK_SWIPE_ACTION, 0, UserHandle.USER_CURRENT) == 5/*action_app_action*/;
        mLeftSwipeAppSelection.setEnabled(isAppSelection);

        mRightSwipeAppSelection = (Preference) findPreference(KEY_RIGHT_SWIPE_APP_ACTION);
        isAppSelection = Settings.System.getIntForUser(resolver,
                Settings.System.RIGHT_LONG_BACK_SWIPE_ACTION, 0, UserHandle.USER_CURRENT) == 5/*action_app_action*/;
        mRightSwipeAppSelection.setEnabled(isAppSelection);

        int leftVerticalSwipeActions = Settings.System.getIntForUser(resolver,
                Settings.System.LEFT_VERTICAL_BACK_SWIPE_ACTION, 0,
                UserHandle.USER_CURRENT);
        mLeftVerticalSwipeActions = (ListPreference) findPreference(KEY_LEFT_VERTICAL_SWIPE_ACTIONS);
        mLeftVerticalSwipeActions.setValue(Integer.toString(leftVerticalSwipeActions));
        mLeftVerticalSwipeActions.setSummary(mLeftVerticalSwipeActions.getEntry());
        mLeftVerticalSwipeActions.setOnPreferenceChangeListener(this);

        int rightVerticalSwipeActions = Settings.System.getIntForUser(resolver,
                Settings.System.RIGHT_VERTICAL_BACK_SWIPE_ACTION, 0,
                UserHandle.USER_CURRENT);
        mRightVerticalSwipeActions = (ListPreference) findPreference(KEY_RIGHT_VERTICAL_SWIPE_ACTIONS);
        mRightVerticalSwipeActions.setValue(Integer.toString(rightVerticalSwipeActions));
        mRightVerticalSwipeActions.setSummary(mRightVerticalSwipeActions.getEntry());
        mRightVerticalSwipeActions.setOnPreferenceChangeListener(this);

        mLeftVerticalSwipeAppSelection = (Preference) findPreference(KEY_LEFT_VERTICAL_SWIPE_APP_ACTION);
        isAppSelection = Settings.System.getIntForUser(resolver,
                Settings.System.LEFT_VERTICAL_BACK_SWIPE_ACTION, 0, UserHandle.USER_CURRENT) == 5/*action_app_action*/;
        mLeftVerticalSwipeAppSelection.setEnabled(isAppSelection);

        mRightVerticalSwipeAppSelection = (Preference) findPreference(KEY_RIGHT_VERTICAL_SWIPE_APP_ACTION);
        isAppSelection = Settings.System.getIntForUser(resolver,
                Settings.System.RIGHT_VERTICAL_BACK_SWIPE_ACTION, 0, UserHandle.USER_CURRENT) == 5/*action_app_action*/;
        mRightVerticalSwipeAppSelection.setEnabled(isAppSelection);

        mTimeout = (SystemSettingListPreference) findPreference(KEY_LONG_BACK_SWIPE_TIMEOUT);

        mExtendedSwipe = (SystemSettingSwitchPreference) findPreference(KEY_BACK_SWIPE_EXTENDED);
        boolean extendedSwipe = Settings.System.getIntForUser(resolver,
                Settings.System.BACK_SWIPE_EXTENDED, 0,
                UserHandle.USER_CURRENT) != 0;
        mExtendedSwipe.setChecked(extendedSwipe);
        mExtendedSwipe.setOnPreferenceChangeListener(this);
        mTimeout.setEnabled(!mExtendedSwipe.isChecked());

        if (!hasMenu && menuCategory != null) {
            prefSet.removePreference(menuCategory);
        }

        if (!hasAssist && assistCategory != null) {
            prefSet.removePreference(assistCategory);
        }

        if (!hasCamera && cameraCategory != null) {
            prefSet.removePreference(cameraCategory);
        }

        if (deviceKeys == 0) {
            prefSet.removePreference(mSwapHardwareKeys);
            prefSet.removePreference(menuCategory);
            prefSet.removePreference(assistCategory);
            prefSet.removePreference(cameraCategory);
        }

        mHandler = new Handler();

        navbarCheck();
        customAppCheck();

        mBackLongPressCustomApp.setEnabled(mBackLongPress.getEntryValues()
                [backlongpress].equals("16"));
        mBackDoubleTapCustomApp.setEnabled(mBackDoubleTap.getEntryValues()
                [backdoubletap].equals("16"));
        mHomeLongPressCustomApp.setEnabled(mHomeLongPress.getEntryValues()
                [homelongpress].equals("16"));
        mHomeDoubleTapCustomApp.setEnabled(mHomeDoubleTap.getEntryValues()
                [homedoubletap].equals("16"));
        mAppSwitchLongPressCustomApp.setEnabled(mAppSwitchLongPress.getEntryValues()
                [appswitchlongpress].equals("16"));
        mAppSwitchDoubleTapCustomApp.setEnabled(mAppSwitchDoubleTap.getEntryValues()
                [appswitchdoubletap].equals("16"));
        mLeftSwipeAppSelection.setVisible(mLeftSwipeActions.getEntryValues()
                [leftSwipeActions].equals("5"));
        mRightSwipeAppSelection.setVisible(mRightSwipeActions.getEntryValues()
                [rightSwipeActions].equals("5"));
        mLeftVerticalSwipeAppSelection.setVisible(mLeftVerticalSwipeActions.getEntryValues()
                [leftVerticalSwipeActions].equals("5"));
        mRightVerticalSwipeAppSelection.setVisible(mRightVerticalSwipeActions.getEntryValues()
                [rightVerticalSwipeActions].equals("5"));

    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mNavigationBar) {
            boolean value = (Boolean) objValue;
            if (mIsNavSwitchingMode) {
                return false;
            }
            mIsNavSwitchingMode = true;
            Settings.System.putInt(resolver,
                    Settings.System.FORCE_SHOW_NAVBAR, value ? 1 : 0);
            navbarCheck();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsNavSwitchingMode = false;
                }
            }, 1500);
            return true;
        } else if (preference == mBackLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_BACK_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mBackLongPress.findIndexOfValue((String) objValue);
            mBackLongPress.setSummary(
                    mBackLongPress.getEntries()[index]);
            customAppCheck();
            mBackLongPressCustomApp.setEnabled(mBackLongPress.getEntryValues()
                    [index].equals("16"));
            return true;
        } else if (preference == mBackDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_BACK_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mBackDoubleTap.findIndexOfValue((String) objValue);
            mBackDoubleTap.setSummary(
                    mBackDoubleTap.getEntries()[index]);
            mBackDoubleTapCustomApp.setEnabled(mBackDoubleTap.getEntryValues()
                    [index].equals("16"));
            return true;
        } else if (preference == mHomeLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_HOME_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mHomeLongPress.findIndexOfValue((String) objValue);
            mHomeLongPress.setSummary(
                    mHomeLongPress.getEntries()[index]);
            mHomeLongPressCustomApp.setEnabled(mHomeLongPress.getEntryValues()
                    [index].equals("16"));
            return true;
        } else if (preference == mHomeDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_HOME_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mHomeDoubleTap.findIndexOfValue((String) objValue);
            mHomeDoubleTap.setSummary(
                    mHomeDoubleTap.getEntries()[index]);
            mHomeDoubleTapCustomApp.setEnabled(mHomeDoubleTap.getEntryValues()
                    [index].equals("16"));
            return true;
        } else if (preference == mAppSwitchLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_APP_SWITCH_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mAppSwitchLongPress.findIndexOfValue((String) objValue);
            mAppSwitchLongPress.setSummary(
                    mAppSwitchLongPress.getEntries()[index]);
            mAppSwitchLongPressCustomApp.setEnabled(mAppSwitchLongPress.getEntryValues()
                    [index].equals("16"));
            return true;
        } else if (preference == mAppSwitchDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_APP_SWITCH_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mAppSwitchDoubleTap.findIndexOfValue((String) objValue);
            mAppSwitchDoubleTap.setSummary(
                    mAppSwitchDoubleTap.getEntries()[index]);
            mAppSwitchDoubleTapCustomApp.setEnabled(mAppSwitchDoubleTap.getEntryValues()
                    [index].equals("16"));
            return true;
        } else if (preference == mMenuLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_MENU_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mMenuLongPress.findIndexOfValue((String) objValue);
            mMenuLongPress.setSummary(
                    mMenuLongPress.getEntries()[index]);
            return true;
        } else if (preference == mMenuDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_MENU_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mMenuDoubleTap.findIndexOfValue((String) objValue);
            mMenuDoubleTap.setSummary(
                    mMenuDoubleTap.getEntries()[index]);
            return true;
        } else if (preference == mCameraLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_CAMERA_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mCameraLongPress.findIndexOfValue((String) objValue);
            mCameraLongPress.setSummary(
                    mCameraLongPress.getEntries()[index]);
            return true;
        } else if (preference == mCameraDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_CAMERA_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mCameraDoubleTap.findIndexOfValue((String) objValue);
            mCameraDoubleTap.setSummary(
                    mCameraDoubleTap.getEntries()[index]);
            return true;
        } else if (preference == mAssistLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_ASSIST_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mAssistLongPress.findIndexOfValue((String) objValue);
            mAssistLongPress.setSummary(
                    mAssistLongPress.getEntries()[index]);
            return true;
        } else if (preference == mAssistDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_ASSIST_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mAssistDoubleTap.findIndexOfValue((String) objValue);
            mAssistDoubleTap.setSummary(
                    mAssistDoubleTap.getEntries()[index]);
            return true;
        } else if (preference == mLeftSwipeActions) {
            int leftSwipeActions = Integer.valueOf((String) objValue);
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.LEFT_LONG_BACK_SWIPE_ACTION, leftSwipeActions,
                    UserHandle.USER_CURRENT);
            int index = mLeftSwipeActions.findIndexOfValue((String) objValue);
            mLeftSwipeActions.setSummary(
                    mLeftSwipeActions.getEntries()[index]);
            mLeftSwipeAppSelection.setEnabled(leftSwipeActions == 5);
            actionPreferenceReload();
            customAppCheck();
            return true;
        } else if (preference == mRightSwipeActions) {
            int rightSwipeActions = Integer.valueOf((String) objValue);
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.RIGHT_LONG_BACK_SWIPE_ACTION, rightSwipeActions,
                    UserHandle.USER_CURRENT);
            int index = mRightSwipeActions.findIndexOfValue((String) objValue);
            mRightSwipeActions.setSummary(
                    mRightSwipeActions.getEntries()[index]);
            mRightSwipeAppSelection.setEnabled(rightSwipeActions == 5);
            actionPreferenceReload();
            customAppCheck();
            return true;
        } else if (preference == mExtendedSwipe) {
            boolean enabled = ((Boolean) objValue).booleanValue();
            mExtendedSwipe.setChecked(enabled);
            mTimeout.setEnabled(!enabled);
            navbarCheck();
            return true; 
        } else if (preference == mLeftVerticalSwipeActions) {
            int leftVerticalSwipeActions = Integer.valueOf((String) objValue);
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.LEFT_VERTICAL_BACK_SWIPE_ACTION, leftVerticalSwipeActions,
                    UserHandle.USER_CURRENT);
            int index = mLeftVerticalSwipeActions.findIndexOfValue((String) objValue);
            mLeftVerticalSwipeActions.setSummary(
                    mLeftVerticalSwipeActions.getEntries()[index]);
            mLeftVerticalSwipeAppSelection.setEnabled(leftVerticalSwipeActions == 5);
            actionPreferenceReload();
            customAppCheck();
            return true;
        } else if (preference == mRightVerticalSwipeActions) {
            int rightVerticalSwipeActions = Integer.valueOf((String) objValue);
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.RIGHT_VERTICAL_BACK_SWIPE_ACTION, rightVerticalSwipeActions,
                    UserHandle.USER_CURRENT);
            int index = mRightVerticalSwipeActions.findIndexOfValue((String) objValue);
            mRightVerticalSwipeActions.setSummary(
                    mRightVerticalSwipeActions.getEntries()[index]);
            mRightVerticalSwipeAppSelection.setEnabled(rightVerticalSwipeActions == 5);
            actionPreferenceReload();
            customAppCheck();
            return true;
        }
        return false;
    }

    private void customAppCheck() {
        mBackLongPressCustomApp.setSummary(Settings.System.getString(getActivity().getContentResolver(),
                String.valueOf(Settings.System.KEY_BACK_LONG_PRESS_CUSTOM_APP_FR_NAME)));
        mBackDoubleTapCustomApp.setSummary(Settings.System.getString(getActivity().getContentResolver(),
                String.valueOf(Settings.System.KEY_BACK_DOUBLE_TAP_CUSTOM_APP_FR_NAME)));
        mHomeLongPressCustomApp.setSummary(Settings.System.getString(getActivity().getContentResolver(),
                String.valueOf(Settings.System.KEY_HOME_LONG_PRESS_CUSTOM_APP_FR_NAME)));
        mHomeDoubleTapCustomApp.setSummary(Settings.System.getString(getActivity().getContentResolver(),
                String.valueOf(Settings.System.KEY_HOME_DOUBLE_TAP_CUSTOM_APP_FR_NAME)));
        mAppSwitchLongPressCustomApp.setSummary(Settings.System.getString(getActivity().getContentResolver(),
                String.valueOf(Settings.System.KEY_APP_SWITCH_LONG_PRESS_CUSTOM_APP_FR_NAME)));
        mAppSwitchDoubleTapCustomApp.setSummary(Settings.System.getString(getActivity().getContentResolver(),
                String.valueOf(Settings.System.KEY_APP_SWITCH_DOUBLE_TAP_CUSTOM_APP_FR_NAME)));
        mLeftSwipeAppSelection.setSummary(Settings.System.getStringForUser(getActivity().getContentResolver(),
                String.valueOf(Settings.System.LEFT_LONG_BACK_SWIPE_APP_FR_ACTION), UserHandle.USER_CURRENT));
        mRightSwipeAppSelection.setSummary(Settings.System.getStringForUser(getActivity().getContentResolver(),
                String.valueOf(Settings.System.RIGHT_LONG_BACK_SWIPE_APP_FR_ACTION), UserHandle.USER_CURRENT));
        mLeftVerticalSwipeAppSelection.setSummary(Settings.System.getStringForUser(getContentResolver(),
                String.valueOf(Settings.System.LEFT_VERTICAL_BACK_SWIPE_APP_FR_ACTION), UserHandle.USER_CURRENT));
        mRightVerticalSwipeAppSelection.setSummary(Settings.System.getStringForUser(getContentResolver(),
                String.valueOf(Settings.System.RIGHT_VERTICAL_BACK_SWIPE_APP_FR_ACTION), UserHandle.USER_CURRENT));
    }

    private void actionPreferenceReload() {
        int leftSwipeActions = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.LEFT_LONG_BACK_SWIPE_ACTION, 0,
                UserHandle.USER_CURRENT);
        int rightSwipeActions = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.RIGHT_LONG_BACK_SWIPE_ACTION, 0,
                UserHandle.USER_CURRENT);

        int leftVerticalSwipeActions = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.LEFT_VERTICAL_BACK_SWIPE_ACTION, 0,
                UserHandle.USER_CURRENT);

        int rightVerticalSwipeActions = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.RIGHT_VERTICAL_BACK_SWIPE_ACTION, 0,
                UserHandle.USER_CURRENT);

        // Reload the action preferences
        mLeftSwipeActions.setValue(Integer.toString(leftSwipeActions));
        mLeftSwipeActions.setSummary(mLeftSwipeActions.getEntry());

        mRightSwipeActions.setValue(Integer.toString(rightSwipeActions));
        mRightSwipeActions.setSummary(mRightSwipeActions.getEntry());

        mLeftVerticalSwipeActions.setValue(Integer.toString(leftVerticalSwipeActions));
        mLeftVerticalSwipeActions.setSummary(mLeftVerticalSwipeActions.getEntry());

        mRightVerticalSwipeActions.setValue(Integer.toString(rightVerticalSwipeActions));
        mRightVerticalSwipeActions.setSummary(mRightVerticalSwipeActions.getEntry());

        mLeftSwipeAppSelection.setVisible(mLeftSwipeActions.getEntryValues()
                [leftSwipeActions].equals("5"));
        mRightSwipeAppSelection.setVisible(mRightSwipeActions.getEntryValues()
                [rightSwipeActions].equals("5"));

        mLeftVerticalSwipeAppSelection.setVisible(mLeftVerticalSwipeActions.getEntryValues()
                [leftVerticalSwipeActions].equals("5"));
        mRightVerticalSwipeAppSelection.setVisible(mRightVerticalSwipeActions.getEntryValues()
                [rightVerticalSwipeActions].equals("5"));
    }

    private boolean isNavbarVisible() {
        boolean defaultToNavigationBar = BiancaUtils.deviceSupportNavigationBar(getActivity());
        return Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.FORCE_SHOW_NAVBAR, defaultToNavigationBar ? 1 : 0) == 1;
    }

    private void navbarCheck() {

        boolean extendedSwipe = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.BACK_SWIPE_EXTENDED, 0,
                UserHandle.USER_CURRENT) != 0;

        deviceKeys = getResources().getInteger(
                com.android.internal.R.integer.config_deviceHardwareKeys);

        if (deviceKeys == 0) {
                homeCategory.setEnabled(true);
                backCategory.setEnabled(true);
                menuCategory.setEnabled(true);
                assistCategory.setEnabled(true);
                appSwitchCategory.setEnabled(true);
                cameraCategory.setEnabled(true);
        } else {
            if (isNavbarVisible()) {
                homeCategory.setEnabled(true);
                backCategory.setEnabled(true);
                menuCategory.setEnabled(true);
                assistCategory.setEnabled(true);
                appSwitchCategory.setEnabled(true);
                cameraCategory.setEnabled(true);
            } else {
                homeCategory.setEnabled(true);
                backCategory.setEnabled(true);
                menuCategory.setEnabled(true);
                assistCategory.setEnabled(true);
                appSwitchCategory.setEnabled(true);
                cameraCategory.setEnabled(true);
            }
        }

        if (BiancaUtils.isGestureNavbar() && isNavbarVisible()) {
            homeCategory.setVisible(false);
            backCategory.setVisible(false);
            menuCategory.setVisible(false);
            assistCategory.setVisible(false);
            appSwitchCategory.setVisible(false);
            cameraCategory.setVisible(false);
            leftSwipeCategory.setVisible(true);
            rightSwipeCategory.setVisible(true);
            leftVerticalSwipeCategory.setVisible(true);
            rightVerticalSwipeCategory.setVisible(true);
        } else {
            homeCategory.setVisible(true);
            backCategory.setVisible(true);
            menuCategory.setVisible(true);
            assistCategory.setVisible(true);
            appSwitchCategory.setVisible(true);
            cameraCategory.setVisible(true);
            leftSwipeCategory.setVisible(false);
            rightSwipeCategory.setVisible(false);
            leftVerticalSwipeCategory.setVisible(false);
            rightVerticalSwipeCategory.setVisible(false);
        }

        if (BiancaUtils.isThemeEnabled("com.android.internal.systemui.navbar.twobutton") && isNavbarVisible()) {
            homeCategory.setEnabled(true);
            backCategory.setEnabled(true);
            menuCategory.setVisible(false);
            assistCategory.setVisible(false);
            appSwitchCategory.setVisible(false);
            cameraCategory.setVisible(false);
            leftSwipeCategory.setVisible(false);
            rightSwipeCategory.setVisible(false);
            leftVerticalSwipeCategory.setVisible(false);
            rightVerticalSwipeCategory.setVisible(false);
        }

        if (BiancaUtils.isThemeEnabled("com.android.internal.systemui.navbar.threebutton")) {
            mTimeout.setVisible(false);
            mExtendedSwipe.setVisible(false);
            mBackGesture.setVisible(false);
            leftSwipeCategory.setVisible(false);
            rightSwipeCategory.setVisible(false);
            leftVerticalSwipeCategory.setVisible(false);
            rightVerticalSwipeCategory.setVisible(false);
        } else if (BiancaUtils.isThemeEnabled("com.android.internal.systemui.navbar.twobutton")) {
            mTimeout.setVisible(false);
            mExtendedSwipe.setVisible(false);
            mBackGesture.setVisible(false);
            leftSwipeCategory.setVisible(false);
            rightSwipeCategory.setVisible(false);
            leftVerticalSwipeCategory.setVisible(false);
            rightVerticalSwipeCategory.setVisible(false);
        } else if (BiancaUtils.isGestureNavbar()) {
            mTimeout.setVisible(true);
            mExtendedSwipe.setVisible(true);
            mBackGesture.setVisible(true);
            leftSwipeCategory.setVisible(true);
            rightSwipeCategory.setVisible(true);
            if (!extendedSwipe) {
                leftVerticalSwipeCategory.setVisible(false);
                rightVerticalSwipeCategory.setVisible(false);
            } else {
                leftVerticalSwipeCategory.setVisible(true);
                rightVerticalSwipeCategory.setVisible(true);
            }
        }

        if (BiancaUtils.isThemeEnabled("com.android.internal.systemui.navbar.threebutton")) {
            mGestureSystemNavigation.setSummary(getString(R.string.legacy_navigation_title));
        } else if (BiancaUtils.isThemeEnabled("com.android.internal.systemui.navbar.twobutton")) {
            mGestureSystemNavigation.setSummary(getString(R.string.swipe_up_to_switch_apps_title));
        } else if (BiancaUtils.isGestureNavbar()) {
            mGestureSystemNavigation.setSummary(getString(R.string.edge_to_edge_navigation_title));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        navbarCheck();
        customAppCheck();
        actionPreferenceReload();
    }

    @Override
    public void onPause() {
        super.onPause();
        navbarCheck();
        customAppCheck();
        actionPreferenceReload();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BIANCA;
    }
}
