package com.uucyan.memopush.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Uucyan on 2017/08/07.
 * メモ データクラス
 */
public open class Memo(
        @PrimaryKey
        public open var id: Int = 0,
        public open var title: String = "",
        public open var body: String = "",
        public open var notificationTime: String = ""
) : RealmObject() {}