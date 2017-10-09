package com.uucyan.memopush

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast
import com.uucyan.memopush.model.Memo
import com.uucyan.memopush.service.NotificationService
import io.realm.Realm


/**
 * Created by Uucyan on 2017/10/03.
 *
 * 端末が再起動した時に実行される処理
 *
 * 端末の再起動がされてしまうと設定した通知がクリアされてしまうため、
 * メモの日時通知を再設定する。
 */
class StartupReceiver : BroadcastReceiver() {

//    var context: Context? = null

    override fun onReceive(context: Context, intent: Intent) {

        // Activityを開始する場合の処理（今は使わないのでコメントアウト）
//        val intentActivity = Intent(context, MainActivity::class.java)
//        intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(intentActivity)

        val memos =  Realm.getDefaultInstance().where(Memo::class.java).findAll()

        for (memo in memos) {
            if (memo.notificationTime.isBlank()) continue

            NotificationService.setAlarmMemo(context, memo)

            Toast.makeText(context, "再起動されたため、めもぷっしゅによる日付通知を再設定しました。", Toast.LENGTH_SHORT).show()
        }
    }
}