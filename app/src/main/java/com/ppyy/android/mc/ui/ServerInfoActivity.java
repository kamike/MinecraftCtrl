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
    private String[] items = {"控制台(发送命令)","查看服务器日志", "权限管理(白名单)","游戏设置", "关闭退出软件"};
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
        linearScroll.addView(addShowTxtContent("开启时间：", "--"));
        linearScroll.addView(addShowTxtContent("开启时长：", "--"));
        linearScroll.addView(addShowTxtContent("最大玩家数：", "99"));
        linearScroll.addView(addShowTxtContent("在线玩家：", "5/20"));
        linearScroll.addView(addShowTxtContent("服务器内存：", "0M"));
        linearScroll.addView(addShowTxtContent("已使用内存：", "0M"));
        linearScroll.addView(addShowTxtContent("剩余内存：", "0M"));
        linearScroll.addView(addShowTxtContent("游戏版本：", "--"));
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
            updataShowTxtContent(linearScroll, "开启时间：", server.startTime);
            updataShowTxtContent(linearScroll, "开启时长：", server.sustainTime);
            updataShowTxtContent(linearScroll, "最大玩家数：", server.maxPlayer);
            updataShowTxtContent(linearScroll, "在线玩家：", server.playerNum);
            updataShowTxtContent(linearScroll, "服务器内存：", server.totalMem);
            updataShowTxtContent(linearScroll, "已使用内存：", server.usedMem);
            updataShowTxtContent(linearScroll, "剩余内存：", server.freeMem);
            updataShowTxtContent(linearScroll, "游戏版本：", server.gameVersion);
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
            //刷新服务器信息
            setAllData();
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
                        doStartOter(SystemLogActivity.class);

                        break;
                    case 2:
                        doStartOter(WhitelistActivity.class);

                        break;
                    case 3:
                        doStartOter(SystemSettingActivity.class);
                        break;
                    case 4:
                        preference.edit().remove("user_account").commit();
                        finish();
                        break;
                }
            }
        }).setPositiveButton("取消", null).show();
    }

    public void onclickRefershInfo(View view) {
        NetworkCore.doGet("info", null, handlerInit, ServerInfoBean.class);
        doShowProgress();
    }
}
