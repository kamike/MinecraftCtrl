package com.ppyy.android.mc.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ppyy.android.mc.R;
import com.ppyy.android.mc.bean.SendInfoBean;
import com.ppyy.android.mc.bean.ServerInfoBean;
import com.ppyy.android.mc.utils.NetworkCore;
import com.wangtao.universallylibs.BaseActivity;
import com.wangtao.universallylibs.utils.FileUtils;

import java.util.ArrayList;

public class WhitelistActivity extends BaseActivity {

    private ListView listview;

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_modify_file);
        listview = (ListView) findViewById(R.id.modify_file_listview);

    }

    private String[] array = {"server.proper", "whitelist.json", "essass/init.txt", "xxxxxx"};

    @Override
    public void setAllData() {
        doSetTitle(R.id.modify_file_include, "修改白名单");
        list = new ArrayList<>();
        NetworkCore.doGet("whitelist", null, handlerInit, ServerInfoBean.class);


    }

    private Handler handlerInit = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            doLogMsg("mainObj:" + msg.obj);
            doDismiss();
            if (msg.what <= 0) {
                doShowMesage(msg.obj + "");
                return;
            }
            String arrayStr = msg.obj + "";

            if (TextUtils.isEmpty(arrayStr)) {
                doShowMesage("服务器的白名单为空");
                return;
            }
            String[] array = arrayStr.split("\n");
            if (array == null || array.length == 0) {
                doShowMesage("服务器的白名单格式不正确：" + msg.obj);
                return;
            }
            list.clear();
            for (String str : array) {
                list.add(str);
            }
            listview.setAdapter(new AdapterWhiteslit(WhitelistActivity.this, list));


        }
    };
    private ArrayList<String> list;

    public void onclickAddWhitelist(View view) {
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setView(et).setTitle("添加白名单").setPositiveButton("取消", null).setNegativeButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String str = et.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    doShowMesage("要添加的白名单为空！");
                    return;
                }

                dialog.dismiss();
                doShowProgress();
                NetworkCore.doGet("write?command=\"whitelist add " + str + "\"", null, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what <= 0) {
                            doShowMesage(msg.obj + "");
                            return;
                        }
                        doDismiss();
                        doShowToastLong("添加成功！");
                        list.add(str);
                        listview.setAdapter(new AdapterWhiteslit(WhitelistActivity.this, list));
                    }
                }, SendInfoBean.class);
            }
        }).show();


    }
}
