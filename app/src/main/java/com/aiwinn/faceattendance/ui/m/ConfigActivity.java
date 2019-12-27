package com.aiwinn.faceattendance.ui.m;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.aiwinn.base.activity.BaseActivity;
import com.aiwinn.base.util.ToastUtils;
import com.aiwinn.faceattendance.AttApp;
import com.aiwinn.faceattendance.R;
import com.aiwinn.faceattendance.common.AttConstants;
import com.aiwinn.facedetectsdk.FaceDetectManager;
import com.aiwinn.facedetectsdk.common.ConfigLib;
import com.aiwinn.facedetectsdk.common.Constants;

import java.util.ArrayList;

/**
 * com.aiwinn.faceattendance.ui
 * SDK_ATT
 * 2018/08/24
 * Created by LeoLiu on User
 */

public class ConfigActivity extends BaseActivity implements View.OnClickListener , CompoundButton.OnCheckedChangeListener,View.OnLongClickListener{

    TextView mTitle;
    ImageView mBack;

    Switch mLR_switch;
    Switch mTB_switch;
    Switch mLiveness_switch;
    Switch mRecognition_switch;
    Switch mDoubleCamera_switch;
    Switch mUvcMode_switch;
    Switch mPL_switch;

    EditText mCamera_id;
    EditText mCamera_rotate;
    EditText mPreview_rotate;
    TextView mSetting;

    EditText mUnlock;
    TextView mSetting_unlock;

    EditText mLivenes;
    TextView mSetting_livenes;
    EditText mLivenes1;
    EditText mLivenes2;
    EditText mLivenes3;
    EditText mLivenes4;
    EditText mLiveCount;
    EditText mFakeCount;

    EditText mFace_overlay;
    EditText mCamera_id_infrared;
    EditText mCamera_rotate_infrared;
    EditText mPreview_rotate_infrared;
    TextView mSetting_infrared;

    EditText mLivenes_infrared;
    TextView mSetting_livenes_infrared;
    EditText mLivenes_infrared1;
    EditText mLivenes_infrared2;
    EditText mLivenes_infrared3;
    EditText mLivenes_infrared4;

    Spinner mPreviewSpinner;
    Spinner mRecognitionSpinner;

    RadioGroup mRgRegister;
    RadioGroup mRgDetect;

    ArrayAdapter<String> mPreviewAdapter;
    ArrayList<String> previewList = new ArrayList<>();
    ArrayAdapter<String> mRecognitionModeAdapter;
    ArrayList<String> recognitionModeList = new ArrayList<>();
    boolean isFirstSelection = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_config;
    }

    @Override
    public void initViews() {
        mBack = (ImageView) findViewById(R.id.back);
        mTitle = (TextView)findViewById(R.id.title);

        mRgRegister = (RadioGroup) findViewById(R.id.rgregister);
        mRgDetect = (RadioGroup)findViewById(R.id.rgdetect);

        mPreviewSpinner = (Spinner) findViewById(R.id.previewsp);
        mRecognitionSpinner = (Spinner) findViewById(R.id.recognitionsp);

        mLR_switch = (Switch) findViewById(R.id.lr_switch);
        mTB_switch = (Switch) findViewById(R.id.tb_switch);
        mLiveness_switch = (Switch) findViewById(R.id.liveness_switch);
        mRecognition_switch = (Switch) findViewById(R.id.recognition_switch);
        mDoubleCamera_switch = (Switch) findViewById(R.id.double_camera_switch);
        mPL_switch = (Switch) findViewById(R.id.pl_switch);
        mUvcMode_switch = (Switch) findViewById(R.id.uvc_switch);

        mCamera_id = (EditText) findViewById(R.id.camera_id);
        mCamera_rotate = (EditText) findViewById(R.id.camera_rotate);
        mPreview_rotate = (EditText) findViewById(R.id.preview_rotate);
        mSetting = (TextView) findViewById(R.id.setting);

        mUnlock = (EditText) findViewById(R.id.unlock);
        mSetting_unlock = (TextView) findViewById(R.id.setting_unlock);

        mLivenes = (EditText) findViewById(R.id.liveness);
        mLivenes1 = (EditText) findViewById(R.id.liveness1);
        mLivenes2 = (EditText) findViewById(R.id.liveness2);
        mLivenes3 = (EditText) findViewById(R.id.liveness3);
        mLivenes4 = (EditText) findViewById(R.id.liveness4);
        mLiveCount = (EditText) findViewById(R.id.livecount);
        mFakeCount = (EditText) findViewById(R.id.fakecount);
        mSetting_livenes = (TextView) findViewById(R.id.setting_livenes);

        mFace_overlay = (EditText) findViewById(R.id.face_overlay);
        mCamera_id_infrared = (EditText) findViewById(R.id.camera_id_infrared);
        mCamera_rotate_infrared = (EditText) findViewById(R.id.camera_rotate_infrared);
        mPreview_rotate_infrared = (EditText) findViewById(R.id.preview_rotate_infrared);
        mSetting_infrared = (TextView) findViewById(R.id.setting_infrared);

        mLivenes_infrared = (EditText) findViewById(R.id.liveness_infrared);
        mSetting_livenes_infrared = (TextView) findViewById(R.id.setting_livenes_infrared);
        mLivenes_infrared1 = (EditText) findViewById(R.id.liveness_infrared1);
        mLivenes_infrared2 = (EditText) findViewById(R.id.liveness_infrared2);
        mLivenes_infrared3 = (EditText) findViewById(R.id.liveness_infrared3);
        mLivenes_infrared4 = (EditText) findViewById(R.id.liveness_infrared4);
    }

    @Override
    public void initData() {
        mTitle.setText(getResources().getString(R.string.config));

        mLR_switch.setChecked(AttConstants.LEFT_RIGHT);
        mTB_switch.setChecked(AttConstants.TOP_BOTTOM);
        mLiveness_switch.setChecked(ConfigLib.detectWithLiveness);
        mRecognition_switch.setChecked(ConfigLib.detectWithRecognition);
        mDoubleCamera_switch.setChecked(ConfigLib.doubleCameraWithInfraredLiveness);
        mPL_switch.setChecked(AttConstants.PORTRAIT_LANDSCAPE);
        mUvcMode_switch.setChecked(AttConstants.UVC_MODE);

        mCamera_id.setText(String.valueOf(AttConstants.CAMERA_ID));
        mCamera_rotate.setText(String.valueOf(AttConstants.CAMERA_DEGREE));
        mPreview_rotate.setText(String.valueOf(AttConstants.PREVIEW_DEGREE));
        mUnlock.setText(String.valueOf(ConfigLib.featureThreshold));
        mLivenes.setText(String.valueOf(ConfigLib.livenessThreshold));
        mLivenes1.setText(String.valueOf(ConfigLib.livenessAwRGBThreshold1));
        mLivenes2.setText(String.valueOf(ConfigLib.livenessAwRGBThreshold2));
        mLivenes3.setText(String.valueOf(ConfigLib.livenessAwRGBThreshold3));
        mLivenes4.setText(String.valueOf(ConfigLib.livenessAwRGBThreshold4));
        mLiveCount.setText(String.valueOf(ConfigLib.livenessLiveNum));
        mFakeCount.setText(String.valueOf(ConfigLib.livenessFakeNum));

        mFace_overlay.setText(String.valueOf(ConfigLib.overlayAreaThreshold));
        mCamera_id_infrared.setText(String.valueOf(AttConstants.CAMERA_ID_INFRARED));
        mCamera_rotate_infrared.setText(String.valueOf(AttConstants.CAMERA_DEGREE_INFRARED));
        mPreview_rotate_infrared.setText(String.valueOf(AttConstants.PREVIEW_DEGREE_INFRARED));

        mLivenes_infrared.setText(String.valueOf(ConfigLib.livenessInFraredThreshold));
        mLivenes_infrared1.setText(String.valueOf(ConfigLib.livenessInfraredThreshold1));
        mLivenes_infrared2.setText(String.valueOf(ConfigLib.livenessInfraredThreshold2));
        mLivenes_infrared3.setText(String.valueOf(ConfigLib.livenessInfraredThreshold3));
        mLivenes_infrared4.setText(String.valueOf(ConfigLib.livenessInfraredThreshold4));

        if (AttConstants.REGISTER_DEFAULT) {
            mRgRegister.check(R.id.register_default);
        }else {
            mRgRegister.check(R.id.register_ex);
        }
        if (AttConstants.DETECT_DEFAULT) {
            mRgDetect.check(R.id.detect_default);
        }else {
            mRgDetect.check(R.id.detect_ex);
        }

        recognitionModeList.clear();
        recognitionModeList.add(getResources().getString(R.string.recognition_mode_track1));
        recognitionModeList.add(getResources().getString(R.string.recognition_mode_recognition));
        mRecognitionModeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, recognitionModeList);
        mRecognitionModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRecognitionSpinner.setAdapter(mRecognitionModeAdapter);
        if (Constants.RECOGNITION_MODE == Constants.RECOGNITION_MODE_TRACKER1){
            mRecognitionSpinner.setSelection(0);
        } else if (Constants.RECOGNITION_MODE == Constants.RECOGNITION_MODE_RECOGNITION){
            mRecognitionSpinner.setSelection(1);
        }
        previewList.clear();
        previewList.add("("+getResources().getString(R.string.auto)+")");
        previewList.add("(640*480)");
        previewList.add("(1280*720)");
        previewList.add("(1280*960)");
        previewList.add("(1920*1080)");
        previewList.add("(480*640)");
        previewList.add("(720*1280)");
        previewList.add("(960*1280)");
        previewList.add("(1080*1920)");
        mPreviewAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, previewList);
        mPreviewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPreviewSpinner.setAdapter(mPreviewAdapter);
        switch (AttConstants.CAMERA_PREVIEW_HEIGHT){
            case 0:
                mPreviewSpinner.setSelection(0);
                break;
            case 480:
                mPreviewSpinner.setSelection(1);
                break;
            case 720:
                mPreviewSpinner.setSelection(2);
                break;
            case 960:
                mPreviewSpinner.setSelection(3);
                break;
            case 1080:
                mPreviewSpinner.setSelection(4);
                break;
            case 640:
                mPreviewSpinner.setSelection(5);
                break;
            case 1280:
                mPreviewSpinner.setSelection(6);
                break;
            case 1280*2:
                mPreviewSpinner.setSelection(7);
                break;
            case 1920:
                mPreviewSpinner.setSelection(8);
                break;
        }
    }

    @Override
    public void initListeners() {

        mBack.setOnLongClickListener(this);
        mTitle.setOnLongClickListener(this);
        mBack.setOnClickListener(this);
        mTitle.setOnClickListener(this);

        mSetting.setOnClickListener(this);
        mSetting_unlock.setOnClickListener(this);
        mSetting_livenes.setOnClickListener(this);
        mSetting_infrared.setOnClickListener(this);
        mSetting_livenes_infrared.setOnClickListener(this);

        mLiveness_switch.setOnCheckedChangeListener(this);
        mRecognition_switch.setOnCheckedChangeListener(this);
        mLR_switch.setOnCheckedChangeListener(this);
        mTB_switch.setOnCheckedChangeListener(this);
        mDoubleCamera_switch.setOnCheckedChangeListener(this);
        mPL_switch.setOnCheckedChangeListener(this);
        mUvcMode_switch.setOnCheckedChangeListener(this);

        mPreviewSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                switch (arg2) {
                    case 0:
                        AttConstants.CAMERA_PREVIEW_WIDTH = 0;
                        AttConstants.CAMERA_PREVIEW_HEIGHT = 0;
                        break;
                    case 1:
                        AttConstants.CAMERA_PREVIEW_WIDTH = 640;
                        AttConstants.CAMERA_PREVIEW_HEIGHT = 480;
                        break;
                    case 2:
                        AttConstants.CAMERA_PREVIEW_WIDTH = 1280;
                        AttConstants.CAMERA_PREVIEW_HEIGHT = 720;
                        break;
                    case 3:
                        AttConstants.CAMERA_PREVIEW_WIDTH = 1280;
                        AttConstants.CAMERA_PREVIEW_HEIGHT = 960;
                        break;
                    case 4:
                        AttConstants.CAMERA_PREVIEW_WIDTH = 1920;
                        AttConstants.CAMERA_PREVIEW_HEIGHT = 1080;
                        break;
                    case 5:
                        AttConstants.CAMERA_PREVIEW_WIDTH = 480;
                        AttConstants.CAMERA_PREVIEW_HEIGHT = 640;
                        break;
                    case 6:
                        AttConstants.CAMERA_PREVIEW_WIDTH = 720;
                        AttConstants.CAMERA_PREVIEW_HEIGHT = 1280;
                        break;
                    case 7:
                        AttConstants.CAMERA_PREVIEW_WIDTH = 960;
                        AttConstants.CAMERA_PREVIEW_HEIGHT = 1280*2;
                        break;
                    case 8:
                        AttConstants.CAMERA_PREVIEW_WIDTH = 1080;
                        AttConstants.CAMERA_PREVIEW_HEIGHT = 1920;
                        break;
                }
                AttApp.sp.edit().putInt(AttConstants.PREFS_CAMERA_PREVIEW_SIZE,AttConstants.CAMERA_PREVIEW_HEIGHT).commit();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        mRecognitionSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                switch (arg2) {
                    case 0:
                        FaceDetectManager.setRecognizeMode(Constants.RECOGNITION_MODE_TRACKER1);
                        if (!isFirstSelection) {
                            //应用内切换了识别模式需要重新初始化开启指定模式线程
                            ToastUtils.showShort(getString(R.string.reopen));
                        }
                        isFirstSelection = false;
                        break;

                    case 1:
                        FaceDetectManager.setRecognizeMode(Constants.RECOGNITION_MODE_RECOGNITION);
                        if (!isFirstSelection) {
                            //应用内切换了识别模式需要重新初始化开启指定模式线程
                            ToastUtils.showShort(getString(R.string.reopen));
                        }
                        isFirstSelection = false;
                        break;
                }
                AttApp.sp.edit().putInt(AttConstants.PREFS_TRACKER_MODE,Constants.RECOGNITION_MODE).commit();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        mRgRegister.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.register_default:
                        AttConstants.REGISTER_DEFAULT = true;
                        break;

                    case R.id.register_ex:
                        AttConstants.REGISTER_DEFAULT = false;
                        break;

                }
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_REGISTER_DEFAULT,AttConstants.REGISTER_DEFAULT).commit();
            }
        });

        mRgDetect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.detect_default:
                        AttConstants.DETECT_DEFAULT = true;
                        break;

                    case R.id.detect_ex:
                        AttConstants.DETECT_DEFAULT = false;
                        break;

                }
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_DETECT_DEFAULT,AttConstants.DETECT_DEFAULT).commit();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){

            case R.id.recognition_switch:
                ConfigLib.detectWithRecognition = isChecked;
                mRecognition_switch.setChecked(ConfigLib.detectWithRecognition);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_RECOGNITION,ConfigLib.detectWithRecognition).commit();
                break;

            case R.id.liveness_switch:
                ConfigLib.detectWithLiveness = isChecked;
                mLiveness_switch.setChecked(ConfigLib.detectWithLiveness);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_LIVENESS,ConfigLib.detectWithLiveness).commit();
                break;

            case R.id.lr_switch:
                AttConstants.LEFT_RIGHT = isChecked;
                mLR_switch.setChecked(AttConstants.LEFT_RIGHT);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_LR,AttConstants.LEFT_RIGHT).commit();
                break;

            case R.id.tb_switch:
                AttConstants.TOP_BOTTOM = isChecked;
                mTB_switch.setChecked(AttConstants.TOP_BOTTOM);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_TB,AttConstants.TOP_BOTTOM).commit();
                break;
            case R.id.double_camera_switch:
                ConfigLib.doubleCameraWithInfraredLiveness = isChecked;
                if (ConfigLib.doubleCameraWithInfraredLiveness) {
                    //应用内单目切换了双目需要重新初始化开启双目功能和线程
                    ToastUtils.showShort(getString(R.string.reopen));
                }
                mDoubleCamera_switch.setChecked(ConfigLib.doubleCameraWithInfraredLiveness);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_OPEN_DOUBLE_CAMERA,ConfigLib.doubleCameraWithInfraredLiveness).commit();
                break;
            case R.id.pl_switch:
                AttConstants.PORTRAIT_LANDSCAPE = isChecked;
                mPL_switch.setChecked(AttConstants.PORTRAIT_LANDSCAPE);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_PORTRAIT_LANDSCAPE,AttConstants.PORTRAIT_LANDSCAPE).commit();
                break;
            case R.id.uvc_switch:
                AttConstants.UVC_MODE = isChecked;
                mUvcMode_switch.setChecked(AttConstants.UVC_MODE);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_UVC,AttConstants.UVC_MODE).commit();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume","ConfigActivity -> onResume > Detect_Exception "+AttConstants.Detect_Exception +" INIT_STATE "+AttConstants.INIT_STATE);
        if(AttConstants.Detect_Exception && AttConstants.INIT_STATE) {
            mIntent.setClass(ConfigActivity.this, DetectActivity.class);
            startActivity(mIntent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back:
            case R.id.title:
                ConfigActivity.this.finish();
                break;

            case R.id.setting:
                String id = mCamera_id.getText().toString().trim();
                int intId = Integer.parseInt(id);
                String rot = mCamera_rotate.getText().toString().trim();
                int intRot = Integer.parseInt(rot);
                String prot = mPreview_rotate.getText().toString().trim();
                int intPRot = Integer.parseInt(prot);
                AttConstants.CAMERA_ID = intId;
                AttConstants.CAMERA_DEGREE = intRot;
                AttConstants.PREVIEW_DEGREE = intPRot;
                setSucc(AttConstants.CAMERA_ID +" | "+AttConstants.CAMERA_DEGREE +" | "+AttConstants.PREVIEW_DEGREE);
                AttApp.sp.edit().putInt(AttConstants.PREFS_CAMERA_ID,AttConstants.CAMERA_ID).commit();
                AttApp.sp.edit().putInt(AttConstants.PREFS_CAMERA_DEGREE,AttConstants.CAMERA_DEGREE).commit();
                AttApp.sp.edit().putInt(AttConstants.PREFS_PREVIEW_DEGREE,AttConstants.PREVIEW_DEGREE).commit();
                break;

            case R.id.setting_unlock:
                String unlock = mUnlock.getText().toString().trim();
                ConfigLib.featureThreshold = Float.valueOf(unlock);
                setSucc(ConfigLib.featureThreshold);
                AttApp.sp.edit().putFloat(AttConstants.PREFS_UNLOCK,ConfigLib.featureThreshold).commit();
                break;

            case R.id.setting_livenes:
                String liveness = mLivenes.getText().toString().trim();
                ConfigLib.livenessThreshold = Float.valueOf(liveness);
                String liveness1 = mLivenes1.getText().toString().trim();
                ConfigLib.livenessAwRGBThreshold1 = Float.valueOf(liveness1);
                String liveness2 = mLivenes2.getText().toString().trim();
                ConfigLib.livenessAwRGBThreshold2 = Float.valueOf(liveness2);
                String liveness3 = mLivenes3.getText().toString().trim();
                ConfigLib.livenessAwRGBThreshold3 = Float.valueOf(liveness3);
                String liveness4 = mLivenes4.getText().toString().trim();
                ConfigLib.livenessAwRGBThreshold4 = Float.valueOf(liveness4);
                String lives = mLiveCount.getText().toString().trim();
                ConfigLib.livenessLiveNum = Integer.parseInt(lives);
                String fakes = mFakeCount.getText().toString().trim();
                ConfigLib.livenessFakeNum = Integer.parseInt(fakes);
                setSucc(ConfigLib.livenessThreshold+" | "+ConfigLib.livenessAwRGBThreshold1+" | "+ConfigLib.livenessAwRGBThreshold2 +" | "+ConfigLib.livenessAwRGBThreshold3 +" | "+ConfigLib.livenessAwRGBThreshold4 +" | "+ConfigLib.livenessLiveNum+" | "+ConfigLib.livenessFakeNum);
                AttApp.sp.edit().putFloat(AttConstants.PREFS_LIVENESST,ConfigLib.livenessThreshold).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_LIVENESST2,ConfigLib.livenessThreshold2).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_LIVENESST3,ConfigLib.livenessThreshold3).commit();
                AttApp.sp.edit().putInt(AttConstants.PREFS_LIVECOUNT,ConfigLib.livenessLiveNum).commit();
                AttApp.sp.edit().putInt(AttConstants.PREFS_FAKECOUNT,ConfigLib.livenessFakeNum).commit();
                break;
            case R.id.setting_infrared:
                String face_overlay = mFace_overlay.getText().toString().trim();
                float intId_overlay = Float.valueOf(face_overlay);
                String id_infrared = mCamera_id_infrared.getText().toString().trim();
                int intId_infrared = Integer.parseInt(id_infrared);
                String rot_infrared = mCamera_rotate_infrared.getText().toString().trim();
                int intRot_infrared = Integer.parseInt(rot_infrared);
                String prot_infrared = mPreview_rotate_infrared.getText().toString().trim();
                int intPRot_infrared = Integer.parseInt(prot_infrared);
                ConfigLib.overlayAreaThreshold = intId_overlay;
                AttConstants.CAMERA_ID_INFRARED = intId_infrared;
                AttConstants.CAMERA_DEGREE_INFRARED = intRot_infrared;
                AttConstants.PREVIEW_DEGREE_INFRARED = intPRot_infrared;
                setSucc(ConfigLib.overlayAreaThreshold + " | " +AttConstants.CAMERA_ID_INFRARED +" | "+AttConstants.CAMERA_DEGREE_INFRARED +" | "+AttConstants.PREVIEW_DEGREE_INFRARED);
                AttApp.sp.edit().putFloat(AttConstants.PREFS_FACE_OVERLAY,ConfigLib.overlayAreaThreshold).commit();
                AttApp.sp.edit().putInt(AttConstants.PREFS_CAMERA_ID_INFRARED,AttConstants.CAMERA_ID_INFRARED).commit();
                AttApp.sp.edit().putInt(AttConstants.PREFS_CAMERA_DEGREE_INFRARED,AttConstants.CAMERA_DEGREE_INFRARED).commit();
                AttApp.sp.edit().putInt(AttConstants.PREFS_PREVIEW_DEGREE_INFRARED,AttConstants.PREVIEW_DEGREE_INFRARED).commit();
                break;

            case R.id.setting_livenes_infrared:
                String liveness_infrared = mLivenes_infrared.getText().toString().trim();
                ConfigLib.livenessInFraredThreshold = Float.valueOf(liveness_infrared);
                String liveness_infrared1 = mLivenes_infrared1.getText().toString().trim();
                ConfigLib.livenessInfraredThreshold1 = Float.valueOf(liveness_infrared1);
                String liveness_infrared2 = mLivenes_infrared2.getText().toString().trim();
                ConfigLib.livenessInfraredThreshold2 = Float.valueOf(liveness_infrared2);
                String liveness_infrared3 = mLivenes_infrared3.getText().toString().trim();
                ConfigLib.livenessInfraredThreshold3 = Float.valueOf(liveness_infrared3);
                String liveness_infrared4 = mLivenes_infrared4.getText().toString().trim();
                ConfigLib.livenessInfraredThreshold4 = Float.valueOf(liveness_infrared4);
                setSucc(ConfigLib.livenessInFraredThreshold+" | "+ConfigLib.livenessInfraredThreshold1+" | "+ConfigLib.livenessInfraredThreshold2 +" | "+ConfigLib.livenessInfraredThreshold3+" | "+ConfigLib.livenessInfraredThreshold4);
                AttApp.sp.edit().putFloat(AttConstants.PREFS_LIVENESS_INFRAREDT,ConfigLib.livenessInFraredThreshold).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_LIVENESS_INFRAREDT1,ConfigLib.livenessInfraredThreshold1).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_LIVENESS_INFRAREDT2,ConfigLib.livenessInfraredThreshold2).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_LIVENESS_INFRAREDT3,ConfigLib.livenessInfraredThreshold3).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_LIVENESS_INFRAREDT4,ConfigLib.livenessInfraredThreshold4).commit();
                break;

            default:break;
        }
    }

    void setSucc(Object o){
        ToastUtils.showLong(getResources().getString(R.string.set_success)+" : "+o);
        ConfigActivity.this.finish();
    }

    void setFail(){
        ToastUtils.showLong(getResources().getString(R.string.set_fail));
    }

    @Override
    public boolean onLongClick(View v) {

        if(v.getId() == R.id.back || v.getId() == R.id.title){
            mIntent = new Intent(ConfigActivity.this,DebugActivity.class);
            startActivity(mIntent);
            return true;
        }

        return false;
    }
}
