package com.qppd.fifov2.ui.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.qppd.fifov2.AboutActivity;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.HelpAndSupportActivity;
import com.qppd.fifov2.Libs.AutoTimez.AutotimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.IntentManager.IntentManagerClass;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;
import com.qppd.fifov2.R;
import com.qppd.fifov2.SettingCalendarActivity;
import com.qppd.fifov2.SettingManageAccountActivity;
import com.qppd.fifov2.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment implements View.OnClickListener {

    private FragmentSettingBinding binding;
    private View root;
    private Context context;
    private LayoutInflater layoutInflater;

    private String TAG = "SettingFragment";

    private DBHandler dbHandler;
    private UserFunctions functions;
    private AutotimeClass autotime;
    private SharedPreferencesClass sharedPreferences;

    private LinearLayout llManage;
    private LinearLayout llNotification;
    private LinearLayout llVoice;
    private LinearLayout llCalendar;
    private LinearLayout llAbout;
    private LinearLayout llHelpAndSupport;

    private RadioGroup rgpNotification;
    private RadioGroup rgpVoice;

    private RadioButton radOn;
    private RadioButton radOff;

//    private RadioButton rad1Hour;
//    private RadioButton rad4Hours;
//    private RadioButton rad8Hours;
//    private RadioButton rad24Hours;
//    private RadioButton radNever;

    private RadioButton radVoiceYes;
    private RadioButton radVoiceNo;

    private AlertDialog.Builder dialogNotificationBuilder;
    private View dialogNotificationView;
    private AlertDialog alertNotificationDialog;

    private AlertDialog.Builder dialogVoiceBuilder;
    private View dialogVoiceView;
    private AlertDialog alertVoiceDialog;

    private boolean voiceActivated = false;
    private int notificationStatus = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false);
        root = binding.getRoot();
        context = root.getContext();

        functions = new UserFunctions(context);
        autotime = new AutotimeClass(context);
        sharedPreferences = new SharedPreferencesClass(context);

        initComponents();

        return root;
    }

    private void initComponents() {

        llManage = root.findViewById(R.id.llManage);
        llManage.setOnClickListener(this);
        llNotification = root.findViewById(R.id.llNotification);
        llNotification.setOnClickListener(this);
        llVoice = root.findViewById(R.id.llVoice);
        llVoice.setOnClickListener(this);
        llCalendar = root.findViewById(R.id.llCalendar);
        llCalendar.setOnClickListener(this);
        llAbout = root.findViewById(R.id.llAbout);
        llAbout.setOnClickListener(this);
        llHelpAndSupport = root.findViewById(R.id.llHelpAndSupport);
        llHelpAndSupport.setOnClickListener(this);

        buildNotificationDialog();
        buildVoiceDialog();
        loadSettings();
    }

    void loadSettings() {

        if(sharedPreferences.getInt("setting_notification", 0) == 1){
            radOn.setChecked(true);
        } else if(sharedPreferences.getInt("setting_notification", 0) == 0){
            radOff.setChecked(true);
        }
//        if(sharedPreferences.getInt("setting_notification", 0) == 0){
//            radNever.setChecked(true);
//        } else if(sharedPreferences.getInt("setting_notification", 0) == 1){
//            rad1Hour.setChecked(true);
//        } else if(sharedPreferences.getInt("setting_notification", 0) == 4){
//            rad4Hours.setChecked(true);
//        } else if(sharedPreferences.getInt("setting_notification", 0) == 8){
//            rad8Hours.setChecked(true);
//        } else if(sharedPreferences.getInt("setting_notification", 0) == 24){
//            rad24Hours.setChecked(true);
//        }

        if (sharedPreferences.getBoolean("setting_voice", false)) {
            radVoiceYes.setChecked(true);
        }else{
            radVoiceNo.setChecked(true);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llManage:
                IntentManagerClass.intentsify(getActivity(), SettingManageAccountActivity.class);
                break;
            case R.id.llNotification:
                alertNotificationDialog.show();

                break;
            case R.id.llVoice:
                alertVoiceDialog.show();
                break;
            case R.id.llCalendar:
                IntentManagerClass.intentsify(getActivity(), SettingCalendarActivity.class);
                break;
            case R.id.llAbout:
                IntentManagerClass.intentsify(getActivity(), AboutActivity.class);
                break;
            case R.id.llHelpAndSupport:
                IntentManagerClass.intentsify(getActivity(), HelpAndSupportActivity.class);
                break;
        }
    }

    private void buildNotificationDialog() {
        dialogNotificationBuilder = new AlertDialog.Builder(getContext());
        dialogNotificationView = layoutInflater.inflate(R.layout.dialog_setting_notification, null);

        rgpNotification = root.findViewById(R.id.rgpNotification);

        radOn = dialogNotificationView.findViewById(R.id.radOn);
        radOff = dialogNotificationView.findViewById(R.id.radOff);

        radOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    notificationStatus = 1;
                }
            }
        });

        radOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    notificationStatus = 0;
                }
            }
        });


//        rad1Hour = dialogNotificationView.findViewById(R.id.rad1Hour);
//        rad4Hours = dialogNotificationView.findViewById(R.id.rad4Hours);
//        rad8Hours = dialogNotificationView.findViewById(R.id.rad8Hours);
//        rad24Hours = dialogNotificationView.findViewById(R.id.rad24Hours);
//        radNever = dialogNotificationView.findViewById(R.id.radNever);

//        rad1Hour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    notificationStatus = 1;
//                }
//            }
//        });
//
//        rad4Hours.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    notificationStatus = 4;
//                }
//            }
//        });
//
//        rad8Hours.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    notificationStatus = 8;
//                }
//            }
//        });
//
//        rad24Hours.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    notificationStatus = 24;
//                }
//            }
//        });
//
//        radNever.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    notificationStatus = 0;
//                }
//            }
//        });

        dialogNotificationBuilder.setCancelable(false);
        dialogNotificationBuilder.setView(dialogNotificationView)
                .setPositiveButton("SAVE", (dialog, id) -> {
                    dialog.dismiss();

                    sharedPreferences.putInt("setting_notification", notificationStatus);

                })
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    dialog.dismiss();
                });
        alertNotificationDialog = dialogNotificationBuilder.create();
    }


    private void buildVoiceDialog() {
        dialogVoiceBuilder = new AlertDialog.Builder(getContext());
        dialogVoiceView = layoutInflater.inflate(R.layout.dialog_setting_voice, null);

        radVoiceYes = dialogVoiceView.findViewById(R.id.radVoiceYes);
        radVoiceYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    voiceActivated = true;
                    sharedPreferences.putBoolean("setting_voice", true);
                }
            }
        });

        radVoiceNo = dialogVoiceView.findViewById(R.id.radVoiceNo);
        radVoiceNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    voiceActivated = false;
                    sharedPreferences.putBoolean("setting_voice", false);
                }
            }
        });

        dialogVoiceBuilder.setCancelable(false);
        dialogVoiceBuilder.setView(dialogVoiceView)
                .setPositiveButton("SAVE", (dialog, id) -> {
                    dialog.dismiss();
                    sharedPreferences.putBoolean("setting_voice", voiceActivated);

                })
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    dialog.dismiss();
                });
        alertVoiceDialog = dialogVoiceBuilder.create();
    }


}