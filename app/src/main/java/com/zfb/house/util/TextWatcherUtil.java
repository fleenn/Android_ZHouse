package com.zfb.house.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * EditText输入字数限制的工具类
 * 中文按两个，英文按一个
 * <p/>
 * Created by Administrator on 2016/7/13.
 */
public class TextWatcherUtil {

    private EditText editText;
    private int maxLength;

    public TextWatcherUtil(EditText editText, int maxLength) {
        this.editText = editText;
        this.maxLength = maxLength;
    }

    /**
     * 获得TextWatcher
     *
     * @return
     */
    public TextWatcher getTextWatcher() {
        return textWatcher;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = editText.getSelectionStart();
            editEnd = editText.getSelectionEnd();
            //先去掉监听器，否则会出现栈溢出
            editText.removeTextChangedListener(textWatcher);
            if (!TextUtils.isEmpty(editText.getText())) {
                while (calculateLength(editText.getText().toString().trim()) > maxLength) {
                    s.delete(editStart - 1, editEnd);
                    editStart--;
                    editEnd--;
                }
            }
            editText.setText(s);
            editText.setSelection(editStart);
            //恢复监听器
            editText.addTextChangedListener(textWatcher);
        }

    };

    private int calculateLength(String str) {
        int resultLength = 0;
        for (char c : str.toCharArray()) {
            //中文字符范围0x4e00 0x9fbb
            if ((c >= 0x2E80 && c <= 0xFE4F) || (c >= 0xA13F && c <= 0xAA40) || c >= 0x80) {
                resultLength = resultLength + 2;
            } else {
                resultLength++;
            }
        }
        return resultLength;
    }

}
