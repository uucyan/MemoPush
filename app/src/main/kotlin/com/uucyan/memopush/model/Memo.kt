package com.uucyan.memopush.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Uucyan on 2017/08/07.
 * メモ データクラス
 */
data class Memo(val id: String,
                val title: String,
                val body: String,
                val notificationTime: String) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Memo> = object : Parcelable.Creator<Memo> {
            override fun createFromParcel(source: Parcel): Memo = source.run {
                Memo(readString(), readString(), readString(), readString())
            }

            override fun newArray(size: Int): Array<Memo?> = arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.run {
            writeString(id)
            writeString(title)
            writeString(body)
            writeString(notificationTime)
        }
    }
}