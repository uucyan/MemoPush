package com.uucyan.memopush

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import java.util.Calendar;

/**
 * Created by Uucyan on 2017/08/27.
 */
class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, this, year, month, dayOfMonth)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // 日付が選択されたときの処理
        val activity = activity
        if (activity is MemoActivity) {
            activity.setDate(year, month, day)
            // 日時選択のダイアログを表示
            activity.showTimpicker()
        }
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)

        val activity = activity
        if (activity is MemoActivity) {
            activity.unsetCheckAndClearDateTime()
        }
    }
}