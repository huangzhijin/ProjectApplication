package com.click.cn.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.click.cn.view.contact.Contact;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class ContactUtil {
    public static String getPinyin(@NonNull Contact contact) {
        String nickname = contact.getNickname();
        String pinyin = "";
        if (TextUtils.isEmpty(nickname)) {
            pinyin = "";
        } else {
            Pinyin4j pinyin4j = new Pinyin4j();
            try {
                pinyin = pinyin4j.toPinYinUppercaseInitials(nickname);

            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }
        pinyin = "0".equals(pinyin) ? "#" : pinyin;
        return pinyin;
    }
}
