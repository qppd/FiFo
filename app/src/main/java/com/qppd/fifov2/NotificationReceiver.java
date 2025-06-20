package com.qppd.fifov2;
/// NotificationReceiver.java
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Globals.UserGlobal;
import com.qppd.fifov2.Libs.DateTimez.DateTimeClass;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "balance_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Load data and check balance here
        double income, expenses, balance;
        DBHandler dbHandler = new DBHandler(context); // Assuming DbHandler takes a context
        SharedPreferencesClass sharedPreferencesClass = new SharedPreferencesClass(context); // Assuming SharedPreferencesClass takes a context

        income = Double.parseDouble(dbHandler.getProfile(UserGlobal.getUser().getId()).getIncome());
        expenses = dbHandler.getCurrentMonthTotalExpenses(Integer.parseInt(new DateTimeClass("M").getFormattedTime()));
        balance = income - expenses;

        if (balance <= 0 && sharedPreferencesClass.getInt("setting_notification", 0) == 1) {
            createNotificationChannel(context);
            showNotification(context, "Oops, Sumosobra kana!");
        }else if(balance >= 0 && sharedPreferencesClass.getInt("setting_notification", 0) == 1){
            createNotificationChannel(context);
            showNotification(context, "CONGRATS, Hindi kana Gastador!");
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
}
