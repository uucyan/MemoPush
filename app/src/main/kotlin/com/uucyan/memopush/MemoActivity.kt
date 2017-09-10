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
import java.util.Calendar;
import android.R.id.edit
import android.text.SpannableStringBuilder
import android.widget.EditText



/**
 * Created by Uucyan on 2017/08/27.
 */
class MemoActivity : AppCompatActivity() {

//    val notificationTimeView: TextView by bindView<TextView>(R.id.notification_time_view)
//    private val notificationTimeView: TextView? = null

//    companion object {
//
//        private const val MEMO_EXTRA: String = "memo"
//
//        fun intent(context: Context, memo: Memo): Intent =
//                Intent(context, MemoActivity::class.java).putExtra(MEMO_EXTRA, memo)
//    }

    // custom getterで保存・編集対象のIDを定義
    // 新規登録の場合 0 が入る
    private val memoId: Int
        get() = intent.getIntExtra("MEMO_ID", 0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        // idが渡ってきた場合0ではないため、編集画面として既存のデータをセットする
        if (this.memoId != 0) {
            this.setFieldData()
        }

        val memoView = findViewById<MemoView>(R.id.memo_view) as MemoView
//        val memo: Memo = intent.getParcelableExtra(MEMO_EXTRA)
//        memoView.setMemo(memo)

        val toggle = findViewById<CompoundButton>(R.id.notification_time_switch) as CompoundButton
        toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val datePicker = DatePickerDialogFragment()
                datePicker.show(supportFragmentManager, "datePicker")
            } else {
                val notificationTimeView = findViewById<TextView>(R.id.notification_time_view)
                notificationTimeView.setText("")
            }
        }
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
                item.isVisible = this.memoId != 0
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
                if (this.memoId == 0) {
                    this.createMemo()
                } else {
                    this.updateMemo()
                }
            }
            R.id.delete_memo -> {
                this.deleteMemo()
            }
        }

        finish()
        return true
    }

    /**
     * カレンダーで選択した年月日のセット
     */
    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        val notificationTimeView = findViewById<TextView>(R.id.notification_time_view)
        val cal = Calendar.getInstance()
        cal.set(year, month, dayOfMonth)
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        notificationTimeView.setText(sdf.format(cal.getTime()))
    }

    /**
     * 既存のメモデータをフィールドにセットする
     */
    private fun setFieldData() {
        val realm = Realm.getDefaultInstance()
        val memo = realm.where(Memo::class.java).equalTo("id", this.memoId).findFirst()

        val title = findViewById<EditText>(R.id.title_edit)
        val body = findViewById<EditText>(R.id.body_edit)
        val notificationTime = findViewById<TextView>(R.id.notification_time_view)

        title.setText(memo!!.title)
        body.setText(memo.body)
        notificationTime.setText(memo.notificationTime)
    }

    /**
     * メモの新規登録
     * TODO: 色々とイけてないからちゃんと考えたい
     */
    private fun createMemo() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                val memo = realm.createObject(Memo::class.java, RealmService.idGeneration())

                // 入力値を取得
                val title = findViewById<EditText>(R.id.title_edit)
                val body = findViewById<EditText>(R.id.body_edit)
                val notificationTime = findViewById<TextView>(R.id.notification_time_view)

                // 登録
                memo.title = title.getText().toString()
                memo.body = body.getText().toString()
                memo.notificationTime = notificationTime.getText().toString()
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
                val memo = realm.where(Memo::class.java).equalTo("id", this.memoId).findFirst()

                // 入力値を取得
                val title = findViewById<EditText>(R.id.title_edit)
                val body = findViewById<EditText>(R.id.body_edit)
                val notificationTime = findViewById<TextView>(R.id.notification_time_view)

                // 登録
                memo?.title = title.getText().toString()
                memo?.body = body.getText().toString()
                memo?.notificationTime = notificationTime.getText().toString()
            }
        }
    }

    /**
     * メモの削除
     */
    private fun deleteMemo() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                val memo = realm.where(Memo::class.java).equalTo("id", this.memoId).findFirst()
                memo?.deleteFromRealm()
            }
        }
    }
}