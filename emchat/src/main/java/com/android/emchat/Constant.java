package com.android.emchat;

/**
 * Created by Administrator on 2015/8/30.
 */
public class Constant {
    public static final String APP_ID = "a0293c257df4";
    public static final String APP_KEY = "289764bc5f58d2b0c42aa136cd3617e2";
    public static final String QINIU_DOMAIN = "http://7xid0d.com1.z0.glb.clouddn.com/";
    public static final String QINIU_UPLOAD_DOMAIN = "http://upload.qiniu.com";

    //-------huanxin-----------
    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String CHAT_ROOM = "item_chatroom";
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
    public static final String ACCOUNT_REMOVED = "account_removed";
    public static final String CHAT_ROBOT = "item_robots";
    public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
    public static final String IS_CONFLICT = "isConflict";

    //-------------------Constant------------------------
    public static final String SUCCESS_TOAST = "0";
    public static final String POINT_TOAST = "1";

    //-------------------Request------------------------
    public static final int REQUEST_MODIFY_REMARKS = 3;
    public static final int MAP_REQUEST = 4;
    public static final int PERMISSION_CODE_FILE = 1;
    public static final int PERMISSION_CODE_LOC = 2;
    public static final int PERMISSION_CODE_RADIO = 3;
    public static final int REQUEST_REAL_NAME = 8;
    public static final int REQUEST_QUALIFICATION = 9;

    //--------------------SharedPreference----------------
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String CUSTOMIZE_LAT = "customize_lat";
    public static final String CUSTOMIZE_LNG = "customize_lng";
    public static final String IS_CUSTOMIZE = "is_customize";
    public static final String LAST_USERNAME = "last_username";
    public static final String CALL_HISTORY = "callHistory";
    public static final String FIRST_SEND_TIME = "first_send_time";

    //-----------------default value--------------------
    public static final double DEFAULT_LAT = 24.478314;
    public static final double DEFAULT_LNG = 118.111461;

    //每页显示数目
    public static final int PAGE_SIZE = 10;

    //-------------------json------------------------
    public static final String CALL_SELL_BROKERAGE="[\n" +
            "      {\n" +
            "        \"name\": \"面谈\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"mt\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"0.5%\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"0.5\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"1%\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"1\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"1.5%\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"1.5\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"2%\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"2\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"2.5%\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"2.5\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"3%\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"3\"\n" +
            "      }\n" +
            "]";

    public static final String CALL_RENT_BROKERAGE= "[\n" +
            "      {\n" +
            "        \"name\": \"面谈\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"mt\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"5天\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"5\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"10天\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"10\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"15天\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"15\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"20天\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"20\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"25天\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"25\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"30天\",\n" +
            "        \"isSelected\": false,\n" +
            "        \"value\": \"30\"\n" +
            "      }\n" +
            "]";
}
