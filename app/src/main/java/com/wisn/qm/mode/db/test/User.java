package com.wisn.qm.mode.db.test;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Wisn on 2020/6/6 下午5:07.
 */
@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "user_name")
    public String username;
    @ColumnInfo(name = "e_mail")
    public String email;
    @Ignore
    public User() {
    }
    @Ignore
    public User(int uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
