package com.aiwinn.faceattendance.common;

import android.os.Environment;

import com.aiwinn.facedetectsdk.common.Status;

import java.io.File;

/**
 * com.aiwinn.faceattendance.common
 * SDK_ATT
 * 2018/08/24
 * Created by LeoLiu on User
 */

public class AttConstants {
    public static final String PREFS = "ATT_SP";
    public static final String EXDB = "EX";
    public static boolean REGISTER_DEFAULT = true;
    public static boolean DETECT_DEFAULT = true;

    public static boolean DEBUG = true;
    public static boolean DEBUG_SHOW_FACEINFO = false;
    public static boolean INIT_STATE = false;
    public static Status INIT_STATE_ERROR = null;

    public static boolean hasBackCamera = true;
    public static boolean hasFrontCamera = true;
    public static boolean UVC_MODE = false;

    public static int cameraCount = 2;

    public static boolean LEFT_RIGHT = false;
    public static boolean TOP_BOTTOM = false;
    //竖屏时摄像头预览为横向画面
    public static boolean PORTRAIT_LANDSCAPE = false;

    public static int CAMERA_ID_INFRARED = 1;
    public static int CAMERA_DEGREE_INFRARED = 0;
    public static int PREVIEW_DEGREE_INFRARED = 0;

    public static int CAMERA_ID = 0;
    public static int CAMERA_DEGREE = 0;
    public static int PREVIEW_DEGREE = 0;

    public static int CAMERA_PREVIEW_WIDTH = 0;
    public static int CAMERA_PREVIEW_HEIGHT = 0;

    public static int CAMERA_BACK_ID = 0;
    public static int CAMERA_BACK_DEGREE = 0;

    public static int CAMERA_FRONT_ID = 1;
    public static int CAMERA_FRONT_DEGREE = 90;

    public static int DETECT_LIST_SIZE = 10;

    public static boolean Detect_Exception = false;

    public static String SD_CARD = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static String PATH_AIWINN = SD_CARD+ File.separator+"aiwinn";

    public static String PATH_ATTENDANCE = PATH_AIWINN+ File.separator+"attendance";

    public static String PATH_BULK_REGISTRATION = PATH_ATTENDANCE+ File.separator+"bulkregistration";
    public static String PATH_CARD = PATH_ATTENDANCE+ File.separator+"testslotcard";

    public static final String PREFS_DETECT_RATE = "DETECT_RATE";
    public static final String PREFS_DETECT_SIZE = "DETECT_SIZE";
    public static final String PREFS_TRACKER_SIZE = "TRACKER_SIZE";
    public static final String PREFS_FEATURE_SIZE = "FEATURE_SIZE";
    public static final String PREFS_FACE_SIZE = "FACE_SIZE";
    public static final String PREFS_CAMERA_ID = "CAMERA_ID";
    public static final String PREFS_CAMERA_DEGREE = "CAMERA_DEGREE";
    public static final String PREFS_PREVIEW_DEGREE = "PREVIEW_DEGREE";
    public static final String PREFS_CAMERA_PREVIEW_SIZE = "CAMERA_PREVIEW_SIZE";
    public static final String PREFS_RECOGNITION = "RECOGNITION";
    public static final String PREFS_TRACKER_MODE = "TRACKERMODE";
    public static final String PREFS_DETECT_MODE = "DETECTMODE";
    public static final String PREFS_LIVE_MODE = "LIVEMODE";
    public static final String PREFS_DETECT_DEFAULT = "DETECT_DEFAULT";
    public static final String PREFS_REGISTER_DEFAULT = "REGISTER_DEFAULT";
    public static final String PREFS_LIVENESS = "LIVENESS";
    public static final String PREFS_UNLOCK = "UNLOCK";
    public static final String PREFS_LIVENESST = "LIVENESST";
    public static final String PREFS_LIVENESST2 = "LIVENESST2";
    public static final String PREFS_LIVENESST3 = "LIVENESST3";
    public static final String PREFS_LIVECOUNT = "LIVECOUNT";
    public static final String PREFS_FAKECOUNT = "FAKECOUNT";
    public static final String PREFS_FACEMINIMA = "FACEMINIMA";
    public static final String PREFS_SAVEBLURDATA = "SAVEBLURDATA";
    public static final String PREFS_SAVENOFACEDATA = "SAVENOFACEDATA";
    public static final String PREFS_SAVESSDATA = "SAVESSDATA";
    public static final String PREFS_DETECTINFRARED = "DETECTINFRARED";
    public static final String PREFS_SAVEINFRAREDLIVE = "SAVEINFRAREDLIVE";
    public static final String PREFS_SAVEINFRAREDFAKE = "SAVEINFRAREDFAKE";
    public static final String PREFS_INFRAREDVALUE = "INFRAREDVALUE";
    public static final String PREFS_INFRAREDMODE = "INFRAREDMODE";
    public static final String PREFS_DEBUG = "DEBUG";
    public static final String PREFS_INOFO = "FACEINFO";
    public static final String PREFS_LR = "LR";
    public static final String PREFS_TB = "TB";
    public static final String PREFS_TRACKER = "TRACKER";
    public static final String PREFS_ST = "ST";
    public static final String PREFS_SE = "SE";
    public static final String PREFS_SC = "SC";
    public static final String PREFS_LIVE = "LIVE";
    public static final String PREFS_FAKE = "FAKE";
    public static final String PREFS_MAXREGISTERBRIGHTNESS = "MAXREGISTERBRIGHTNESS";
    public static final String PREFS_MINREGISTERBRIGHTNESS = "MINREGISTERBRIGHTNESS";
    public static final String PREFS_BLURREGISTERTHRESHOLD = "BLURREGISTERTHRESHOLD";
    public static final String PREFS_MAXRECOGNIZEBRIGHTNESS = "MAXDETECTBRIGHTNESS";
    public static final String PREFS_MINRECOGNIZEBRIGHTNESS = "MINDETECTBRIGHTNESS";
    public static final String PREFS_BLURECOGNIZETHRESHOLD = "BLURDETECTTHRESHOLD";
    public static final String PREFS_BLURECOGNIZENEWTHRESHOLD = "BLURDETECTNEWTHRESHOLD";
    public static final String PREFS_USENEWLIVEVERSION = "USENEWLIVEVERSION";
    public static final String PREFS_USEAWRGBLIVEVERSION = "USEAWRGBLIVEVERSION";
    public static final String PREFS_USEFIXLIVEVERSION = "USEFIXLIVEVERSION";
    public static final String PREFS_ENHANCEMODE = "ENHANCEMODE";
    public static final String PREFS_COVERMODE = "COVERMODE";
    public static final String PREFS_IOU = "IOU";
    public static final String PREFS_MULIVENESSSS = "MULIVENESSSS";
    public static final String PREFS_MULASSISTSS = "MULASSISTSS";
    public static final String PREFS_MULFRAME = "MULFRAME";
    public static final String PREFS_MULTHRESHOLD = "MULTHRESHOLD";
    public static final String PREFS_OPEN_DOUBLE_CAMERA = "OPEN_DOUBLE_CAMERA";
    public static final String PREFS_FACE_OVERLAY = "FACE_OVERLAY";
    public static final String PREFS_CAMERA_ID_INFRARED = "CAMERA_ID_INFRARED";
    public static final String PREFS_CAMERA_DEGREE_INFRARED = "CAMERA_DEGREE_INFRARED";
    public static final String PREFS_PREVIEW_DEGREE_INFRARED = "PREVIEW_DEGREE_INFRARED";
    public static final String PREFS_PORTRAIT_LANDSCAPE = "PORTRAIT_LANDSCAPE";
    public static final String PREFS_UVC = "UVC";
    public static final String PREFS_LIVENESS_INFRAREDT = "LIVENESS_INFRAREDT";
    public static final String PREFS_LIVENESS_INFRAREDT1 = "LIVENESS_INFRAREDT1";
    public static final String PREFS_LIVENESS_INFRAREDT2 = "LIVENESS_INFRAREDT2";
    public static final String PREFS_LIVENESS_INFRAREDT3 = "LIVENESS_INFRAREDT3";
    public static final String PREFS_LIVENESS_INFRAREDT4 = "LIVENESS_INFRAREDT4";
    public static final String PREFS_REMOVE_UNNORMAL_FACE = "REMOVE_UNNORMAL_FACE";
    public static final String PREFS_RETINA_FD = "RETINA_FD";
    public static final String PREFS_DETECT_FACE_NUM = "DETECT_FACE_NUM";
    public static final String PREFS_SIMILARITY_FILTER = "SIMILARITY_FILTER";
}
