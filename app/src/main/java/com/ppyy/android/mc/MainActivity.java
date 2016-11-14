package com.ppyy.android.mc;


import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wangtao.universallylibs.BaseActivity;
import com.wangtao.universallylibs.utils.NetworkCore;

import java.util.HashMap;

public class MainActivity extends BaseActivity {
    private EditText etAccount, etPwd;

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_main);
        etAccount = (EditText) findViewById(R.id.main_account_et);
        etPwd = (EditText) findViewById(R.id.main_pwd_et);
    }

    @Override
    public void setAllData() {
        etAccount.setText(preference.getString("user_account", ""));
        etAccount.setSelection(etAccount.getText().length());
    }

    public void onclickLogin(View view) {
        final String account = etAccount.getText().toString();
        if (TextUtils.isEmpty(account)) {
            doShowToastLong("账号不能为空");
            return;
        }
        String pwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            doShowToastLong("密码不能为空");
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", account);
        params.put("password", pwd);
        NetworkCore.doGet("login", params, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                SendInfoBean serverInfo = (SendInfoBean) msg.obj;
                if (serverInfo.code == 0) {
                    doShowMesage(serverInfo.msg);
                    return;
                }
                preference.edit().putString("user_account", account).commit();
                finish();
                doStartOter(ServerInfoActivity.class);
            }
        }, SendInfoBean.class);


    }
}
