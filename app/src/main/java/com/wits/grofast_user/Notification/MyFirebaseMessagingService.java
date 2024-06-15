package com.wits.grofast_user.Notification;

import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wits.grofast_user.MainActivity;
import com.wits.grofast_user.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static int notificationId = 0;
    private static final String CHANNEL_ID = "grofastChannel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.e("TAG", "onMessageReceived: notification received ");
        Log.e("TAG", "onMessageReceived: Message type" + message.getMessageType());


        // Handle notification message
        if (message.getNotification() != null) {
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();

            Log.e("TAG", "onMessageReceived: title " + title);
            Log.e("TAG", "onMessageReceived: body " + body);
            showNotification(title, body);
        }

        // Handle data payload
        if (message.getData().size() > 0) {
            String msgKey = message.getData().get("key");
            String msgId = message.getData().get("id");

            // Handle the extracted data as required
            Log.d("TAG", "msgKey: " + msgKey);
            Log.d("TAG", "msgId: " + msgId);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("TAG", "onNewToken: fcm " + token);
    }


    @SuppressLint("ObsoleteSdkInt")
    private void showNotification(String title, String body) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "groFast", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }


        // Create intent to launch your app's main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // Create notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher) // Set small icon here
                .setContentTitle(title).setContentText(body).setAutoCancel(true).setContentIntent(pendingIntent);

        notificationManager.notify(notificationId++, builder.build());
    }
}