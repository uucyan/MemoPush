package com.uucyan.memopush

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.uucyan.memopush.model.Memo
import com.uucyan.memopush.view.MemoView

/**
 * Created by Uucyan on 2017/08/27.
 */
class MemoActivity : AppCompatActivity() {
    companion object {

        private const val MEMO_EXTRA: String = "memo"

        fun intent(context: Context, memo: Memo): Intent =
                Intent(context, MemoActivity::class.java).putExtra(MEMO_EXTRA, memo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        val memoView = findViewById<MemoView>(R.id.memo_view) as MemoView

        val memo: Memo = intent.getParcelableExtra(MEMO_EXTRA)
        memoView.setMemo(memo)
    }
}