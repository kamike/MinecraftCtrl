package com.ppyy.android.mc.ui;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ppyy.android.mc.R;
import com.ppyy.android.mc.bean.SendInfoBean;
import com.ppyy.android.mc.bean.ServerInfoBean;
import com.ppyy.android.mc.utils.NetworkCore;
import com.wangtao.universallylibs.BaseActivity;

public class ServerInfoActivity extends BaseActivity {
    private LinearLayout linearScroll;
    private String[] items = {"控制台(发送命令)", "配置文件修改(插件)", "权限管理(白名单)", "查看服务器日志", "关闭退出软件"};
    private Button btnStart;
    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_server_info);
        linearScroll = (LinearLayout) findViewById(R.id.server_info_linear);
        btnStart= (Button) findViewById(R.id.server_info_start_btn);
        btnStart.setOnClickListener(onclickStartOrStop());
    }



    @Override
    public void setAllData() {


        NetworkCore.doGet("info", null, handlerInit, ServerInfoBean.class);
        doShowProgress();
        linearScroll.removeAllViews();
        linearScroll.addView(addShowTxtContent("运行状态：", "停止..."));
        linearScroll.addView(addShowTxtContent("开启时长：", "1天2小时50分"));
        linearScroll.addView(addShowTxtContent("在线玩家：", "5/20"));
    }

    private Handler handlerInit = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            doLogMsg("mainObj:"+msg.obj);
            doDismiss();
            if (msg.what <= 0) {
                doShowMesage(msg.obj+"");
                return;
            }
            ServerInfoBean server = new Gson().fromJson(msg.obj+"", ServerInfoBean.class);
            if (server == null) {
                return;
            }
            String str = "未启动";
            if (server.status.equals("1")) {
                isStarting=true;
                str = "已启动";
            }else{
                isStarting=false;
            }
            updataBtnTxt();
            updataShowTxtContent(linearScroll, "运行状态：", str);
            updataShowTxtContent(linearScroll, "开启时长：", server.sustainTime);
            updataShowTxtContent(linearScroll, "在线玩家：", server.playerNum);
        }
    };

    private boolean isStarting=false;

    private void updataBtnTxt(){
        if(isStarting){
            btnStart.setText("关闭服务器");
        }else{
            btnStart.setText("开启服务器");
        }
    }

    private View.OnClickListener onclickStartOrStop() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStarting){
                    NetworkCore.doGet("stop", null, handler, SendInfoBean.class);
                }else{
                    NetworkCore.doGet("start", null, handler, SendInfoBean.class);
                }
                doShowProgress();
            }
        };
    }





    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            doLogMsg(""+msg.obj);
           doDismiss();
            if(msg.what<=0){
                doShowMesage(msg.obj+"");
                return;
            }
            isStarting=!isStarting;
            updataBtnTxt();

        }
    };

    public void onclickFunction(View view) {
        new AlertDialog.Builder(this).setTitle("功能选择").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        doStartOter(CommandActivity.class);
                        break;
                    case 1:
                        doStartOter(ModifyFileActivity.class);
                        break;
                    case 2:

                        break;
                    case 3:
                        doStartOter(SystemLogActivity.class);
                        break;
                    case 4:
                        preference.edit().remove("user_account").commit();
                        finish();
                        break;
                }
            }
        }).setPositiveButton("取消", null).show();
    }
}
