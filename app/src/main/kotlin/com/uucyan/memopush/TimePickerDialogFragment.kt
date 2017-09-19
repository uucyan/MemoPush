package com.uucyan.memopush

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import java.util.Calendar;

/**
 * Created by Uucyan on 2017/08/28.
 * 日時のカレンダーはまだ未使用
 */
class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // 時刻が選択されたときの処理
        val activity = activity
        if (activity is MemoActivity) {
            activity.setTime(hourOfDay, minute)
            // メモの編集画面に選択した日時を表示
            activity.showDateTime()
        }
    }

}