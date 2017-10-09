package com.uucyan.memopush.receiver

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.uucyan.memopush.model.Memo
import com.uucyan.memopush.service.NotificationService
import io.realm.Realm


/**
 * Created by Uucyan on 2017/09/24.
 */
class AlarmReceiver : BroadcastReceiver() {

    var context: Context? = null

    // データを受信した
    override fun onReceive(context: Context, intent: Intent) {
        this.context = context

        val memoId = intent.getIntExtra("memoId", 0)

        // 通知対象のメモ情報を取得
        val memo = Realm.getDefaultInstance().where(Memo::class.java).equalTo("id", memoId).findFirst()

        if (memo is Memo) {
            NotificationService.sendMemo(context, memo)
        } else {
            Toast.makeText(context, "通知対象のメモが存在しません。", Toast.LENGTH_SHORT).show()
        }
    }
}