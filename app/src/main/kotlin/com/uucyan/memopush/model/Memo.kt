package com.uucyan.memopush.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.PrimaryKey

/**
 * Created by Uucyan on 2017/08/07.
 * メモ データクラス
 */
@RealmClass
public data class Memo(
        @PrimaryKey
        public var id: Int,
        public var title: String,
        public var body: String,
        public var notificationTime: String
) : RealmObject() {}