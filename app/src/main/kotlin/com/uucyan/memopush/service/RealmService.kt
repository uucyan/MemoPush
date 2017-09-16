package com.uucyan.memopush.service

import com.uucyan.memopush.model.Memo
import io.realm.Realm

/**
 * Created by Uucyan on 2017/09/10.
 */
class RealmService {
    companion object {
        /**
         * idの生成
         * whereで取得できなければ1を返却
         */
        fun idGeneration(): Number {
            var id: Number? = Realm.getDefaultInstance().where(Memo::class.java).max("id")
            if (id != null) id = id.toInt() + 1
            return id ?: 1
        }
    }
}