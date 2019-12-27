package com.aiwinn.faceattendance.ui.m;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.aiwinn.base.AiwinnManager;
import com.aiwinn.base.activity.BaseActivity;
import com.aiwinn.base.util.ToastUtils;
import com.aiwinn.deblocks.common.FaceConstants;
import com.aiwinn.faceattendance.AttApp;
import com.aiwinn.faceattendance.R;
import com.aiwinn.faceattendance.common.AttConstants;
import com.aiwinn.facedetectsdk.FaceDetectManager;
import com.aiwinn.facedetectsdk.common.ConfigLib;
import com.aiwinn.facedetectsdk.common.Constants;

import java.util.ArrayList;

/**
 * com.aiwinn.faceattendance.ui.m
 * SDK_ATT
 * 2018/12/20
 * Created by LeoLiu on User
 */

public class DebugActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    TextView mTitle;
    ImageView mBack;

    Spinner mDetectSpinner;
    Spinner mLiveSpinner;

    Switch mCov_switch;
    Switch mEnhance_switch;
    Switch mDebug_switch;
    Switch mInfo_switch;
    RelativeLayout mRSb;
    View mRSbv;
    RelativeLayout mRSn;
    View mRSnv;
    RelativeLayout mRSs;
    View mRSsv;
    RelativeLayout mRSf;
    View mRSfv;
    RelativeLayout mRSl;
    View mRSlv;
    RelativeLayout mRSt;
    View mRStv;
    RelativeLayout mRStan;
    View mRStanv;
    RelativeLayout mRSerror;
    View mRSerrorv;

    Switch mSaveCov_switch;
    Switch mSaveBlur_switch;
    Switch mSaveNoFace_switch;
    Switch mSaveSS_switch;
    Switch mSaveF_switch;
    Switch mSaveL_switch;
    Switch mSaveTracker_switch;
    Switch mSaveTan_switch;
    Switch mSaveError_switch;

    Switch mUseNewLiveVersion_switch;
    Switch mUseFixLiveVersion_switch;
    Switch mUseAWRGBLiveVersion_switch;

    Switch mSimilarityFilter_switch;
    EditText mRegisterLightMin;
    EditText mRegisterLightMax;
    EditText mRegisterBlur;
    EditText mFaceMinima;
    TextView mRegister_Settings_light_blur;

    EditText mDetectLightMin;
    EditText mDetectLightMax;
    EditText mDetectBlur;
    EditText mDetectBlurNew;
    EditText mDetectFaceNum;
    TextView mDetect_Settings_light_blur;

    EditText mDetectRate;
    EditText mDetectSize;
    EditText mTrackerSize;
    EditText mFeatureSize;
    EditText mTrackerFaceSize;
    TextView mSettingDetect;

    ArrayAdapter<String> mDetectModeAdapter;
    ArrayList<String> detectModeList = new ArrayList<>();
    ArrayAdapter<String> mLiveModeAdapter;
    ArrayList<String> liveModeList = new ArrayList<>();

    Switch mInfrared_switch;
    Switch mSaveInfrared_switch;
    Switch mSaveInfraredLive_switch;
    Switch mSaveInfraredFake_switch;
    EditText mInfraredValue;
    TextView mSetting_infrared;
    Spinner mInfraredSpinner;
    ArrayAdapter<String> mInfraredModeAdapter;
    ArrayList<String> infraredModeList = new ArrayList<>();

    EditText mIouValue;
    TextView mSetting_iou;

    Switch mMulLiveness;
    Switch mMulAssist;
    EditText mMulFrame;
    EditText mMulThreshold;
    TextView mSetMulFrame;
    TextView mSetMulThreshold;

    boolean initInfrared = true;
    Switch mRemoveUnnormal;
    Switch mRetina_FD;

    @Override
    public int getLayoutId() {
        return R.layout.activity_debug;
    }

    @Override
    public void initViews() {
        mBack = (ImageView) findViewById(R.id.back);
        mTitle = (TextView)findViewById(R.id.title);

        mInfrared_switch = (Switch)findViewById(R.id.infrared_switch);
        mSaveInfrared_switch = (Switch)findViewById(R.id.saveinfrared_switch);
        mSaveInfraredLive_switch = (Switch)findViewById(R.id.saveinfraredlive_switch);
        mSaveInfraredFake_switch = (Switch)findViewById(R.id.saveinfraredfake_switch);
        mInfraredValue = (EditText)findViewById(R.id.infrared);
        mSetting_infrared = (TextView)findViewById(R.id.setting_infrared);
        mIouValue = (EditText)findViewById(R.id.iou);
        mSetting_iou = (TextView)findViewById(R.id.setting_iou);
        mInfraredSpinner = (Spinner)findViewById(R.id.infraredsp);

        mDetectSpinner = (Spinner) findViewById(R.id.detectsp);
        mLiveSpinner = (Spinner) findViewById(R.id.livesp);

        mUseNewLiveVersion_switch = (Switch) findViewById(R.id.use_new_switch);
        mUseFixLiveVersion_switch = (Switch) findViewById(R.id.use_fix_switch);
        mUseAWRGBLiveVersion_switch = (Switch) findViewById(R.id.use_AWRGB_switch);

        mCov_switch = (Switch) findViewById(R.id.cov_switch) ;
        mEnhance_switch = (Switch) findViewById(R.id.enhance_switch);
        mDebug_switch = (Switch) findViewById(R.id.debug_switch);
        mInfo_switch = (Switch) findViewById(R.id.info_switch);
        mRSb = (RelativeLayout) findViewById(R.id.rsb);
        mRSbv = (View) findViewById(R.id.rsbv);
        mRSn = (RelativeLayout) findViewById(R.id.rsn);
        mRSnv = (View) findViewById(R.id.rsnv);
        mRSs = (RelativeLayout) findViewById(R.id.rss);
        mRSsv = (View) findViewById(R.id.rssv);
        mRSf = (RelativeLayout) findViewById(R.id.rsf);
        mRSfv = (View) findViewById(R.id.rsfv);
        mRSl = (RelativeLayout) findViewById(R.id.rsl);
        mRSlv = (View) findViewById(R.id.rslv);
        mRSt = (RelativeLayout) findViewById(R.id.rst);
        mRStv = (View) findViewById(R.id.rstv);
        mRStan = (RelativeLayout) findViewById(R.id.rstan);
        mRStanv = (View) findViewById(R.id.rstanv);
        mRSerror = (RelativeLayout) findViewById(R.id.rserror);
        mRSerrorv = (View) findViewById(R.id.rserrorv);

        mSaveCov_switch = (Switch) findViewById(R.id.savecov_switch);
        mSaveBlur_switch = (Switch) findViewById(R.id.saveblur_switch);
        mSaveNoFace_switch = (Switch) findViewById(R.id.savenoface_switch);
        mSaveSS_switch = (Switch) findViewById(R.id.savess_switch);
        mSaveF_switch = (Switch) findViewById(R.id.savefake_switch);
        mSaveL_switch = (Switch) findViewById(R.id.savelive_switch);
        mSaveTracker_switch = (Switch) findViewById(R.id.savet_switch);
        mSaveTan_switch = (Switch) findViewById(R.id.savetan_switch);
        mSaveError_switch = (Switch) findViewById(R.id.saveerror_switch);

        mSimilarityFilter_switch = (Switch) findViewById(R.id.similarity_filter_switch);
        mRegisterLightMin = (EditText) findViewById(R.id.registerlightMin);
        mRegisterLightMax = (EditText) findViewById(R.id.registerlightMax);
        mRegisterBlur = (EditText) findViewById(R.id.registerblur);
        mFaceMinima = (EditText) findViewById(R.id.faceregister);
        mRegister_Settings_light_blur = (TextView) findViewById(R.id.settings_register_light_blur);

        mDetectLightMin = (EditText) findViewById(R.id.detectlightMin);
        mDetectLightMax = (EditText) findViewById(R.id.detectlightMax);
        mDetectBlur = (EditText) findViewById(R.id.detectblur);
        mDetectBlurNew = (EditText) findViewById(R.id.detectblurnew);
        mDetectFaceNum = (EditText) findViewById(R.id.detect_face_num);
        mDetect_Settings_light_blur = (TextView) findViewById(R.id.settings_detect_light_blur);

        mDetectRate = (EditText) findViewById(R.id.detect_rate);
        mDetectSize = (EditText) findViewById(R.id.detect_size);
        mTrackerSize = (EditText) findViewById(R.id.tracker_size);
        mFeatureSize = (EditText) findViewById(R.id.feature_size);
        mTrackerFaceSize = (EditText) findViewById(R.id.tracker_face_size);
        mSettingDetect = (TextView) findViewById(R.id.setting_detect);

        mMulLiveness = findViewById(R.id.mullive_switch);
        mMulAssist = findViewById(R.id.mulassist_switch);
        mMulFrame = findViewById(R.id.mulframe);
        mMulThreshold = findViewById(R.id.multhreshold);
        mSetMulFrame = findViewById(R.id.setting_mul_frame);
        mSetMulThreshold = findViewById(R.id.setting_mul_threshold);

        mRemoveUnnormal = (Switch) findViewById(R.id.remove_unnormal_switch);
        mRetina_FD = (Switch) findViewById(R.id.retina_fd);
    }

    @Override
    public void initData() {
        mTitle.setText(getResources().getString(R.string.debug));
        initInfrared = true;
        mInfrared_switch.setChecked(ConfigLib.detectWithInfraredLiveness);
        if (ConfigLib.detectWithInfraredLiveness) {
            mSaveInfrared_switch.setChecked(FaceDetectManager.getInfraredDebugPicsState());
        }else {
            mSaveInfrared_switch.setVisibility(View.GONE);
        }
        mSaveInfraredLive_switch.setChecked(Constants.DEBUG_SAVE_INFRARED_LIVE);
        mSaveInfraredFake_switch.setChecked(Constants.DEBUG_SAVE_INFRARED_FAKE);
        mInfraredValue.setText(String.valueOf(ConfigLib.livenessInFraredThreshold));
        infraredModeList.clear();
        infraredModeList.add("940NM");
        infraredModeList.add("850NM");
        mInfraredModeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, infraredModeList);
        mInfraredModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mInfraredSpinner.setAdapter(mInfraredModeAdapter);
        mInfraredSpinner.setSelection(Constants.INFRARED_MODE);

        mCov_switch.setChecked(ConfigLib.detectWithCovStatus);
        mEnhance_switch.setChecked(ConfigLib.enhanceMode);
        mDebug_switch.setChecked(AttConstants.DEBUG);
        mInfo_switch.setChecked(AttConstants.DEBUG_SHOW_FACEINFO);
        mSaveTan_switch.setChecked(Constants.DEBUG_SAVE_LIVEPIC_SDK);
        mSaveError_switch.setChecked(Constants.DEBUG_SAVE_ERROR);
        mSaveBlur_switch.setChecked(Constants.DEBUG_SAVE_BLUR);
        mSaveNoFace_switch.setChecked(Constants.DEBUG_SAVE_NOFACE);
        mSaveSS_switch.setChecked(Constants.DEBUG_SAVE_SIMILARITY_SMALL);
        mSaveF_switch.setChecked(Constants.DEBUG_SAVE_FAKE);
        mSaveL_switch.setChecked(Constants.DEBUG_SAVE_LIVE);
        mSaveTracker_switch.setChecked(Constants.DEBUG_SAVE_TRACKER);
        mSaveCov_switch.setChecked(ConfigLib.mouthDebug);

        mUseNewLiveVersion_switch.setChecked(ConfigLib.detectWithLivenessModeUseNew);
        mUseFixLiveVersion_switch.setChecked(ConfigLib.detectWithLivenessModeUseMix);
        mUseAWRGBLiveVersion_switch.setChecked(ConfigLib.detectWithAwRGBLiveness);

        refreshDebug();

        detectModeList.clear();
        detectModeList.add("0");
        detectModeList.add("1");
        detectModeList.add("2");
        detectModeList.add("3");
        detectModeList.add("4");
        detectModeList.add("16");
        detectModeList.add("17");
        detectModeList.add("18");
        detectModeList.add("19");
        detectModeList.add("20");
        mDetectModeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, detectModeList);
        mDetectModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDetectSpinner.setAdapter(mDetectModeAdapter);
        mDetectSpinner.setSelection(AttApp.sp.getInt(AttConstants.PREFS_DETECT_MODE,0));
        liveModeList.clear();
        liveModeList.add(getResources().getString(R.string.live_mode_normal));
        liveModeList.add(getResources().getString(R.string.live_mode_auto));
        mLiveModeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, liveModeList);
        mLiveModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLiveSpinner.setAdapter(mLiveModeAdapter);
        mLiveSpinner.setSelection(Constants.LIVENESS_MODE);

        mFaceMinima.setText(String.valueOf(ConfigLib.registerPicRect));
        mRegisterLightMax.setText(String.valueOf(ConfigLib.maxRegisterBrightness));
        mRegisterLightMin.setText(String.valueOf(ConfigLib.minRegisterBrightness));
        mRegisterBlur.setText(String.valueOf(ConfigLib.blurRegisterThreshold));
        mSimilarityFilter_switch.setChecked(ConfigLib.similarityFilter);

        mDetectLightMax.setText(String.valueOf(ConfigLib.maxRecognizeBrightness));
        mDetectLightMin.setText(String.valueOf(ConfigLib.minRecognizeBrightness));
        mDetectBlur.setText(String.valueOf(ConfigLib.blurRecognizeThreshold));
        mDetectBlurNew.setText(String.valueOf(ConfigLib.blurRecognizeNewThreshold));
        mDetectFaceNum.setText(String.valueOf(ConfigLib.maxShowDetectNum));

        mDetectRate.setText(String.valueOf(ConfigLib.picScaleRate));
        mDetectSize.setText(String.valueOf(FaceDetectManager.getFaceMinRect()));
        mTrackerSize.setText(String.valueOf(ConfigLib.picScaleSize));
        mFeatureSize.setText(String.valueOf(ConfigLib.Nv21ToBitmapScale));
        mTrackerFaceSize.setText(String.valueOf(ConfigLib.minRecognizeRect));

        mIouValue.setText(String.valueOf(ConfigLib.IOUThreshold));

        mMulFrame.setText(String.valueOf(ConfigLib.mul_detect_frames));
        mMulThreshold.setText(String.valueOf(ConfigLib.mul_threshold));

        mMulLiveness.setChecked(ConfigLib.detectWithLivenessModeUseMul);
        mMulAssist.setChecked(ConfigLib.detectWithLivenessWithMulAssist);
        mRemoveUnnormal.setChecked(ConfigLib.isRemoveUnnormalFace);
        mRetina_FD.setChecked(FaceConstants.RETINA_FD);
    }

    @Override
    public void initListeners() {
        mBack.setOnClickListener(this);
        mTitle.setOnClickListener(this);

        mSetMulFrame.setOnClickListener(this);
        mSetMulThreshold.setOnClickListener(this);

        mSetting_iou.setOnClickListener(this);
        mSetting_infrared.setOnClickListener(this);
        mUseNewLiveVersion_switch.setOnCheckedChangeListener(this);
        mUseFixLiveVersion_switch.setOnCheckedChangeListener(this);
        mUseAWRGBLiveVersion_switch.setOnCheckedChangeListener(this);
        mInfrared_switch.setOnCheckedChangeListener(this);
        mSaveInfrared_switch.setOnCheckedChangeListener(this);
        mSaveInfraredLive_switch.setOnCheckedChangeListener(this);
        mSaveInfraredFake_switch.setOnCheckedChangeListener(this);

        mRegister_Settings_light_blur.setOnClickListener(this);
        mDetect_Settings_light_blur.setOnClickListener(this);
        mSettingDetect.setOnClickListener(this);

        mSaveBlur_switch.setOnCheckedChangeListener(this);
        mSaveNoFace_switch.setOnCheckedChangeListener(this);
        mSaveSS_switch.setOnCheckedChangeListener(this);
        mEnhance_switch.setOnCheckedChangeListener(this);
        mCov_switch.setOnCheckedChangeListener(this);
        mDebug_switch.setOnCheckedChangeListener(this);
        mInfo_switch.setOnCheckedChangeListener(this);
        mSaveF_switch.setOnCheckedChangeListener(this);
        mSaveL_switch.setOnCheckedChangeListener(this);
        mSaveTracker_switch.setOnCheckedChangeListener(this);
        mSaveTan_switch.setOnCheckedChangeListener(this);
        mSaveError_switch.setOnCheckedChangeListener(this);
        mSaveCov_switch.setOnCheckedChangeListener(this);
        mSimilarityFilter_switch.setOnCheckedChangeListener(this);

        mUseFixLiveVersion_switch.setOnCheckedChangeListener(this);
        mUseFixLiveVersion_switch.setOnCheckedChangeListener(this);
        mUseAWRGBLiveVersion_switch.setOnCheckedChangeListener(this);

        mMulLiveness.setOnCheckedChangeListener(this);
        mMulAssist.setOnCheckedChangeListener(this);
        mRemoveUnnormal.setOnCheckedChangeListener(this);
        mRetina_FD.setOnCheckedChangeListener(this);

        mInfraredSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Constants.INFRARED_MODE = arg2;
                AttApp.sp.edit().putInt(AttConstants.PREFS_INFRAREDMODE,Constants.INFRARED_MODE).commit();
                if (ConfigLib.detectWithInfraredLiveness && !initInfrared) {
                    ToastUtils.showShort(getResources().getString(R.string.reopen));
                }
                if (initInfrared) {
                    initInfrared = false;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        mDetectSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                int mapValue = 16;
                switch (arg2) {
                    case 5:
                        mapValue = 16;
                        break;
                    case 6:
                        mapValue = 17;
                        break;
                    case 7:
                        mapValue = 18;
                        break;
                    case 8:
                        mapValue = 19;
                        break;
                    case 9:
                        mapValue = 20;
                        break;
                    default:
                        mapValue = arg2;
                }
                FaceDetectManager.setDetectFaceMode(mapValue);
                AttApp.sp.edit().putInt(AttConstants.PREFS_DETECT_MODE,arg2).commit();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        mLiveSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Constants.LIVENESS_MODE = arg2;
                AttApp.sp.edit().putInt(AttConstants.PREFS_LIVE_MODE,Constants.LIVENESS_MODE).commit();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    void setSucc(Object o){
        ToastUtils.showLong(getResources().getString(R.string.set_success)+" : "+o);
        DebugActivity.this.finish();
    }

    void setFail(){
        ToastUtils.showLong(getResources().getString(R.string.set_fail));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back:
            case R.id.title:
                DebugActivity.this.finish();
                break;

            case R.id.setting_mul_frame:
                String frame = mMulFrame.getEditableText().toString();
                if (frame.equals("")) {
                    setFail();
                    return;
                }
                int mulFrame = Integer.parseInt(frame);
                ConfigLib.mul_detect_frames = mulFrame;
                setSucc(ConfigLib.mul_detect_frames);
                AttApp.sp.edit().putInt(AttConstants.PREFS_MULFRAME,ConfigLib.mul_detect_frames).commit();
                break;

            case R.id.setting_mul_threshold:
                String threshold = mMulThreshold.getEditableText().toString();
                if (threshold.equals("")) {
                    setFail();
                    return;
                }
                int mulThreshold = Integer.parseInt(threshold);
                ConfigLib.mul_threshold = mulThreshold;
                setSucc(ConfigLib.mul_threshold);
                AttApp.sp.edit().putInt(AttConstants.PREFS_MULTHRESHOLD,ConfigLib.mul_threshold).commit();
                break;

            case R.id.settings_register_light_blur:
                String registermaxlight = mRegisterLightMax.getText().toString().trim();
                ConfigLib.maxRegisterBrightness = Float.valueOf(registermaxlight);
                String registerminlight = mRegisterLightMin.getText().toString().trim();
                ConfigLib.minRegisterBrightness = Float.valueOf(registerminlight);
                String registerblur = mRegisterBlur.getText().toString().trim();
                ConfigLib.blurRegisterThreshold = Float.valueOf(registerblur);
                String faceminima = mFaceMinima.getText().toString().trim();
                ConfigLib.registerPicRect = Integer.parseInt(faceminima);
                setSucc(ConfigLib.registerPicRect);
                setSucc(ConfigLib.maxRegisterBrightness +" | "+ ConfigLib.minRegisterBrightness +" | "+ ConfigLib.blurRegisterThreshold +" | "+ ConfigLib.registerPicRect);
                AttApp.sp.edit().putFloat(AttConstants.PREFS_MAXREGISTERBRIGHTNESS,ConfigLib.maxRegisterBrightness).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_MINREGISTERBRIGHTNESS,ConfigLib.minRegisterBrightness).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_BLURREGISTERTHRESHOLD,ConfigLib.blurRegisterThreshold).commit();
                AttApp.sp.edit().putInt(AttConstants.PREFS_FACEMINIMA,ConfigLib.registerPicRect).commit();
                break;

            case R.id.settings_detect_light_blur:
                String detectmaxlight = mDetectLightMax.getText().toString().trim();
                ConfigLib.maxRecognizeBrightness = Float.valueOf(detectmaxlight);
                String detectminlight = mDetectLightMin.getText().toString().trim();
                ConfigLib.minRecognizeBrightness = Float.valueOf(detectminlight);
                String detectblur = mDetectBlur.getText().toString().trim();
                ConfigLib.blurRecognizeThreshold = Float.valueOf(detectblur);
                String detectblurnew = mDetectBlurNew.getText().toString().trim();
                ConfigLib.blurRecognizeNewThreshold = Float.valueOf(detectblurnew);
                String detectfacenum = mDetectFaceNum.getText().toString().trim();
                ConfigLib.maxShowDetectNum = Integer.valueOf(detectfacenum);
                setSucc(ConfigLib.maxRecognizeBrightness +" | "+ ConfigLib.minRecognizeBrightness +" | "+ ConfigLib.blurRecognizeThreshold+" | "+ConfigLib.blurRecognizeNewThreshold+" | "+ConfigLib.maxShowDetectNum);
                AttApp.sp.edit().putFloat(AttConstants.PREFS_MAXRECOGNIZEBRIGHTNESS,ConfigLib.maxRecognizeBrightness).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_MINRECOGNIZEBRIGHTNESS,ConfigLib.minRecognizeBrightness).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_BLURECOGNIZETHRESHOLD,ConfigLib.blurRecognizeThreshold).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_BLURECOGNIZENEWTHRESHOLD,ConfigLib.blurRecognizeNewThreshold).commit();
                AttApp.sp.edit().putInt(AttConstants.PREFS_DETECT_FACE_NUM,ConfigLib.maxShowDetectNum).commit();
                break;

            case R.id.setting_detect:
                String rate = mDetectRate.getText().toString().trim();
                float floatRate = Float.parseFloat(rate);
                String size = mTrackerSize.getText().toString().trim();
                float floatSize = Float.parseFloat(size);
                String iSize = mDetectSize.getText().toString().trim();
                int intSize = Integer.parseInt(iSize);
                String iFSize = mFeatureSize.getText().toString().trim();
                int intFSize = Integer.parseInt(iFSize);
                String rFSize = mTrackerFaceSize.getText().toString().trim();
                float fRect = Float.parseFloat(rFSize);
                ConfigLib.picScaleRate = floatRate;
                ConfigLib.picScaleSize = floatSize;
                ConfigLib.Nv21ToBitmapScale = intFSize;
                FaceDetectManager.setFaceMinRect(intSize);
                ConfigLib.minRecognizeRect = fRect;
                setSucc(ConfigLib.picScaleRate +" | "+ConfigLib.picScaleSize+" | "+FaceDetectManager.getFaceMinRect()+" | "+ConfigLib.minRecognizeRect);
                AttApp.sp.edit().putFloat(AttConstants.PREFS_DETECT_RATE,ConfigLib.picScaleRate).commit();
                AttApp.sp.edit().putInt(AttConstants.PREFS_DETECT_SIZE,FaceDetectManager.getFaceMinRect()).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_TRACKER_SIZE,ConfigLib.picScaleSize).commit();
                AttApp.sp.edit().putInt(AttConstants.PREFS_FEATURE_SIZE,ConfigLib.Nv21ToBitmapScale).commit();
                AttApp.sp.edit().putFloat(AttConstants.PREFS_FACE_SIZE,ConfigLib.minRecognizeRect).commit();
                break;

            case R.id.setting_infrared:
                String irate = mInfraredValue.getText().toString().trim();
                float floatiRate = Float.parseFloat(irate);
                ConfigLib.livenessInFraredThreshold = floatiRate;
                setSucc(ConfigLib.livenessInFraredThreshold);
                AttApp.sp.edit().putFloat(AttConstants.PREFS_INFRAREDVALUE,ConfigLib.livenessInFraredThreshold).commit();
                break;

            case R.id.setting_iou:
                String iouValue = mIouValue.getText().toString().trim();
                float floatiIou = Float.parseFloat(iouValue);
                ConfigLib.IOUThreshold = floatiIou;
                setSucc(ConfigLib.IOUThreshold);
                AttApp.sp.edit().putFloat(AttConstants.PREFS_IOU,ConfigLib.IOUThreshold).commit();
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//        Log.d(TAG, "onCheckedChanged: use_new_switch = " + isChecked);
        switch (buttonView.getId()){
            case R.id.retina_fd:
                FaceDetectManager.setRetinaFD(isChecked);
                mRetina_FD.setChecked(FaceConstants.RETINA_FD);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_RETINA_FD, FaceConstants.RETINA_FD).commit();
                break;

            case R.id.remove_unnormal_switch:
                ConfigLib.isRemoveUnnormalFace = isChecked;
                mRemoveUnnormal.setChecked(ConfigLib.isRemoveUnnormalFace);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_REMOVE_UNNORMAL_FACE, ConfigLib.isRemoveUnnormalFace).commit();
                break;

            case R.id.mullive_switch:
                ConfigLib.detectWithLivenessModeUseMul = isChecked;
                mMulLiveness.setChecked(ConfigLib.detectWithLivenessModeUseMul);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_MULIVENESSSS, ConfigLib.detectWithLivenessModeUseMul).commit();
                break;

            case R.id.mulassist_switch:
                ConfigLib.detectWithLivenessWithMulAssist = isChecked;
                mMulAssist.setChecked(ConfigLib.detectWithLivenessWithMulAssist);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_MULASSISTSS, ConfigLib.detectWithLivenessWithMulAssist).commit();
                break;

            case R.id.cov_switch:
                ConfigLib.detectWithCovStatus = isChecked;
                mCov_switch.setChecked(ConfigLib.detectWithCovStatus);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_COVERMODE, ConfigLib.detectWithCovStatus).commit();
                break;

            case R.id.enhance_switch:
                ConfigLib.enhanceMode = isChecked;
                mEnhance_switch.setChecked(ConfigLib.enhanceMode);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_ENHANCEMODE,ConfigLib.enhanceMode).commit();
                break;

            case R.id.use_new_switch:
//                Log.d(TAG, "onCheckedChanged: use_new_switch = " + isChecked);
                ConfigLib.detectWithLivenessModeUseNew = isChecked;
                mUseNewLiveVersion_switch.setChecked(ConfigLib.detectWithLivenessModeUseNew);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_USENEWLIVEVERSION,ConfigLib.detectWithLivenessModeUseNew).commit();
                break;

            case R.id.use_fix_switch:
                ConfigLib.detectWithLivenessModeUseMix = isChecked;
                mUseFixLiveVersion_switch.setChecked(ConfigLib.detectWithLivenessModeUseMix);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_USEFIXLIVEVERSION,ConfigLib.detectWithLivenessModeUseMix).commit();
                break;

            case R.id.use_AWRGB_switch:
                ConfigLib.detectWithAwRGBLiveness = isChecked;
                mUseAWRGBLiveVersion_switch.setChecked(ConfigLib.detectWithAwRGBLiveness);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_USEAWRGBLIVEVERSION,ConfigLib.detectWithAwRGBLiveness).commit();
                break;


            case R.id.infrared_switch:
                ConfigLib.detectWithInfraredLiveness = isChecked;
                mInfrared_switch.setChecked(ConfigLib.detectWithInfraredLiveness);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_DETECTINFRARED,ConfigLib.detectWithInfraredLiveness).commit();
                ToastUtils.showShort(getResources().getString(R.string.reopen));
                break;

            case R.id.saveinfrared_switch:
                mSaveInfrared_switch.setChecked(isChecked);
                FaceDetectManager.setInfraredDebugPicsState(isChecked);
                break;

            case R.id.saveinfraredlive_switch:
                Constants.DEBUG_SAVE_INFRARED_LIVE = isChecked;
                mSaveInfraredLive_switch.setChecked(Constants.DEBUG_SAVE_INFRARED_LIVE);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_SAVEINFRAREDLIVE,Constants.DEBUG_SAVE_INFRARED_LIVE).commit();
                break;

            case R.id.saveinfraredfake_switch:
                Constants.DEBUG_SAVE_INFRARED_FAKE = isChecked;
                mSaveInfraredFake_switch.setChecked(Constants.DEBUG_SAVE_INFRARED_FAKE);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_SAVEINFRAREDFAKE,Constants.DEBUG_SAVE_INFRARED_FAKE).commit();
                break;

            case R.id.saveblur_switch:
                Constants.DEBUG_SAVE_BLUR = isChecked;
                mSaveBlur_switch.setChecked(Constants.DEBUG_SAVE_BLUR);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_SAVEBLURDATA,Constants.DEBUG_SAVE_BLUR).commit();
                break;

            case R.id.savenoface_switch:
                Constants.DEBUG_SAVE_NOFACE = isChecked;
                mSaveNoFace_switch.setChecked(Constants.DEBUG_SAVE_NOFACE);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_SAVENOFACEDATA,Constants.DEBUG_SAVE_NOFACE).commit();
                break;

            case R.id.savess_switch:
                Constants.DEBUG_SAVE_SIMILARITY_SMALL = isChecked;
                mSaveSS_switch.setChecked(Constants.DEBUG_SAVE_SIMILARITY_SMALL);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_SAVESSDATA,Constants.DEBUG_SAVE_SIMILARITY_SMALL).commit();
                break;

            case R.id.debug_switch:
                AttConstants.DEBUG = isChecked;
                mDebug_switch.setChecked(AttConstants.DEBUG);
                refreshDebug();
                FaceDetectManager.setDebug(AttConstants.DEBUG);
                AiwinnManager.getInstance().setDebug(AttConstants.DEBUG);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_DEBUG,AttConstants.DEBUG).commit();
                break;

            case R.id.info_switch:
                AttConstants.DEBUG_SHOW_FACEINFO = isChecked;
                mInfo_switch.setChecked(AttConstants.DEBUG_SHOW_FACEINFO);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_INOFO,AttConstants.DEBUG_SHOW_FACEINFO).commit();
                break;

            case R.id.savefake_switch:
                Constants.DEBUG_SAVE_FAKE = isChecked;
                mSaveF_switch.setChecked(Constants.DEBUG_SAVE_FAKE);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_FAKE,Constants.DEBUG_SAVE_FAKE).commit();
                break;

            case R.id.savelive_switch:
                Constants.DEBUG_SAVE_LIVE = isChecked;
                mSaveL_switch.setChecked(Constants.DEBUG_SAVE_LIVE);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_LIVE,Constants.DEBUG_SAVE_LIVE).commit();
                break;

            case R.id.savet_switch:
                Constants.DEBUG_SAVE_TRACKER = isChecked;
                mSaveTracker_switch.setChecked(Constants.DEBUG_SAVE_TRACKER);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_TRACKER,Constants.DEBUG_SAVE_TRACKER).commit();
                break;

            case R.id.savetan_switch:
                Constants.DEBUG_SAVE_LIVEPIC_SDK = isChecked;
                mSaveTan_switch.setChecked(Constants.DEBUG_SAVE_LIVEPIC_SDK);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_ST,Constants.DEBUG_SAVE_LIVEPIC_SDK).commit();
                break;

            case R.id.saveerror_switch:
                Constants.DEBUG_SAVE_ERROR = isChecked;
                mSaveError_switch.setChecked(Constants.DEBUG_SAVE_ERROR);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_SE,Constants.DEBUG_SAVE_ERROR).commit();
                break;

            case R.id.savecov_switch:
                ConfigLib.mouthDebug = isChecked;
                mSaveCov_switch.setChecked(ConfigLib.mouthDebug);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_SC,ConfigLib.mouthDebug).commit();
                break;
            case R.id.similarity_filter_switch:
                ConfigLib.similarityFilter = isChecked;
                mSimilarityFilter_switch.setChecked(ConfigLib.similarityFilter);
                AttApp.sp.edit().putBoolean(AttConstants.PREFS_SIMILARITY_FILTER, ConfigLib.similarityFilter).commit();
                break;
        }
    }

    private void refreshDebug() {
        if (AttConstants.DEBUG) {
            mRSb.setVisibility(View.VISIBLE);
            mRSbv.setVisibility(View.VISIBLE);
            mRSn.setVisibility(View.VISIBLE);
            mRSnv.setVisibility(View.VISIBLE);
            mRSs.setVisibility(View.VISIBLE);
            mRSsv.setVisibility(View.VISIBLE);
            mRSf.setVisibility(View.VISIBLE);
            mRSfv.setVisibility(View.VISIBLE);
            mRSl.setVisibility(View.VISIBLE);
            mRSlv.setVisibility(View.VISIBLE);
            mRSt.setVisibility(View.VISIBLE);
            mRStv.setVisibility(View.VISIBLE);
            mRStan.setVisibility(View.VISIBLE);
            mRStanv.setVisibility(View.VISIBLE);
            mRSerror.setVisibility(View.VISIBLE);
            mRSerrorv.setVisibility(View.VISIBLE);
        }else {
            mRSb.setVisibility(View.GONE);
            mRSbv.setVisibility(View.GONE);
            mRSn.setVisibility(View.GONE);
            mRSnv.setVisibility(View.GONE);
            mRSs.setVisibility(View.GONE);
            mRSsv.setVisibility(View.GONE);
            mRSf.setVisibility(View.GONE);
            mRSfv.setVisibility(View.GONE);
            mRSl.setVisibility(View.GONE);
            mRSlv.setVisibility(View.GONE);
            mRSt.setVisibility(View.GONE);
            mRStv.setVisibility(View.GONE);
            mRStan.setVisibility(View.GONE);
            mRStanv.setVisibility(View.GONE);
            mRSerror.setVisibility(View.GONE);
            mRSerrorv.setVisibility(View.GONE);
        }
    }

}
