package com.wangtao.universallylibs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wangtao.universallylibs.utils.SaveCache;

/**
 * Created by BaseActivity on 2016/9/9.
 * QQ：751190264
 */
public abstract class BaseActivity extends Activity {
    public static final String NETWORK_EXCEPTION = "网络异常";
    /**
     * 导航栏高度
     */
    public static int TILE_HEIGHT = 200;
    /**
     * 像素密度
     */
    public static float screenDensity = 1;
    /**
     * 屏幕高度
     */
    public static int screenHeight = 1920;
    /**
     * 屏幕宽度
     */
    public static int screenWidth = 1080;
    protected Context mContext;
    protected static SaveCache mSaveCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        initShowLayout();
        initCacheData();
        setupScreen();
        setAllData();
    }

    public static SharedPreferences preference = null;

    private void initCacheData() {
        if (mSaveCache == null) {
            mSaveCache = new SaveCache(this);
        }
        if (preference == null) {
            preference = getSharedPreferences("preference_cache", Context.MODE_PRIVATE);
        }
    }


    public abstract void initShowLayout();

    public abstract void setAllData();

    public void setupScreen() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        if (frame.top != 0) {
            this.TILE_HEIGHT = frame.top;// 获取导航栏的高度,这里必须在界面绘制出来才能正确获取
        } else {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                TILE_HEIGHT = mContext.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenDensity = metrics.density;
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

    }

    /**
     * 根据界面名称打印日志
     *
     * @param message
     */
    public void doLogMsg(String message) {
        Log.i(getLocalClassName(), "" + message);
    }

    public void doShowMesage(String msg, DialogInterface.OnClickListener listener) {
        if (isFinishing()) {
            return;
        }
        new AlertDialog.Builder(this).setTitle(null).setMessage(msg).setNegativeButton("确定", listener).show();
    }

    public void doShowMesage(String msg) {
        if (isFinishing()) {
            return;
        }
        doShowMesage(msg, null);
    }

    /**
     * 短消息提示
     *
     * @param msg
     */
    public void doShowToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短消息提示
     *
     * @param msg
     */
    public void doShowToast(int msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长消息提示
     *
     * @param msg
     */
    public void doShowToastLong(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 长消息提示
     *
     * @param msg
     */
    public void doShowToastLong(int msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 启动另外一个界面
     *
     * @param activity
     */
    public void doStartOter(Class activity) {
        Intent intentActive = new Intent(this, activity);
        startActivity(intentActive);
    }

    public static boolean isStatusSuccess(String status) {
        return TextUtils.equals(status, "success");
    }

    public View addShowTxtContent(String title, String content) {
        LinearLayout linear = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_item_show_txt, null);
        TextView tvTitle = (TextView) linear.findViewById(R.id.item_show_txt_name);
        TextView tvContent = (TextView) linear.findViewById(R.id.item_show_txt_content);
        linear.setTag(title);
        tvContent.setTag("content");
        tvTitle.setText(title);
        tvContent.setText(content);
        return linear;
    }

    public void updataShowTxtContent(LinearLayout linear, String tag, String newContent) {
        if (linear == null) {
            return;
        }
        for (int i = 0; i < linear.getChildCount(); i++) {
            if (tag.equals(linear.getChildAt(i).getTag())) {
                try {
                    LinearLayout grounp = (LinearLayout) linear.getChildAt(i);
                    for (int j = 0; j < grounp.getChildCount(); j++) {
                        View v = grounp.getChildAt(j);
                        if (v instanceof LinearLayout) {
                            LinearLayout item = (LinearLayout) v;
                            for (int index = 0; index < item.getChildCount(); index++) {
                                View view = item.getChildAt(index);
                                if ("content".equals(view.getTag())) {
                                    TextView tv = (TextView) view;
                                    tv.setText(newContent);
                                    return;
                                }
                            }

                        }

                    }

                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doSetTitle(int id, String title) {
        View linear = findViewById(id);
        if (linear == null) {
            return;
        }
        TextView tvTitle = (TextView) linear
                .findViewById(R.id.include_title_tv);
        ImageView ivBack = (ImageView) linear
                .findViewById(R.id.include_back_iv);
        if(tvTitle==null){
            doLogMsg("doSetTitle："+tvTitle);
            return;
        }
        tvTitle.setText(title);
        ivBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}
