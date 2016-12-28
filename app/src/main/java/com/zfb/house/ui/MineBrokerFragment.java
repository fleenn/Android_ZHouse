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
import com.zfb.house.component.RatingBar;
import com.zfb.house.model.bean.ReleasePull;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.ExpertTypeParam;
import com.zfb.house.model.param.SignInParam;
import com.zfb.house.model.result.ExpertTypeResult;
import com.zfb.house.model.result.SignInResult;
import com.zfb.house.util.ToolUtil;

import java.util.regex.Pattern;

/**
 * 经纪人“我的”主页面
 * Created by Administrator on 2016/4/24.
 */
@Layout(id = R.layout.fragment_mine_broker)
public class MineBrokerFragment extends LemonFragment {

    private static final String TAG = "MineBrokerFragment";
    public static final int REQUEST_EXIST = 1;

    //头像
    @FieldView(id = R.id.img_broker_avatar)
    private ImageView imgAvatar;
    //用户名
    @FieldView(id = R.id.txt_broker_name)
    private TextView txtName;
    //星级
    @FieldView(id = R.id.rating_broker_star)
    private RatingBar rbStar;
    //近期状态
    @FieldView(id = R.id.txt_broker_type)
    private TextView txtType;
    //金币数
    @FieldView(id = R.id.txt_broker_coin)
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
     * 从本地获取信息初始化经纪人的个人信息
     */
    public void initPersonalInfo() {
        UserBean userBean = UserBean.getInstance(getActivity());
        //头像
        Glide.with(getActivity()).load(userBean.photo).placeholder(R.drawable.default_avatar).into(imgAvatar);
        //昵称
        txtName.setText(userBean.name);
        //星级
        rbStar.setStar(userBean.star);
        //金币数
        txtCoinNumber.setText(userBean.point + "金币");
        //专家类型
        String expertType = userBean.expertType;
        if (!ParamUtils.isNull(mReleasePull)) {
            txtType.setText(Pattern.compile("[0-9]*").matcher(expertType).matches() ? ToolUtil.convertValueToLabel(expertType, mReleasePull.getEXPERT_TYPE()) : expertType);
        }
    }

    /**
     * 点击头像放大
     */
    @OnClick(id = R.id.img_broker_avatar)
    public void enlargeAvatar() {
        Intent intent = new Intent(getActivity(), BrokerShopPhotoActivity.class);
        intent.putExtra("photo", ParamUtils.isEmpty(UserBean.getInstance(getActivity()).photo) ? "isDefault" : UserBean.getInstance(getActivity()).photo);
        startActivity(intent);
    }

    /**
     * 选择专家类型
     */
    @OnClick(id = R.id.rlayout_broker_type)
    public void selectExpertType() {
        ArraySelectWheelDialog expertTypeDialog = null;
        if (!ParamUtils.isNull(expertTypeDialog)) {
            expertTypeDialog.show();
        } else {
            //专家类型
            expertTypeDialog = new ArraySelectWheelDialog(getActivity(), mReleasePull.getEXPERT_TYPE());
            expertTypeDialog.show();
            expertTypeDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                @Override
                public void getSelectValue(String time, String value) {
                    txtType.setText(time);
                    //调用“更新专家类型”接口
                    ExpertTypeParam experttypeParam = new ExpertTypeParam();
                    experttypeParam.setToken(token);
                    experttypeParam.setExpertType(value);
                    apiManager.updateExpertType(experttypeParam);
                }
            });
        }
    }

//    /**
//     * 签到
//     */
//    @OnClick(id = R.id.rlayout_broker_sign)
//    public void toSignIn() {
//        startActivity(new Intent(getActivity(), GoldCoinActivity.class));
//    }

    /**
     * 签到
     */
    @OnClick(id = R.id.txt_broker_sign)
    public void toSignIn() {
        //调用“签到”接口
        SignInParam signInParam = new SignInParam();
        signInParam.setToken(token);
        apiManager.signIn(signInParam);
    }

    /**
     * 个人信息
     */
    @OnClick(id = R.id.rlayout_broker_personal)
    public void toBrokerPersonal() {
        startActivity(new Intent(getActivity(), BrokerDataActivity.class));
    }

    /**
     * 接受的委托
     */
    @OnClick(id = R.id.rlayout_broker_entrust)
    public void toBrokerEntrust() {
        startActivity(new Intent(getActivity(), BrokerEntrustActivity.class));
    }

    /**
     * 成交上传
     */
    @OnClick(id = R.id.rlayout_broker_upload)
    public void toUpload() {
        startActivity(new Intent(getActivity(), UploadActivity.class));
    }

    /**
     * 我的好友
     */
    @OnClick(id = R.id.rlayout_broker_friends)
    public void toBrokerFriends() {
        startActivity(new Intent(getActivity(), MyFriendsActivity.class));
    }

    /**
     * 我的房友圈
     */
    @OnClick(id = R.id.rlayout_broker_my_moments)
    public void toMyMoments() {
        startActivity(new Intent(getActivity(), MyMomentsActivity.class));
    }

    /**
     * 服务的区域
     */
    @OnClick(id = R.id.rlayout_broker_areas)
    public void toBrokerAreas() {
        startActivity(new Intent(getActivity(), AreasActivity.class));
    }

    /**
     * 收藏的房源
     */
    @OnClick(id = R.id.rlayout_broker_collect_houses)
    public void toCollectHouses() {
        startActivity(new Intent(getActivity(), CollectHousesActivity.class));
    }

    /**
     * 房友圈收藏
     */
    @OnClick(id = R.id.rlayout_broker_collect_moments)
    public void toCollectMoments() {
        startActivity(new Intent(getActivity(), CollectMomentsActivity.class));
    }

    /**
     * 设置
     */
    @OnClick(id = R.id.rlayout_broker_setting)
    public void toSetting() {
        startActivityForResult(new Intent(getActivity(), SettingActivity.class), REQUEST_EXIST);
    }

    /**
     * 专家类型
     *
     * @param result
     */
    public void onEventMainThread(ExpertTypeResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            //更新本地经纪人专家类型
            UserBean.getInstance(getActivity()).expertType = txtType.getText().toString();
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
