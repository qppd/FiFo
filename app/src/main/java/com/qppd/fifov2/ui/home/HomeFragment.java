package com.qppd.fifov2.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.qppd.fifov2.CategoryFragment;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Globals.UserGlobal;
import com.qppd.fifov2.Libs.AutoTimez.AutotimeClass;
import com.qppd.fifov2.Libs.DateTimez.DateTimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;
import com.qppd.fifov2.MenuActivity;
import com.qppd.fifov2.R;
import com.qppd.fifov2.SavingActualFragment;
import com.qppd.fifov2.SavingFragment;
import com.qppd.fifov2.databinding.FragmentHomeBinding;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Logger;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.SpeechUtil;
import net.gotev.speech.SupportedLanguagesListener;
import net.gotev.speech.TextToSpeechCallback;
import net.gotev.speech.UnsupportedReason;
import net.gotev.speech.ui.SpeechProgressView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements SpeechDelegate, View.OnLongClickListener, View.OnClickListener {

    private FragmentHomeBinding binding;
    private View root;

    private UserFunctions functions = new UserFunctions(this.getContext());
    private SharedPreferencesClass sharedPreferences;
    private DateTimeClass dateTimeClass;
    private DBHandler dbHandler;

    private final int PERMISSIONS_REQUEST = 1;
    private static final String LOG_TAG = HomeFragment.class.getSimpleName();

    private CardView cardExpenses;
    private CardView cardSavings;
    private LinearLayout llTotal;

    PieChart chartExpenses;

    private ImageButton button;
    private TextView text;
    private SpeechProgressView progress;
    private LinearLayout linearLayout;


    private TextToSpeech.OnInitListener mTttsInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(final int status) {
            switch (status) {
                case TextToSpeech.SUCCESS:
                    Logger.info(LOG_TAG, "TextToSpeech engine successfully started");
                    break;

                case TextToSpeech.ERROR:
                    Logger.error(LOG_TAG, "Error while initializing TextToSpeech engine!");
                    break;

                default:
                    Logger.error(LOG_TAG, "Unknown TextToSpeech status: " + status);
                    break;
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        functions = new UserFunctions(getContext());
        functions.noActionBar(((AppCompatActivity) getActivity()).getSupportActionBar());
        sharedPreferences = new SharedPreferencesClass(root.getContext());
        dateTimeClass = new DateTimeClass("MMMM");
        dbHandler = new DBHandler(getContext());
        initComponents();

        loadPieChart();

        return root;
    }

    private void loadPieChart() {
        double income, expenses, balance, target, total;
        income = Double.parseDouble(dbHandler.getProfile(UserGlobal.getUser().getId()).getIncome());
        expenses = dbHandler.getCurrentMonthTotalExpenses(Integer.parseInt(
                new DateTimeClass("M").getFormattedTime()));
        balance = income - expenses;


        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(Float.parseFloat(String.valueOf(income)), "Income"));
        entries.add(new PieEntry(Float.parseFloat(String.valueOf(expenses)), "Expenses"));
        entries.add(new PieEntry(Float.parseFloat(String.valueOf(balance)), "Savings"));
//        entries.add(new PieEntry(30f, "Savings"));

        int[] colors = {
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.teal_700),
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.aqua),
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.yellow),
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.colorAccent)
        };

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // If the SDK version is Marshmallow (API 23) or higher, use ContextCompat.getColor()
            color = ContextCompat.getColor(getContext(), R.color.colorMainDark);
        } else {
            // For SDK versions lower than Marshmallow, use getResources().getColor()
            color = getResources().getColor(R.color.colorMainDark);
        }
        chartExpenses.setBackgroundColor(color);
        chartExpenses.setCenterText(dateTimeClass.getFormattedTime());
        chartExpenses.setCenterTextColor(Color.BLACK);
        chartExpenses.setCenterTextSize(30);

        chartExpenses.setOnClickListener(this);

        chartExpenses.setHoleColor(color);

        PieData data = new PieData(dataSet);
        chartExpenses.setData(data);

        // Description
        chartExpenses.getDescription().setEnabled(true);
        chartExpenses.getDescription().setText("Monthly Summary Report");



        // Legend
        chartExpenses.getLegend().setEnabled(true);
        chartExpenses.animateXY(1500, 1500);
        chartExpenses.invalidate();
    }

    private void loadSettings() {
        if (sharedPreferences.getBoolean("setting_voice", true)) {
            button.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
        }else{
            button.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
        }
    }

    private void initComponents() {

        Speech.init(getContext(), getActivity().getPackageName(), mTttsInitListener);

        cardExpenses = root.findViewById(R.id.cardExpenses);
        cardExpenses.setOnClickListener(this);
        cardExpenses.setOnLongClickListener(this);
        cardSavings = root.findViewById(R.id.cardSavings);
        cardSavings.setOnClickListener(this);
        cardSavings.setOnLongClickListener(this);

        llTotal = root.findViewById(R.id.llTotal);
        llTotal.setOnClickListener(this);

        linearLayout = root.findViewById(R.id.linearLayout);

        chartExpenses = root.findViewById(R.id.chartExpenses);
        chartExpenses.setOnClickListener(this);

        button = root.findViewById(R.id.button);
        button.setOnClickListener(view -> onButtonClick());

        text = root.findViewById(R.id.text);
        progress = root.findViewById(R.id.progress);


        int[] colors = {
                ContextCompat.getColor(getContext(), android.R.color.black),
                ContextCompat.getColor(getContext(), android.R.color.darker_gray),
                ContextCompat.getColor(getContext(), android.R.color.black),
                ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark),
                ContextCompat.getColor(getContext(), android.R.color.holo_red_dark)
        };
        progress.setColors(colors);

        loadSettings();

    }

    private void onSetSpeechToTextLanguage() {
        Speech.getInstance().getSupportedSpeechToTextLanguages(new SupportedLanguagesListener() {
            @Override
            public void onSupportedLanguages(List<String> supportedLanguages) {
                CharSequence[] items = new CharSequence[supportedLanguages.size()];
                supportedLanguages.toArray(items);

                new AlertDialog.Builder(getContext())
                        .setTitle("Current language: " + Speech.getInstance().getSpeechToTextLanguage())
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Locale locale;

                                if (Build.VERSION.SDK_INT >= 21) {
                                    locale = Locale.forLanguageTag(supportedLanguages.get(i));
                                    Toast.makeText(getContext(), "" + i, Toast.LENGTH_LONG).show();

                                } else {
                                    String[] langParts = supportedLanguages.get(i).split("-");

                                    if (langParts.length >= 2) {
                                        locale = new Locale(langParts[0], langParts[1]);
                                    } else {
                                        locale = new Locale(langParts[0]);
                                    }
                                }

                                Speech.getInstance().setLocale(locale);
                                Toast.makeText(getContext(), "Selected: " + items[i], Toast.LENGTH_LONG).show();
                            }
                        })
                        .setPositiveButton("Cancel", null)
                        .create()
                        .show();
            }

            @Override
            public void onNotSupported(UnsupportedReason reason) {
                switch (reason) {
                    case GOOGLE_APP_NOT_FOUND:
                        showSpeechNotSupportedDialog();
                        break;

                    case EMPTY_SUPPORTED_LANGUAGES:
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.set_stt_langs)
                                .setMessage(R.string.no_langs)
                                .setPositiveButton("OK", null)
                                .show();
                        break;
                }
            }
        });
    }

    private void onSetTextToSpeechVoice() {
        List<Voice> supportedVoices = Speech.getInstance().getSupportedTextToSpeechVoices();

        if (supportedVoices.isEmpty()) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.set_tts_voices)
                    .setMessage(R.string.no_tts_voices)
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        // Sort TTS voices
        Collections.sort(supportedVoices, (v1, v2) -> v1.toString().compareTo(v2.toString()));

        CharSequence[] items = new CharSequence[supportedVoices.size()];
        Iterator<Voice> iterator = supportedVoices.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Voice voice = iterator.next();

            items[i] = voice.toString();
            i++;
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Current: " + Speech.getInstance().getTextToSpeechVoice())
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Speech.getInstance().setVoice(supportedVoices.get(i));
                        Toast.makeText(getContext(), "Selected: " + items[i], Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Speech.getInstance().shutdown();
    }

    private void onButtonClick() {
        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                onRecordAudioPermissionGranted();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSIONS_REQUEST) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay!
                onRecordAudioPermissionGranted();
            } else {
                // permission denied, boo!
                Toast.makeText(getContext(), R.string.permission_required, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void onRecordAudioPermissionGranted() {
        button.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

        try {
            Speech.getInstance().stopTextToSpeech();
            Speech.getInstance().startListening(progress, this);

        } catch (SpeechRecognitionNotAvailable exc) {
            showSpeechNotSupportedDialog();

        } catch (GoogleVoiceTypingDisabledException exc) {
            showEnableGoogleVoiceTyping();
        }
    }

//    private void onSpeakClick() {
//        if (textToSpeech.getText().toString().trim().isEmpty()) {
//            Toast.makeText(getContext(), R.string.input_something, Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        Speech.getInstance().say(textToSpeech.getText().toString().trim(), new TextToSpeechCallback() {
//            @Override
//            public void onStart() {
//                Toast.makeText(getContext(), "TTS onStart", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCompleted() {
//                Toast.makeText(getContext(), "TTS onCompleted", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError() {
//                Toast.makeText(getContext(), "TTS onError", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onStartOfSpeech() {
    }

    @Override
    public void onSpeechRmsChanged(float value) {
        //Log.d(getClass().getSimpleName(), "Speech recognition rms is now " + value +  "dB");
    }

    private FragmentTransaction transaction;

    @Override
    public void onSpeechResult(String result) {

        button.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);

        if (result.isEmpty()) {
            Speech.getInstance().say(getString(R.string.repeat));

        } else {
            functions.showMessage(result);
            if (checkSpeech(result)) {

                result = getTextAfterSubstr(result).trim();
//                result = result.substring(0, 1).toUpperCase() + result.substring(1);
                Speech.getInstance().say("Going to " + result + ".");
                text.setText("Going to " + result);

                if (result == "savings" || result == "saving") {
                    transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_menu, new SavingFragment());
                    transaction.addToBackStack(null); // Optional: Add transaction to back stack
                    transaction.commit();
                } else if(result == "expenses" || result == "expense"){
                    transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_menu, new CategoryFragment());
                    transaction.addToBackStack(null); // Optional: Add transaction to back stack
                    transaction.commit();

                } else if(result == "total" || result == "totals"){
                    transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_menu, new SavingActualFragment());
                    transaction.addToBackStack(null); // Optional: Add transaction to back stack
                    transaction.commit();

                }
            } else {
                Speech.getInstance().say("I did not understand! Please repeat!");
                text.setText("I did not understand! Please repeat!");
            }


        }
    }

    private boolean checkSpeech(String result) {
        // Check if the length of result is at least 15 characters
        if (result.length() >= 9) {
            // Get the substring of the first 15 characters
            String first15Chars;
            if (result.length() < 15) {
                first15Chars = result.substring(0, result.length());
            } else {
                first15Chars = result.substring(0, 15);
            }


            if (first15Chars.contains("hi po") && (first15Chars.contains("show") || first15Chars.contains("ad"))) {
                return true;
            } else if (first15Chars.contains("typo") && (first15Chars.contains("show") || first15Chars.contains("ad"))) {
                return true;
            } else if (first15Chars.contains("fifo") && (first15Chars.contains("show") || first15Chars.contains("ad"))) {
                return true;
            } else if (first15Chars.contains("hypo") && (first15Chars.contains("show") || first15Chars.contains("ad"))) {
                return true;
            } else if (first15Chars.contains("pipo") && (first15Chars.contains("show") || first15Chars.contains("ad"))) {
                return true;
            } else if (first15Chars.contains("bipo") && (first15Chars.contains("show") || first15Chars.contains("ad"))) {
                return true;
            } else if (first15Chars.contains("people") && (first15Chars.contains("show") || first15Chars.contains("ad"))) {
                return true;
            } else if (first15Chars.contains("iphone") && (first15Chars.contains("show") || first15Chars.contains("ad"))) {
                return true;
            } else if (first15Chars.contains("five po") && (first15Chars.contains("show") || first15Chars.contains("ad"))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static String getTextAfterSubstr(String result) {
        // Find the index of "ad" or "add"
        int indexAd = result.indexOf("show");
        int indexAdd = result.indexOf("shows");

        // Determine the correct index before 15 characters
        int index = Math.min(indexAd, indexAdd);

        // Check if "ad" or "add" is found before 15 characters
        if (index != -1 && index < 15) {
            // Extract and return the text after the found substring
            return result.substring(index + (result.startsWith("show", index) ? 3 : 2));
        }

        // If "ad" or "add" is not found before 15 characters, return an empty string
        return "";
    }

    @Override
    public void onSpeechPartialResults(List<String> results) {
        text.setText("");
        for (String partial : results) {
            //text.append(partial + " ");
        }
    }

    private void showSpeechNotSupportedDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        SpeechUtil.redirectUserToGoogleAppOnPlayStore(getContext());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.speech_not_available)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener)
                .show();
    }

    private void showEnableGoogleVoiceTyping() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.enable_google_voice_typing)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .show();
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.cardExpenses:

                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction;
        switch (view.getId()) {
            case R.id.cardExpenses:
                transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_menu, new CategoryFragment());
                transaction.addToBackStack(null); // Optional: Add transaction to back stack
                transaction.commit();
                break;

            case R.id.cardSavings:
                transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_menu, new SavingFragment());
                transaction.addToBackStack(null); // Optional: Add transaction to back stack
                transaction.commit();
                break;

            case R.id.llTotal:
            case R.id.chartExpenses:

                double expenses = dbHandler.getCurrentMonthTotalExpenses(Integer.parseInt(
                        new DateTimeClass("M").getFormattedTime()));
                if(expenses > 0){
                    transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_menu, new SavingActualFragment());
                    transaction.addToBackStack(null); // Optional: Add transaction to back stack
                    transaction.commit();
                }
                else{
                    functions.showMessage("Input expenses first!");
                }
//                transaction = getParentFragmentManager().beginTransaction();
//                transaction.replace(R.id.nav_host_fragment_activity_menu, new SavingActualFragment());
//                transaction.addToBackStack(null); // Optional: Add transaction to back stack
//                transaction.commit();
                break;
        }
    }
}