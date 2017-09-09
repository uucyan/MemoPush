package com.uucyan.memopush.service

import com.uucyan.memopush.model.Memo
import io.realm.Realm
import kotlin.jvm.javaClass

/**
 * Created by Uucyan on 2017/09/10.
 */
class RealmService {
    companion object{
        // staticメソッドっぽいidの生成処理
        // Realmではオートインクリメントができない && Kotlinで静的に定義できないため
        fun idGeneration(): Int {
            val realm = Realm.getDefaultInstance()

            // 登録が初の場合は1が登録される
            var nextId: Int = 1

            // idの最大値を取得（1件もレコードがなければnullで返ってくる）
            val maxId = realm.where(Memo::class.java).max("id")

            if (maxId != null) {
                nextId = maxId!!.toInt() + 1
            }

            return nextId
        }
    }
}