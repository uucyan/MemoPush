package com.uucyan.memopush

import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.R.attr.data
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast
import com.uucyan.memopush.model.Memo
import io.realm.Realm
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Uucyan on 2017/10/03.
 *
 * 端末が再起動した時に実行される処理
 *
 * 端末の再起動がされてしまうと設定した通知がクリアされてしまうため、
 * メモの日時通知を再設定する。
 */
class StartupReceiver : BroadcastReceiver() {

    var context: Context? = null

    override fun onReceive(context: Context, intent: Intent) {

        // Activityを開始する場合の処理（今は使わないのでコメントアウト）
//        val intentActivity = Intent(context, MainActivity::class.java)
//        intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(intentActivity)

        val memos =  Realm.getDefaultInstance().where(Memo::class.java).findAll()

        for (memo in memos) {
            if (memo.notificationTime == "") continue

            val intent = Intent(context, AlarmBroadcastReceiver::class.java)
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

//            Toast.makeText(context, "めもぷっしゅによる日付通知が再設定されました。", Toast.LENGTH_SHORT).show()
        }
    }
}