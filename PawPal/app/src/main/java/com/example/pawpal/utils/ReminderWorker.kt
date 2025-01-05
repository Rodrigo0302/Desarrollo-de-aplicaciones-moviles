package com.example.pawpal.utils


import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters


class ReminderWorker(private val context:Context,params:WorkerParameters):Worker(context,params) {
    override fun doWork(): Result {


        NotificationHelper(context).createNotification(
            inputData.getString("Title").toString(),
            inputData.getString("Message").toString()
        )
        Log.d("NotificationMas","Recordatorio creado en el worker")
        return Result.success()
    }
}