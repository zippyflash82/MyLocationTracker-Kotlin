package com.example.mylocationtracker

import android.content.Context
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.LocalDateTime

class BackgroundLocationUpload (appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    @RequiresApi(Build.VERSION_CODES.O)
    var location : Location ?=null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val localDateTime = LocalDateTime.now().toString()



        val lat = inputData.getString("lat")
        val long = inputData.getString("long")

        println("lat: $lat, long: $long")

       //println("Service is Running at $localDateTime ")

        return Result.success()
    }
}