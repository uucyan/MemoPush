package com.uucyan.memopush

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.support.v4.content.ContextCompat
import io.realm.Realm
import com.uucyan.memopush.model.Memo


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setMemoListView()
    }

    /**
     * 他の画面から戻った時、メモ一覧を再取得
     */
    override fun onRestart() {
        super.onRestart()
        setMemoListView()
    }

    /**
     * アクションバーにボタン追加
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu_buttons, menu)

        // メモ追加ボタンの着色
        val addMemoButton = getDrawable(R.drawable.ic_action_add_memo)
        addMemoButton.setTint(ContextCompat.getColor(this, R.color.colorIcon))

        return true
    }

    /**
     * アクションバーのボタンを押下した時の処理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == R.id.add_memo) startActivity(Intent(this, MemoActivity::class.java))
        return true
    }

    /**
     * メモ一覧のセット
     */
    private fun setMemoListView() {
        val listAdapter = MemoListAdapter(applicationContext)

        listAdapter.memos = Realm.getDefaultInstance().where(Memo::class.java).findAll()

        val listView: ListView = findViewById<ListView>(R.id.list_view) as ListView
        listView.adapter = listAdapter
        listView.setOnItemClickListener { adapterView, view, position, id ->
            val memo = listAdapter.memos[position]
            val intent = Intent(this, MemoActivity::class.java)

            intent.putExtra("MEMO_ID", memo.id)
            startActivity(intent)
        }
    }
}
