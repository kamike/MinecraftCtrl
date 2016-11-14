package com.ppyy.android.mc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;

import com.wangtao.universallylibs.BaseActivity;
import com.wangtao.universallylibs.utils.NetworkCore;

import java.util.HashMap;

public class ServerInfoActivity extends BaseActivity {
    private LinearLayout linearScroll;
    private String[] items = {"控制台(发送命令)", "配置文件修改(插件)", "权限管理(白名单)", "参数设置", "关闭退出软件"};

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_server_info);
        linearScroll = (LinearLayout) findViewById(R.id.server_info_linear);
    }

    @Override
    public void setAllData() {
        linearScroll.addView(addShowTxtContent("运行状态：", "停止..."));
        linearScroll.addView(addShowTxtContent("开启时长：", "1天2小时50分"));
        linearScroll.addView(addShowTxtContent("剩余内存：", "3.65G/8G"));
        linearScroll.addView(addShowTxtContent("在线玩家：", "5/20"));
        linearScroll.addView(addShowTxtContent("白名单是否开启：", "true"));
        linearScroll.addView(addShowTxtContent("其他参数：", "xxxxxx"));
    }

    public void onclickFunction(View view) {
        new AlertDialog.Builder(this).setTitle("功能选择").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:

                        break;
                    case 1:
                        doStartOter(ModifyFileActivity.class);

                        break;
                    case 2:

                        break;
                    case 3:
                        break;
                    case 4:
                        preference.edit().remove("user_account").commit();
                        finish();
                        break;
                }
            }
        }).setPositiveButton("取消", null).show();
    }

    public void onclickRestartServico(View view) {
        HashMap<String, Object> params = new HashMap<>();
        codeFunction=3;
        params.put("name", codeFunction);
        NetworkCore.doGet("minecraft.mc",params,handler,SendInfoBean.class);
        progress=ProgressDialog.show(mContext,null,"操作中...",false);
    }

    public void onclickClosedServico(View view) {
        HashMap<String, Object> params = new HashMap<>();
        codeFunction=2;
        params.put("name", codeFunction);
        NetworkCore.doGet("minecraft.mc",params,handler,SendInfoBean.class);
        handler.sendEmptyMessageDelayed(0, 3000);
        progress=ProgressDialog.show(mContext,null,"操作中...",false);
    }
    private int codeFunction=1;

    private ProgressDialog progress = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progress != null) {
                progress.dismiss();
            }
            SendInfoBean serverInfo= (SendInfoBean) msg.obj;
            if(serverInfo.code==0){
                doShowMesage(serverInfo.msg);
                return;
            }
            switch (codeFunction) {
                case 1:
                    doShowToastLong("获取服务器信息！");
                    updataShowTxtContent(linearScroll, "运行状态：", "已关闭.......");
                    break;

                case 2:
                    doShowToastLong("成功关闭服务器！");
                    updataShowTxtContent(linearScroll, "运行状态：", "已关闭.......");
                    break;
                case 3:
                    doShowToastLong("服务器重启...");
                    updataShowTxtContent(linearScroll, "运行状态：", "开启中.......");
                    break;
                default:

                    break;
            }
        }
    };
}
