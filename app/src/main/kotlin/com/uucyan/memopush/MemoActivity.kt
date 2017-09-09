package com.uucyan.memopush

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CompoundButton
import android.widget.TextView
import com.uucyan.memopush.model.Memo
import com.uucyan.memopush.view.MemoView
//import jdk.nashorn.internal.objects.NativeDate.getTime
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        val memoId = intent.getIntExtra(MainActivity.EXTRA_MEMO_ID, 0)

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
     * カレンダーで選択した年月日のセット
     */
    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        val notificationTimeView = findViewById<TextView>(R.id.notification_time_view)
        val cal = Calendar.getInstance()
        cal.set(year, month, dayOfMonth)
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        notificationTimeView.setText(sdf.format(cal.getTime()))
    }
}