package com.uucyan.memopush

import android.content.Context.NOTIFICATION_SERVICE
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;
import android.util.Log;
import com.uucyan.memopush.model.Memo
import io.realm.Realm


/**
 * Created by Uucyan on 2017/09/24.
 */
class AlarmBroadcastReceiver : BroadcastReceiver() {

    var context: Context? = null

    // データを受信した
    override fun onReceive(context: Context, intent: Intent) {

//        this.context = context
//
//        Log.d("AlarmBroadcastReceiver", "onReceive() pid=" + android.os.Process.myPid())
//
//        val memoId = intent.getIntExtra("memoId", 0)
//
//        val intent2 = Intent(context, MemoActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(context, memoId, intent2, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notification = NotificationCompat.Builder(context)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setTicker("時間です")
//                .setWhen(System.currentTimeMillis())
//                .setContentTitle("TestAlarm " + memoId)
//                .setContentText("時間になりました")
//                // 音、バイブレート、LEDで通知
//                .setDefaults(Notification.DEFAULT_ALL)
//                // 通知をタップした時にMainActivityを立ち上げる
//                .setContentIntent(pendingIntent)
//                .build()
//
//        // 古い通知を削除
//        notificationManager.cancelAll()
//        // 通知
//        notificationManager.notify(R.string.app_name, notification)

        this.context = context

        val memoId = intent.getIntExtra("memoId", 0)

        // 通知対象のメモ情報を取得
        val memo = Realm.getDefaultInstance().where(Memo::class.java).equalTo("id", memoId).findFirst()

        val resultIntent = Intent(context, MemoActivity::class.java)
        // 通知したメモのタップ時にメモの情報を取得するため、IDをセットしておく
        resultIntent.putExtra("MEMO_ID", memoId)

        // 通議から起動したメモの編集画面から戻るボタンを押した時、
        // AndroidManifestに定義した親クラスに遷移させるための設定処理。
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MemoActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(memoId, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = android.support.v4.app.NotificationCompat.Builder(context)
        builder.setSmallIcon(R.drawable.ic_action_add_memo)
        builder.setContentTitle(memo?.title)
        builder.setContentText(memo?.body)
        builder.setWhen(System.currentTimeMillis())
        builder.setDefaults(Notification.DEFAULT_ALL)
        builder.setContentIntent(resultPendingIntent);

        // 最近のAndroidバージョンだと必要なさそう…。
        val bigTextStyle = android.support.v4.app.NotificationCompat.BigTextStyle(builder)
        bigTextStyle.bigText(memo?.body)

        val manager = NotificationManagerCompat.from(context)
        manager.notify(memoId, builder.build())
    }
}