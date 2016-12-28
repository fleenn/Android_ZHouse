package com.zfb.house.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.util.TextWatcherUtil;

/**
 * 个人信息->编辑昵称
 * Created by Administrator on 2016/6/1.
 */
@Layout(id = R.layout.activity_modify_alise)
public class ModifyAliasActivity extends LemonActivity {

    private static final String TAG = "ModifyAliasActivity";
    //昵称
    @FieldView(id = R.id.modify_name_edt)
    private EditText edtName;
    //角色
    private String userType;

    @Override
    protected void initView() {
        setCenterText(R.string.title_edit_alias);
        setRtText(R.string.save);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        //初始化输入框的值
        edtName.setText(intent.getStringExtra("name"));
        //获得焦点，让光标停在句末
        edtName.requestFocus();
        //经纪人：限制四个中文，用户：限制六个中文
        userType = intent.getStringExtra("userType");
        edtName.setHint(userType.equals("1") ? R.string.hint_limit_broker : R.string.hint_limit_user);
        edtName.addTextChangedListener(userType.equals("1") ? new TextWatcherUtil(edtName, 8).getTextWatcher() : new TextWatcherUtil(edtName, 12).getTextWatcher());
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 保存
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toSave() {
        //获取编辑框的值
        String name = edtName.getText().toString();
        if (ParamUtils.isEmpty(name)) {
            //如果修改后的值为空
            lemonMessage.sendMessage("清输入昵称！");
            return;
        }
        //如果修改后的值和之前不一样的话才传给上一个Activity
        Intent intent = getIntent();
        if (!name.equals(intent.getStringExtra("name"))) {
            intent.putExtra("name", name);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

}
