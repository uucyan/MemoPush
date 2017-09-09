package com.uucyan.memopush.service

import io.realm.Realm

/**
 * Created by Uucyan on 2017/09/10.
 */
class RealmService {
    companion object{
        // staticメソッドっぽいidの生成処理
        // Realmではオートインクリメントができない && Kotlinで静的に定義できないため
        fun idGeneration(): Int {
            var id : Int = 0

            val realm = Realm.getDefaultInstance()


            return id
        }
    }
}