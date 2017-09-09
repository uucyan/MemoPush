package com.uucyan.memopush

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.uucyan.memopush.model.Memo
import android.support.v4.content.ContextCompat
import io.realm.Realm


class MainActivity : AppCompatActivity() {

    companion object Factory {
        val EXTRA_MEMO_ID = "com.uucyan.memopush.MainActivity.EXTRA_MEMO_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val memoView = MemoView(applicationContext)

        val listAdapter = MemoListAdapter(applicationContext)

        val realm = Realm.getDefaultInstance()
        listAdapter.memo = realm.where(Memo::class.java).findAll()

//        Realm.getDefaultInstance().use { realm ->
//            realm.where(Memo::class.java).findAll().forEach {
////                var memos = it
//            }
//            listAdapter.memo = realm.where(Memo::class.java).findAll()
//
//        }

//        val listAdapter = MemoListAdapter(applicationContext)
//        listAdapter.memo = listOf(
//                Memo(id = 1, title = "テストメモ１", body = "これは一つ目のメモです。", notificationTime = "2017-08-27 10:00:00"),
//                Memo(id = 2, title = "テストメモ２", body = "これは二つ目のメモです。", notificationTime = "2017-08-01 10:00:00"))

        val listView: ListView = findViewById<ListView>(R.id.list_view) as ListView
        listView.adapter = listAdapter
        listView.setOnItemClickListener { adapterView, view, position, id ->
            val memo = listAdapter.memo[position]
//            MemoActivity.intent(this, memo).let { startActivity(it) }
            val intent = Intent(this, MemoActivity::class.java)
            intent.putExtra("EXTRA_MEMO_ID", memo.id)
            startActivity(intent)
        }


//        memoView.setMemo(Memo(id = "1",
//                title = "テストメモ",
//                body = "これはテスト用のメモです。",
//                notificationTime = "2017-08-01 10:00:00"))
//
//        setContentView(memoView)
    }

    /**
     * アクションバーにボタン追加
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu_buttons, menu)

        // メモ追加ボタンの色着色
        val addMemoButton = getDrawable(R.drawable.ic_action_add_memo)
        addMemoButton!!.setTint(ContextCompat.getColor(this, R.color.colorIcon))

        return true
    }

    /**
     * アクションバーのボタンを押下した時の処理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == R.id.add_memo) {
            val intent = Intent(this, MemoActivity::class.java)
            startActivity(intent)
        }
        return true
    }
}
