package com.uucyan.memopush.service

import com.uucyan.memopush.model.Memo
import io.realm.Realm

/**
 * Created by Uucyan on 2017/09/10.
 */
class RealmService {
    companion object {
        val firstid: Int = 1

        /**
         * idの生成
         * whereで取得できなければデータが存在しないため1を返却。
         * TODO: 現状Memoクラスにしか対応できていない。動的にやるの辛みだった。
         */
        fun generateId(): Int {
            val id: Number? = Realm.getDefaultInstance().where(Memo::class.java).max("id")
            return if (id == null) firstid else id.toInt().plus(1)
        }
    }
}