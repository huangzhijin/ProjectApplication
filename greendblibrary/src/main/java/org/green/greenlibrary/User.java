package org.green.greenlibrary;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
    @Id
    private  Long UserId;

    @NotNull
    private  String name;

    private String  nick;

    private  String  age;

    @Convert(converter = LoveTypeConverter.class ,columnType = String.class)
   private   LoveType  loveType;

    public LoveType getLoveType() {
        return this.loveType;
    }

    public void setLoveType(LoveType loveType) {
        this.loveType = loveType;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return this.UserId;
    }

    public void setUserId(Long UserId) {
        this.UserId = UserId;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Generated(hash = 228440538)
    public User(Long UserId, @NotNull String name, String nick, String age,
            LoveType loveType) {
        this.UserId = UserId;
        this.name = name;
        this.nick = nick;
        this.age = age;
        this.loveType = loveType;
    }

    @Generated(hash = 586692638)
    public User() {
    }
}
