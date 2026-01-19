package edu.towson.cosc435.labsapp.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import edu.towson.cosc435.labsapp.MainActivity
import edu.towson.cosc435.labsapp.network.SongsFetcher
import kotlinx.coroutines.delay

class DownloadImageWorker(
    private val ctx: Context,
    params: WorkerParameters
):
    CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val image_uri = inputData.getString(INPUT_KEY) ?: return Result.failure()
        val song_name = inputData.getString(INPUT_NAME_KEY) ?: return Result.failure()

        val notification = createNotification(null)
        setForeground(ForegroundInfo(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE))
        delay(15 * 1000)
        val bitmap = SongsFetcher(this.ctx).fetchIcon(image_uri)
        val done = createNotification(bitmap)
        // TODO 2. Check if permissions are granted
        if(ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(ctx).notify(1, done)
        }
        return Result.success()
    }

    private fun createNotification(bitmap: Bitmap? = null): Notification {
        createNotificationChannel()
        val mainActivityIntent = Intent(ctx, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(ctx, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)
        var notification: Notification = Notification()
        if(bitmap == null) {
            notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Downloading icon...")
                .setProgress(0, 0, true)
                .setContentIntent(pendingIntent)
                .build()
        } else {
            // build a notification and show the bitmap
            notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setContentIntent(pendingIntent)
                .build()
        }
        return notification
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Labs App Notification"
            val descriptionText = "Notification channel for LabsApp"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        val CHANNEL_ID = "edu.towson.cosc435.labsapp.channel"
        val INPUT_KEY = "IMAGE_URI"
        val INPUT_NAME_KEY = "IMAGE_NAME"
    }
}