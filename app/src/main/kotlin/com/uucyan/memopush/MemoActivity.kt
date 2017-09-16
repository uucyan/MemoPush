package com.uucyan.memopush

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.TextView
import com.uucyan.memopush.model.Memo
import com.uucyan.memopush.view.MemoView
import com.uucyan.memopush.service.RealmService
import io.realm.Realm
//import jdk.nashorn.internal.objects.NativeDate.getTime
import java.text.SimpleDateFormat;
import android.R.id.edit
import android.text.SpannableStringBuilder
import android.widget.EditText
import android.content.DialogInterface
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.support.v4.app.NotificationManagerCompat
import me.mattak.moment.Moment
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.provider.CalendarContract
import com.uucyan.memopush.bindView
import java.util.*


/**
 * Created by Uucyan on 2017/08/27.
 */
class MemoActivity : AppCompatActivity() {

    // custom getter
    // リストからメモの選択またはメモを通知した際に対象メモのIDがセットされる
    // 新規登録の場合は0がセットされる
    private val memoId: Int
        get() = intent.getIntExtra("MEMO_ID", 0)

    private val titleEditText: EditText
        get() = findViewById<EditText>(R.id.title_edit)

    private val bodyEditText: EditText
        get() = findViewById<EditText>(R.id.body_edit)

    private val notificationTimeTextView: TextView
        get() = findViewById<TextView>(R.id.notification_time_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        // 編集画面の場合、登録してあるデータを入力欄にセットする
        if (!isCreate()) {
            setFieldData()
        }

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
            if (item.itemId == R.id.delete_memo) {
                // 編集画面の時だけ削除ボタンを表示する
                item.isVisible = !isCreate()
            }
        }

        return true
    }

    /**
     * アクションバーのボタンを押下した時の処理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.save_memo -> {
                if (isCreate()) {
                    createMemo()
                } else {
                    updateMemo()
                }
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
        val cal = Calendar.getInstance()
        cal.set(year, month, dayOfMonth)
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        notificationTimeTextView.setText(sdf.format(cal.getTime()))
    }

    /**
     * 既存のメモデータをフィールドにセットする
     */
    private fun setFieldData() {
        val realm = Realm.getDefaultInstance()
        val memo = realm.where(Memo::class.java).equalTo("id", memoId).findFirst()

        titleEditText.setText(memo!!.title)
        bodyEditText.setText(memo.body)
        notificationTimeTextView.setText(memo.notificationTime)
    }

    /**
     * メモの新規登録
     * TODO: 色々とイけてないからちゃんと考えたい
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
     * TODO: 色々とイけてないからちゃんと考えたい
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
     * TODO: 通知関連の処理はServiceにする
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
     * 新規作成かどうか判定
     */
    private fun isCreate(): Boolean = memoId == 0

    /**
     * ボタンの設定
     */
    private fun setButton(): Unit {
        findViewById<Button>(R.id.notification_button).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                onNotificationMemo()
            }
        })
    }

    /**
     * スイッチボタンの設定
     */
    private fun setCompoundButton(): Unit {
        val toggle = findViewById<CompoundButton>(R.id.notification_time_switch) as CompoundButton
        toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val datePicker = DatePickerDialogFragment()
                datePicker.show(supportFragmentManager, "datePicker")
            } else {
                notificationTimeTextView.setText("")
            }
        }
    }
}