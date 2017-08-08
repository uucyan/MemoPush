package com.uucyan.memopush

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.uucyan.memopush.model.*
import com.uucyan.memopush.view.MemoView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        val memoView = MemoView(applicationContext)

        memoView.setMemo(Memo(id = "1",
                title = "テストメモ",
                body = "これはテスト用のメモです。",
                notificationTime = "2017-08-01 10:00:00"))

        setContentView(memoView)
    }
}
