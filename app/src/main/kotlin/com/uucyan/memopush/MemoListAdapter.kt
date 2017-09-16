package com.uucyan.memopush

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.uucyan.memopush.model.Memo
import com.uucyan.memopush.view.MemoView
import java.text.FieldPosition

/**
 * Created by Uucyan on 2017/08/27.
 */
class MemoListAdapter(private val context: Context) : BaseAdapter() {

    var memos: List<Memo> = emptyList()

    override fun getCount(): Int = memos.size

    override fun getItem(position: Int): Any? = memos[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int,
                         convertView: View?,
                         parent: ViewGroup?): View =
            ((convertView as? MemoView) ?: MemoView(context)).apply {
                setMemo(memos[position])
            }

}
