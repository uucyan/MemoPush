package com.uucyan.memopush.model

/**
 * Created by Uucyan on 2017/08/07.
 * メモ データクラス
 */
data class Memo(val id: String,
                val title: String,
                val body: String,
                val notificationTime: String)