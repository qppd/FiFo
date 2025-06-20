package com.qppd.fifov2;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.qppd.fifov2.Classes.Cart;
import com.qppd.fifov2.Classes.Category;
import com.qppd.fifov2.Classes.Expense;
import com.qppd.fifov2.Classes.Saving;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Globals.ExpenseGlobal;
import com.qppd.fifov2.Globals.UserGlobal;
import com.qppd.fifov2.Libs.DateTimez.DateTimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.Receiptz.POSReceipt;
import com.qppd.fifov2.Libs.Receiptz.Receipt;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;
import com.qppd.fifov2.Libs.Validatorz.ValidatorClass;
import com.qppd.fifov2.Tasks.LoginTask;
import com.qppd.fifov2.databinding.FragmentCategoryBinding;
import com.qppd.fifov2.databinding.FragmentExpenseBinding;
import com.qppd.fifov2.ui.home.HomeFragment;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Logger;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.SpeechUtil;
import net.gotev.speech.SupportedLanguagesListener;
import net.gotev.speech.UnsupportedReason;
import net.gotev.speech.ui.SpeechProgressView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ExpenseFragment extends Fragment implements SpeechDelegate, View.OnClickListener {

    private static final String CHANNEL_ID = "balance_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    private FragmentExpenseBinding binding;
    private View root;

    private UserFunctions functions;
    private SharedPreferencesClass sharedPreferences;
    private ListView listExpenses;
    private DBHandler dbHandler;

    private int expense_key;
    private ArrayList<String> expense_keys;
    private ArrayList<Expense> expense_list;

    private ExpenseList expenseAdapter;
    private Expense expense;

    private TextView txtTitle;
    private Button btnAddExpense;
    private EditText edtExpenseContainer;

    private TextView txtTotal;

    private AlertDialog.Builder dialogBuilder;
    private View dialogView;
    private AlertDialog alertDialog;

    private AlertDialog.Builder delete_dialog_builder;
    private AlertDialog delete_dialog;

    private LayoutInflater layoutInflater;
    private EditText edtExpenseProduct;
    private EditText edtExpensePrice;

    private CheckBox chckExpensePriority;

    private DateTimeClass dateTimeClass;

    private final int PERMISSIONS_REQUEST = 1;
    private static final String LOG_TAG = ExpenseFragment.class.getSimpleName();

    private ImageButton button;
    private TextView text;
    private SpeechProgressView progress;
    private LinearLayout linearLayout;


    private int priority_limit = 5;
    private int priority_count = 0;
    private int priority = 0;

    private Button btnBack;

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_expense, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menuRemove:

                delete_dialog.show();
                break;

        }
        return false;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        layoutInflater = inflater;
        binding = FragmentExpenseBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        functions = new UserFunctions(root.getContext());
        dbHandler = new DBHandler(root.getContext());
        sharedPreferences = new SharedPreferencesClass(root.getContext());

        initComponents();

        return root;
    }

    private void initComponents() {

        Speech.init(getContext(), getActivity().getPackageName(), mTttsInitListener);

        txtTitle = root.findViewById(R.id.txtTitle);
        txtTitle.setText(ExpenseGlobal.getTitle());

        btnAddExpense = root.findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(this);

        //edtExpenseContainer = root.findViewById(R.id.edtExpenseContainer);
        listExpenses = root.findViewById(R.id.listExpenses);
        registerForContextMenu(listExpenses);

        txtTotal = root.findViewById(R.id.expense_total);

        loadExpenses();

        btnAddExpense = root.findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(this);

        buildAddExpenseDialog();

        linearLayout = root.findViewById(R.id.linearLayout);
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

        btnBack = root.findViewById(R.id.btnExpenseBack);
        btnBack.setOnClickListener(this);

        delete_dialog_builder = new AlertDialog.Builder(getContext());
        delete_dialog_builder.setCancelable(true);
        delete_dialog_builder.setTitle("Confirmation");
        delete_dialog_builder.setMessage("Are you sure you want to delete this from Expenses?");
        delete_dialog_builder.setPositiveButton(android.R.string.ok, (dialog, which) -> deleteExpense(expense));
        delete_dialog_builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> delete_dialog.dismiss());
        delete_dialog = delete_dialog_builder.create();

    }

    private void deleteExpense(Expense expense) {
        dbHandler.deleteExpense(expense);
        loadExpenses();
    }

    private void buildAddExpenseDialog() {

        dialogBuilder = new AlertDialog.Builder(getContext());
        dialogView = layoutInflater.inflate(R.layout.dialog_add_expense, null);
        edtExpenseProduct = dialogView.findViewById(R.id.edtExpenseProduct);
        edtExpensePrice = dialogView.findViewById(R.id.edtExpensePrice);
        chckExpensePriority = dialogView.findViewById(R.id.chckExpensePriority);
        chckExpensePriority.setChecked(false);
        chckExpensePriority.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    priority = 1; // set priority to 1 if checked

                    dateTimeClass = new DateTimeClass("MM/dd/YYYY");
                    int priority_count = sharedPreferences.getInt(dateTimeClass.getFormattedTime(),
                            0); // get priority count for this month

                    //functions.showMessage("COUNT:" + priority_count);

                    if (priority_count == 5) { // if priorities count is 5
                        priority = 0; // then make it not priority
                    } else if (priority_count < 5) { // if priorities count is less than 5
                        priority = 1; // make it priority
                        priority_count++; // add count to priorities
                        sharedPreferences.putInt(dateTimeClass.getFormattedTime(),
                                priority_count); // save priorities count
                        //functions.showMessage("COUNT AFTER:" + priority_count);
                    }


                } else {
                    priority = 0;

//                    dateTimeClass = new DateTimeClass("MM/dd/YYYY");
//                    int priority_count = sharedPreferences.getInt(dateTimeClass.getFormattedTime(),
//                            0); // get priority count for this month
//
//                    functions.showMessage("COUNT:" + priority_count);
//
//                    if (priority_count < 5) { // if priorities count is less than 5
//                        priority = 1; // make it priority
//                        priority_count--; // subtract count to priorities
//                        sharedPreferences.putInt(dateTimeClass.getFormattedTime(),
//                                priority_count); // save priorities count
//                        functions.showMessage("COUNT AFTER:" + priority_count);
//
//                    }


                }
            }
        });

        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialogView)
                .setPositiveButton("SAVE", (dialog, id) -> {
                    dialog.dismiss();
                    attemptSave();


                })
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog = dialogBuilder.create();
    }

    private void attemptSave() {
        boolean cancel = false;
        View focusView = null;

        String product = edtExpenseProduct.getText().toString();
        String price = edtExpensePrice.getText().toString();


        if (TextUtils.isEmpty(product)) {
            //functions.showMessage("Product is required!");
            product = "*"; // put asterisk if product name is empty
            //cancel = true;
        }

        if (TextUtils.isEmpty(price)) {
            functions.showMessage("Price is required!");
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the
            // field with an error.
            focusView.requestFocus();
        } else {

            DBHandler dbHandler = new DBHandler(getContext());
            dateTimeClass = new DateTimeClass("MM/dd/YYYY");

            if (dbHandler.addExpense(new Expense(ExpenseGlobal.getCategory_id(), product,
                    Double.parseDouble(price), priority, dateTimeClass.getFormattedTime()))) {
                loadExpenses();

                functions.showMessage("Expense saved successfully!");
            } else {
                functions.showMessage("Expense failed saving! Try again!");
            }

        }
    }

    private void loadExpenses() {
        ArrayList<Expense> expenses = dbHandler.getAllExpenses(ExpenseGlobal.getCategory_id());
        expense_list = new ArrayList<>();
        expense_keys = new ArrayList<>();
        expense_list = dbHandler.getAllExpenses(ExpenseGlobal.getCategory_id());

        float total = 0;
        for (Expense expense : expenses) {
            total += expense.getPrice();
            txtTotal.setText("₱ " + String.format("%.2f", total));
        }

        expenseAdapter = new ExpenseList((Activity) root.getContext(), expense_list);
        listExpenses.setAdapter(expenseAdapter);

        listExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                expense_key = expense_list.get(i).getId();
                expense = expense_list.get(i);
                return false;
            }
        });

        if (sharedPreferences.getInt("setting_notification", 0) == 1) {
            double income, expenses2, balance;
            income = Double.parseDouble(dbHandler.getProfile(UserGlobal.getUser().getId()).getIncome());
            expenses2 = dbHandler.getCurrentMonthTotalExpenses(Integer.parseInt(new DateTimeClass("M").getFormattedTime()));
            balance = income - expenses2;

            if (balance <= 0) {
                createNotificationChannel(getContext());
                showNotification(getContext(), "Oops, Sumosobra kana!");
            } else if (balance >= 0) {
                createNotificationChannel(getContext());
                showNotification(getContext(), "CONGRATS, Hindi kana Gastador!");
            }
        }


    }

    private void createNotificationChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Balance Notification Channel";
            String description = "Channel for balance notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(Context context, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Balance Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Speech.getInstance().shutdown();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddExpense:
                dateTimeClass = new DateTimeClass("MM/dd/YYYY");
                priority_count = sharedPreferences.getInt(dateTimeClass.getFormattedTime(), 0);
                //functions.showMessage("PRIO COUNT:" + priority_count);
                if (priority_count == 5) {
                    chckExpensePriority.setVisibility(View.GONE);
                } else {
                    chckExpensePriority.setVisibility(View.VISIBLE);
                }

                chckExpensePriority.setChecked(false);

                alertDialog.show();
                break;
            case R.id.btnExpenseBack:

                getParentFragmentManager().popBackStack();

                break;
        }
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
        binding = null;
        //Speech.getInstance().shutdown();

    }

    private void onButtonClick() {
        Speech.init(getContext(), getActivity().getPackageName(), mTttsInitListener);
        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onStartOfSpeech() {
    }

    @Override
    public void onSpeechRmsChanged(float value) {
        //Log.d(getClass().getSimpleName(), "Speech recognition rms is now " + value +  "dB");
    }

//    @Override
//    public void onSpeechResult(String result) {
//
//        button.setVisibility(View.VISIBLE);
//        linearLayout.setVisibility(View.GONE);
//
//        text.setText(result);
//
//        if (result.isEmpty()) {
//            Speech.getInstance().say(getString(R.string.repeat));
//
//        } else {
//            Speech.getInstance().say(result);
//
//
//        }
//    }

    @Override
    public void onSpeechResult(String result) {

        button.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);

        //functions.showMessage(result);

        if (result.isEmpty()) {
            Speech.getInstance().say(getString(R.string.repeat));
        } else {

            functions.showMessage(result);

            if (checkSpeech(result)) {


                result = getTextAfterSubstr(result).trim();
                result = result.substring(0, 1).toUpperCase() + result.substring(1);
                result = result.replace(",", "");

                if(!ValidatorClass.validateNumberOnly(result)){
                    Speech.getInstance().say("I did not understand! Please repeat!");
                    text.setText("I did not understand! Please repeat!");
                }else {
                    Speech.getInstance().say("I am now adding " + result + ".");
                    text.setText("I am now adding " + result);

                    result = result.replace(",", "");
                    result = result.trim();
                    dateTimeClass = new DateTimeClass("MM/dd/YYYY");

                    dbHandler.addExpense(new Expense(ExpenseGlobal.getCategory_id(), "*",
                            Double.parseDouble(result), 0, dateTimeClass.getFormattedTime()));
                    loadExpenses();
                }

            } else {
                Speech.getInstance().say("I did not understand! Please repeat!");
                text.setText("I did not understand! Please repeat!");
            }
        }
    }


    private boolean checkSpeech(String result) {
        // Check if the length of result is at least 15 characters
        if (result.length() >= 1) {
            // Get the substring of the first 15 characters
            String first15Chars;
            if (result.length() < 15) {
                first15Chars = result.substring(0, result.length());
            } else {
                first15Chars = result.substring(0, 15);
            }


            if (first15Chars.contains("fifo") && (first15Chars.contains("add") || first15Chars.contains("ad"))) {
                return true;
            } else if (first15Chars.contains("pipo") && (first15Chars.contains("add") || first15Chars.contains("ad"))) {
                return true;
            } else if (first15Chars.contains("people") && (first15Chars.contains("add") || first15Chars.contains("ad"))) {
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
        int indexAd = result.indexOf("ad");
        int indexAdd = result.indexOf("add");

        // Determine the correct index before 15 characters
        int index = Math.min(indexAd, indexAdd);

        // Check if "ad" or "add" is found before 15 characters
        if (index != -1 && index < 15) {
            // Extract and return the text after the found substring
            return result.substring(index + (result.startsWith("add", index) ? 3 : 2));
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
}