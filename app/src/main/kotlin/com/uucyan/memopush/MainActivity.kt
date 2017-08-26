package com.uucyan.memopush

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.uucyan.memopush.model.Memo
import com.uucyan.memopush.view.MemoView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val memoView = MemoView(applicationContext)

        val listAdapter = MemoListAdapter(applicationContext)
        listAdapter.memo = listOf(
                Memo(id = "1", title = "テストメモ１", body = "これは一つ目のメモです。", notificationTime = "2017-08-27 10:00:00"),
                Memo(id = "1", title = "テストメモ２", body = "これは二つ目のメモです。", notificationTime = "2017-08-01 10:00:00"))

        val listView: ListView = findViewById<ListView>(R.id.list_view) as ListView
        listView.adapter = listAdapter
        listView.setOnItemClickListener { adapterView, view, position, id ->
            val memo = listAdapter.memo[position]
            MemoActivity.intent(this, memo).let { startActivity(it) }
        }

//        memoView.setMemo(Memo(id = "1",
//                title = "テストメモ",
//                body = "これはテスト用のメモです。",
//                notificationTime = "2017-08-01 10:00:00"))
//
//        setContentView(memoView)
    }
}
