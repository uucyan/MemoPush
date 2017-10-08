package com.uucyan.memopush.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.PrimaryKey

/**
 * Created by Uucyan on 2017/08/07.
 * 設定 データクラス
 */
@RealmClass
public data class Config(
        @PrimaryKey
        public var id: Int
) : RealmObject() {}