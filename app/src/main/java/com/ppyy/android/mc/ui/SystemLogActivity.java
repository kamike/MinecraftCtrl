package com.ppyy.android.mc.ui;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ppyy.android.mc.R;
import com.ppyy.android.mc.bean.ServerInfoBean;
import com.ppyy.android.mc.utils.NetworkCore;
import com.wangtao.universallylibs.BaseActivity;

public class SystemLogActivity extends BaseActivity {


    private LinearLayout linearScroll;
    @Override
    public void initShowLayout() {

        setContentView(R.layout.activity_system_log);
        linearScroll= (LinearLayout) findViewById(R.id.system_log_linear);
        doSetTitle(R.id.log_include,"查看系统日志");
    }

    @Override
    public void setAllData() {
        NetworkCore.doGet("log", null, handler, ServerInfoBean.class);
        doShowProgress();
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            doLogMsg("日志："+msg.obj);
            doDismiss();
            if(msg.what<=0){
                doShowMesage(msg.obj+"");
                return;
            }
            linearScroll.removeAllViews();
            TextView tv=new TextView(SystemLogActivity.this);
            tv.setText(""+msg.obj);
            linearScroll.addView(tv);
        }
    };

    public void onclickRefershLog(View view) {
        setAllData();
    }
}
