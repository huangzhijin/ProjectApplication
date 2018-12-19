package com.click.cn.util;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

public abstract class InputFilterMoney implements InputFilter, TextWatcher {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 删除等特殊字符，直接返回
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        String dValue = dest.toString();
        String[] splitArray = dValue.split("\\.");
        if (splitArray.length > 1) {
            String dotValue = splitArray[1];
            // 2 表示输入框的小数位数
            int diff = dotValue.length() + 1 - 2;
            if (diff > 0) {
                return source.subSequence(start, end - diff);
            }
        }
        return null;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        this.beforeTextChangedMoney(charSequence, i, i1, i2);
    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        //第一个字符不为小数点
        if (s.length() == 1 && s.toString().equals(".")) {
            setText("");
            return;
        }
        int counter = counter(s.toString(), '.');
        if (counter > 1) {
            //小数点第一次出现的位置
            int index = s.toString().indexOf('.');
            setText(s.subSequence(0, index + 1));
        }
        this.onTextChangedMoney(s, i, i1, i2);
    }


    @Override
    public void afterTextChanged(Editable editable) {
        this.afterTextChangedMoney(editable);
    }

    public int counter(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    protected abstract TextView getEditView();

    protected abstract void beforeTextChangedMoney(CharSequence charSequence, int i, int i1, int i2);

    protected abstract void onTextChangedMoney(CharSequence s, int i, int i1, int i2);

    protected abstract void afterTextChangedMoney(Editable editable);

    private void setText(CharSequence text) {
        TextView textView = getEditView();
        if (textView != null)
            textView.setText(text);
    }

}
