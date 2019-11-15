package com.galileo.cc6.helpmate.activities;

/**
 * Created by EEVGG on 11/11/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.dataModels.Assignment;
import com.galileo.cc6.helpmate.database.AssignmentDAO;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class AlertReceiver extends BroadcastReceiver{

    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context context, Intent intent) {
        /* when a broadcast targets this class, it retrieves the assingment list
        compares it to today's date and the decides if it should create a notification
         */
        AssignmentDAO aDAO = new AssignmentDAO(context);
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        try {
            List<Assignment> aList = aDAO.getAssignmentByDate(mCalendar.getTime());
            if(!aList.isEmpty()){
                createNotification(context, "Tareas de Hoy", "Tiene "+aList.size()+"  tareas para hoy, click para ver la lista", "Alert");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert){

        // Define an Intent and an action to perform with it by another application
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, AssigmentActivity.class), 0);

        // Builds a notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(msg)
                        .setTicker(msgAlert)
                        .setContentText(msgText);

        // Defines the Intent to fire when the notification is clicked
        mBuilder.setContentIntent(notificIntent);

        // Set the default notification option
        // DEFAULT_SOUND : Make sound
        // DEFAULT_VIBRATE : Vibrate
        // DEFAULT_LIGHTS : Use the default light notification
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        // Auto cancels the notification when clicked on in the task bar
        mBuilder.setAutoCancel(true);

        // Gets a NotificationManager which is used to notify the user of the background event
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Post the notification
        mNotificationManager.notify(1, mBuilder.build());

    }
}
