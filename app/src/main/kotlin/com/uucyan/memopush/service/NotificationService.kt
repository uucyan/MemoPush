package com.uucyan.memopush.service

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import com.uucyan.memopush.receiver.AlarmReceiver
import com.uucyan.memopush.MemoActivity
import com.uucyan.memopush.R
import com.uucyan.memopush.model.Memo
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Uucyan on 2017/10/04.
 */
class NotificationService {
    companion object {

        /**
         * メモの通知
         */
        fun sendMemo(context: Context, memo: Memo) {
            // 通知したメモのタップ時にメモの情報を取得するため、IDをセットしておく
            val resultIntent = Intent(context, MemoActivity::class.java)
            resultIntent.putExtra("MEMO_ID", memo.id)

            // 通知タップで起動したメモの編集画面で戻るボタンを押した時、
            // AndroidManifestに定義した親クラスに遷移させるための設定処理。
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(MemoActivity::class.java)
            stackBuilder.addNextIntent(resultIntent)
            val resultPendingIntent = stackBuilder.getPendingIntent(memo.id, PendingIntent.FLAG_UPDATE_CURRENT)

            // 通知情報をセット
            val builder = android.support.v4.app.NotificationCompat.Builder(context)
            builder.setSmallIcon(R.mipmap.ic_notification_icon)
            builder.setContentTitle(memo.title)
            builder.setContentText(memo.body)
            builder.setWhen(System.currentTimeMillis())
            builder.setDefaults(Notification.DEFAULT_ALL)
            builder.setContentIntent(resultPendingIntent);

            // 最近のAndroidバージョンだと必要なさげ
            val bigTextStyle = android.support.v4.app.NotificationCompat.BigTextStyle(builder)
            bigTextStyle.bigText(memo.body)

            val manager = NotificationManagerCompat.from(context)
            manager.notify(memo.id, builder.build())
        }

        /**
         * メモのアラーム通知設定
         */
        fun setAlarmMemo(context: Context, memo: Memo) {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra("memoId", memo.id)
            val pendingIntent = PendingIntent.getBroadcast(context, memo.id, intent, PendingIntent.FLAG_CANCEL_CURRENT)

            // 通知のアラーム日時を取得して設定
            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
            val formatDate = sdf.parse(memo.notificationTime)
            val calendar = Calendar.getInstance()
            calendar.setTime(formatDate)

            // アラームをセットする
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
        }

    }
}