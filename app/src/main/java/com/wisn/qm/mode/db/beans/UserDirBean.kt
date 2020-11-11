package com.wisn.qm.mode.db.beans

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "userdirlist")
data class UserDirBean(

        @PrimaryKey
        @ColumnInfo(name = "id")
        @SerializedName("id")
        var id: Long,

        @ColumnInfo(name = "createattimelong")
        @SerializedName("createattimelong")
        var createattimelong: Long?,

        @ColumnInfo(name = "createattimestr")
        @SerializedName("createattimestr")
        var createattimestr: String?,

        @ColumnInfo(name = "filename")
        @SerializedName("filename")
        var filename: String?,

        @ColumnInfo(name = "path")
        @SerializedName("path")
        var path: String?,

        @ColumnInfo(name = "pid")
        @SerializedName("pid")
        var pid: Long?,

        @ColumnInfo(name = "sha1")
        @SerializedName("sha1")
        var sha1: String?,

        @ColumnInfo(name = "sha1_pre")
        @SerializedName("sha1_pre")
        var sha1_pre: String?,

        @ColumnInfo(name = "size")
        @SerializedName("size")
        var size: Int?,

        @ColumnInfo(name = "type")
        @SerializedName("type")
        var type: Int?,

        @ColumnInfo(name = "ftype")
        @SerializedName("ftype")
        var ftype: Int?,

        @ColumnInfo(name = "updatattimelong")
        @SerializedName("updatattimelong")
        var updatattimelong: Long?,

        @ColumnInfo(name = "minitype")
        @SerializedName("minitype")
        var minitype: String?,

        @ColumnInfo(name = "video_duration")
        @SerializedName("video_duration")
        var video_duration: String?,

        @ColumnInfo(name = "updatattimestr")
        @SerializedName("updatattimestr")
        var updatattimestr: String?,

        override var itemType: Int = 0
) : MultiItemEntity, Serializable {


    override fun toString(): String {
        return "UserDirBean(createattimelong=$createattimelong, createattimestr='$createattimestr', filename='$filename', id=$id, path='$path', pid=$pid, sha1='$sha1', sha1_pre='$sha1_pre', size=$size, type=$type, ftype=$ftype, updatattimelong=$updatattimelong, minitype='$minitype', video_duration='$video_duration', updatattimestr='$updatattimestr', itemType=$itemType)"
    }

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as UserDirBean

                if (id != other.id) return false
                if (sha1 != other.sha1) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id.hashCode()
                result = 31 * result + (sha1?.hashCode() ?: 0)
                return result
        }
}