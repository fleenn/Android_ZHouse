package com.zfb.house.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.LemonCacheManager;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.UpdatePointEvent;
import com.lemon.event.UpdateUserInfoEvent;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.component.ArraySelectWheelDialog;
import com.zfb.house.model.bean.ReleasePull;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.SignInParam;
import com.zfb.house.model.param.UserStatusParam;
import com.zfb.house.model.result.SignInResult;
import com.zfb.house.model.result.UserStatusResult;
import com.zfb.house.util.ToolUtil;

import java.util.regex.Pattern;

/**
 * 用户“我的”主界面
 * Created by Administrator on 2016/4/24.
 */
@Layout(id = R.layout.fragment_mine_user)
public class MineUserFragment extends LemonFragment {

    private static final String TAG = "MineUserFragment";
    public static final int REQUEST_EXIST = 1;

    //头像
    @FieldView(id = R.id.img_user_avatar)
    private ImageView imgAvatar;
    //用户名
    @FieldView(id = R.id.txt_user_name)
    private TextView txtName;
    //近期状态
    @FieldView(id = R.id.txt_user_state)
    private TextView txtState;
    //金币数
    @FieldView(id = R.id.txt_user_coin)
    private TextView txtCoinNumber;
    private ReleasePull mReleasePull;
    private String token;

    @Override
    protected void initView() {
        setCenterText(R.string.title_mine);
        mImgTitleLt.setVisibility(View.GONE);

        token = SettingUtils.get(getActivity(), "token", null);
        mReleasePull = new LemonCacheManager().getBean(ReleasePull.class);
        initPersonalInfo();
    }

    /**
     * 从本地获取信息初始化用户的个人信息
     */
    public void initPersonalInfo() {
        UserBean userBean = UserBean.getInstance(getActivity());
        //头像
        Glide.with(getActivity()).load(userBean.photo).placeholder(R.drawable.default_avatar).into(imgAvatar);
        //昵称
        txtName.setText(userBean.name);
        //金币数
        txtCoinNumber.setText(userBean.point + "金币");
        //近期状态
        String recentStatus = userBean.recentStatus;
        if (!ParamUtils.isNull(mReleasePull)) {
            txtState.setText(Pattern.compile("[0-9]*").matcher(recentStatus).matches() ? ToolUtil.convertValueToLabel(recentStatus, mReleasePull.getRECENT_STATUS()) : recentStatus);
        }
    }

    /**
     * 点击头像放大
     */
    @OnClick(id = R.id.img_user_avatar)
    public void enlargeAvatar() {
        Intent intent = new Intent(getActivity(), BrokerShopPhotoActivity.class);
        intent.putExtra("photo", ParamUtils.isEmpty(UserBean.getInstance(getActivity()).photo) ? "isDefault" : UserBean.getInstance(getActivity()).photo);
        startActivity(intent);
    }

    /**
     * 选择近期状态
     */
    @OnClick(id = R.id.rlayout_user_state)
    public void selectState() {
        ArraySelectWheelDialog statusDialog = null;
        if (!ParamUtils.isNull(statusDialog)) {
            statusDialog.show();
        } else {
            //近期状态
            statusDialog = new ArraySelectWheelDialog(getActivity(), mReleasePull.getRECENT_STATUS());
            statusDialog.show();
            statusDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                @Override
                public void getSelectValue(String time, String value) {
                    txtState.setText(time);
                    //调用“用户更新最近状态”接口
                    UserStatusParam userStatusParam = new UserStatusParam();
                    userStatusParam.setToken(token);
                    userStatusParam.setStatus(value);
                    apiManager.updateUserStatus(userStatusParam);
                }
            });
        }
    }

//    /**
//     * 签到
//     */
//    @OnClick(id = R.id.rlayout_user_sign)
//    public void toSignIn() {
//        startActivity(new Intent(getActivity(), GoldCoinActivity.class));
//    }

    /**
     * 签到
     */
    @OnClick(id = R.id.txt_user_sign)
    public void toSignIn() {
        //调用“签到”接口
        SignInParam signInParam = new SignInParam();
        signInParam.setToken(token);
        apiManager.signIn(signInParam);
    }

    /**
     * 个人信息
     */
    @OnClick(id = R.id.rlayout_user_personal)
    public void toUserPersonal() {
        startActivity(new Intent(getActivity(), UserDataActivity.class));
    }

    /**
     * 关注的专家
     */
    @OnClick(id = R.id.rlayout_user_expert)
    public void toUserExpert() {
        startActivity(new Intent(getActivity(), ConcernExpertActivity.class));
    }

    /**
     * 我的委托
     */
    @OnClick(id = R.id.rlayout_user_entrust)
    public void toEntrust() {
        startActivity(new Intent(getActivity(), UserEntrustActivity.class));
    }

    /**
     * 我的好友
     */
    @OnClick(id = R.id.rlayout_user_friends)
    public void toUserFriends() {
        startActivity(new Intent(getActivity(), MyFriendsActivity.class));
    }

    /**
     * 我的房友圈
     */
    @OnClick(id = R.id.rlayout_user_my_moments)
    public void toMyMoments() {
        startActivity(new Intent(getActivity(), MyMomentsActivity.class));
    }

    /**
     * 关注的区域
     */
    @OnClick(id = R.id.rlayout_user_areas)
    public void toUserAreas() {
        startActivity(new Intent(getActivity(), AreasActivity.class));
    }

    /**
     * 收藏的房源
     */
    @OnClick(id = R.id.rlayout_user_collect_houses)
    public void toCollectHouses() {
        startActivity(new Intent(getActivity(), CollectHousesActivity.class));
    }

    /**
     * 房友圈收藏
     */
    @OnClick(id = R.id.rlayout_user_collect_moments)
    public void toCollectMoments() {
        startActivity(new Intent(getActivity(), CollectMomentsActivity.class));
    }

    /**
     * 设置
     */
    @OnClick(id = R.id.rlayout_user_setting)
    public void toSetting() {
        startActivityForResult(new Intent(getActivity(), SettingActivity.class), REQUEST_EXIST);
    }

    /**
     * 近期状态
     *
     * @param result
     */
    public void onEventMainThread(UserStatusResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            //更新本地用户的近期状态
            UserBean.getInstance(getActivity()).recentStatus = txtState.getText().toString();
            UserBean.updateUserBean(getActivity());
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 完成任务后更新积分显示
     *
     * @param event
     */
    public void onEventMainThread(UpdatePointEvent event) {
        txtCoinNumber.setText(String.valueOf(event.getPoint()) + "金币");
    }

    /**
     * 更新个人信息——用户打开自己的个人资料
     *
     * @param event
     */
    public void onEventMainThread(UpdateUserInfoEvent event) {
        //头像
        if (!ParamUtils.isEmpty(event.getPhoto())) {
            Glide.with(getActivity()).load(event.getPhoto()).placeholder(R.drawable.default_avatar).into(imgAvatar);
        }
        //昵称
        if (!ParamUtils.isEmpty(event.getName())) {
            txtName.setText(event.getName());
        }
    }

    /**
     * 签到
     *
     * @param result
     */
    public void onEventMainThread(SignInResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {//签到成功
            lemonMessage.sendMessage("签到成功", "0");
            ToolUtil.updatePoint(getActivity(), result.getData().getTotalPoint(), result.getData().getGetPoint());
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case REQUEST_EXIST://退出
                    ((MainActivity) getActivity()).init();
                    break;
            }
        }
    }

}
