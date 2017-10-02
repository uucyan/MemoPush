package com.uucyan.memopush

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.EditText
import android.content.DialogInterface
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.support.v4.app.NotificationManagerCompat
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context;
import java.util.*
import io.realm.Realm
import me.mattak.moment.Moment
import com.uucyan.memopush.model.Memo
import com.uucyan.memopush.service.RealmService
import android.widget.Toast
import android.app.AlarmManager
import java.text.SimpleDateFormat


/**
 * Created by Uucyan on 2017/08/27.
 */
class MemoActivity : AppCompatActivity() {

    // MemoActivity実行時に渡ってきたメモIDを返却する
    private val memoId: Int
        get() = intent.getIntExtra("MEMO_ID", 0)

    private val titleEditText: EditText
        get() = findViewById<EditText>(R.id.title_edit)

    private val bodyEditText: EditText
        get() = findViewById<EditText>(R.id.body_edit)

    private val notificationTimeTextView: TextView
        get() = findViewById<TextView>(R.id.notification_time_view)

    private val toggleButton: CompoundButton
        get() = findViewById<CompoundButton>(R.id.notification_time_switch) as CompoundButton

    private var notificationDateTime: MutableMap<String, Int> =
            mutableMapOf("year" to 0, "month" to 0, "dayOfMonth" to 0, "hourOfDay" to 0, "minute" to 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        // 編集画面の場合、登録してあるデータを入力欄にセットする
        if (!isCreate()) setFieldData()

        setButton()
        setCompoundButton()
    }

    /**
     * アクションバーにボタン追加
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.memo_menu_buttons, menu)

        for (i in 0..menu.size() - 1) {
            val item = menu.getItem(i)
            // 編集画面の時だけ削除ボタンを表示する
            if (item.itemId == R.id.delete_memo) item.isVisible = !isCreate()
        }

        return true
    }

    /**
     * アクションバーのボタンを押下した時の処理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.save_memo -> {
                if (isCreate()) createMemo() else updateMemo()
                if (toggleButton.isChecked) setAlarmNotification()
                finish()
            }
            R.id.delete_memo -> {
                val alertDlg = AlertDialog.Builder(this)
                alertDlg.setTitle("メモの削除")
                alertDlg.setMessage("本当に削除しますか？")
                alertDlg.setPositiveButton(
                        "OK",
                        DialogInterface.OnClickListener { dialog, which ->
                            deleteMemo()
                            finish()
                        })
                alertDlg.setNegativeButton(
                        "Cancel",
                        DialogInterface.OnClickListener { dialog, which ->
                        })
                alertDlg.create().show()
            }
        }

        return true
    }

    /**
     * カレンダーで選択した年月日のセット
     */
    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        notificationDateTime["year"] = year
        notificationDateTime["month"] = month
        notificationDateTime["dayOfMonth"] = dayOfMonth
    }

    /**
     * タイムピッカーで選択した時間のセット
     */
    fun setTime(hourOfDay: Int, minute: Int) {
        notificationDateTime["hourOfDay"] = hourOfDay
        notificationDateTime["minute"] = minute
    }

    /**
     * 選択した日時を画面に表示
     */
    fun showDateTime() {
        val calendar = Calendar.getInstance()
        calendar.set(notificationDateTime["year"]!!, notificationDateTime["month"]!!, notificationDateTime["dayOfMonth"]!!, notificationDateTime["hourOfDay"]!!, notificationDateTime["minute"]!!)
        notificationTimeTextView.setText(Moment(calendar.time).format("yyyy/MM/dd HH:mm"))
    }

    /**
     * スイッチのチェックを外して日時をクリアする
     */
    fun unsetCheckAndClearDateTime() {
        toggleButton.setChecked(false)
        notificationTimeTextView.setText("")
    }

    /**
     * 時間選択のダイアログを表示
     * 日付選択ダイアログでOKを押した場合に呼び出される
     */
    fun showTimpicker() {
        TimePickerDialogFragment().show(supportFragmentManager, "timePicker")
    }

    /**
     * 既存のメモデータをフィールドにセットする
     */
    private fun setFieldData() {
        val memo = Realm.getDefaultInstance().where(Memo::class.java).equalTo("id", memoId).findFirst()

        titleEditText.setText(memo?.title)
        bodyEditText.setText(memo?.body)
        notificationTimeTextView.setText(memo?.notificationTime)
    }

    /**
     * メモの新規登録
     */
    private fun createMemo() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                val memo = realm.createObject(Memo::class.java, RealmService.idGeneration())

                // 登録
                memo.title = titleEditText.getText().toString()
                memo.body = bodyEditText.getText().toString()
                memo.notificationTime = notificationTimeTextView.getText().toString()
            }
        }
    }

    /**
     * メモの更新
     */
    private fun updateMemo() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                val memo = realm.where(Memo::class.java).equalTo("id", memoId).findFirst()

                // 登録
                memo?.title = titleEditText.getText().toString()
                memo?.body = bodyEditText.getText().toString()
                memo?.notificationTime = notificationTimeTextView.getText().toString()
            }
        }
    }

    /**
     * メモの削除
     */
    private fun deleteMemo() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                val memo = realm.where(Memo::class.java).equalTo("id", memoId).findFirst()
                memo?.deleteFromRealm()
            }
        }
    }

    /**
     * メモの通知
     * TODO: どうにかしたい
     */
    private fun onNotificationMemo() {
        val builder = NotificationCompat.Builder(applicationContext)
        builder.setSmallIcon(R.drawable.ic_action_add_memo)
        builder.setContentTitle(titleEditText.getText().toString())
        builder.setContentText(bodyEditText.getText().toString())
        builder.setWhen(System.currentTimeMillis())

        val resultIntent = Intent(this, MemoActivity::class.java)
        // 通知したメモのタップ時にメモの情報を取得するため、IDをセットしておく
        resultIntent.putExtra("MEMO_ID", memoId)

        // 通議から起動したメモの編集画面から戻るボタンを押した時、
        // AndroidManifestに定義した親クラスに遷移させるための設定処理。
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MemoActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(memoId, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(resultPendingIntent);

        // 最近のAndroidバージョンだと必要なさそう…。
        val bigTextStyle = NotificationCompat.BigTextStyle(builder)
        bigTextStyle.bigText(bodyEditText.getText().toString())

        val manager = NotificationManagerCompat.from(applicationContext)
        manager.notify(memoId, builder.build())
    }

    /**
     * メモの通知アラームを設定
     */
    private fun setAlarmNotification() {
        val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
        intent.putExtra("memoId", memoId)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, memoId, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        // 通知のアラーム日時を取得して設定
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
        val formatDate = sdf.parse(notificationTimeTextView.getText().toString())
        val calendar = Calendar.getInstance()
        calendar.setTime(formatDate)

        // アラームをセットする
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)

        // トーストで設定されたことをを表示
        Toast.makeText(applicationContext, "${notificationTimeTextView.getText().toString()} に通知されます", Toast.LENGTH_SHORT).show()
    }

    /**
     * 新規作成かどうか判定
     */
    private fun isCreate(): Boolean = memoId == 0

    /**
     * ボタンの設定
     */
    private fun setButton() {
        findViewById<Button>(R.id.notification_button).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                onNotificationMemo()
            }
        })
    }

    /**
     * スイッチボタンの設定
     */
    private fun setCompoundButton() {
        // 日付が登録されていればチェックをつける
        if (!notificationTimeTextView.getText().toString().isBlank()) toggleButton.setChecked(true)

        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                DatePickerDialogFragment().show(supportFragmentManager, "datePicker")
            } else {
                notificationTimeTextView.setText("")
            }
        }
    }
}