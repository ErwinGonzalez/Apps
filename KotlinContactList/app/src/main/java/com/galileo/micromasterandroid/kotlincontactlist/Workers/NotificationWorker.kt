package com.galileo.micromasterandroid.kotlincontactlist.Workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.v4.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.galileo.micromasterandroid.kotlincontactlist.R

class NotificationWorker (context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters){

    companion object {
        const val EXTRA_NAME = "contact_name"
        const val EXTRA_PHONE = "contact_phone"
    }

    override fun doWork(): Result {

        val name = inputData.getString(EXTRA_NAME)
        val phone = inputData.getString(EXTRA_PHONE)
        sendNotification(name!!,phone!!)
        return Result.success()
    }

    fun sendNotification(name: String, phone:String){
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel("100","Default Channel",NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification :NotificationCompat.Builder = NotificationCompat.Builder(
                    applicationContext,
                    "100")
                    .setContentTitle("Remember to Call "+name)
                    .setContentText(name+"'s phone number is: "+phone)
                    .setSmallIcon(R.mipmap.notification_icon)

        notificationManager.notify(1,notification.build())
    }



}