package com.uucyan.memopush.view

import android.content.Context
//import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
//import android.widget.ImageView
import android.widget.TextView
import com.uucyan.memopush.R
import com.uucyan.memopush.model.Memo

/**
 * Created by Uucyan on 2017/08/09.
 * メモ一覧に表示するメモ
 */

class MemoView : FrameLayout {
    constructor(context: Context?) : super(context)

    constructor(context: Context?,
                attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?,
                attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context?,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    var titleTextView: TextView? = null

    var bodyTextView: TextView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_memo, this)
        titleTextView = findViewById<TextView>(R.id.title_text_view)
        bodyTextView = findViewById<TextView>(R.id.body_text_view)
    }

    fun setMemo(memo: Memo) {
        titleTextView?.text = memo.title
        bodyTextView?.text = memo.body
    }
}