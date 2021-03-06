package com.ppyy.android.mc.ui;


import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ppyy.android.mc.R;
import com.ppyy.android.mc.utils.NetworkCore;
import com.wangtao.universallylibs.BaseActivity;

import java.util.HashMap;

public class LoginActivity extends BaseActivity {
    private EditText etAccount, etPwd;

    @Override
    public void initShowLayout() {

        setContentView(R.layout.activity_main);
        etAccount = (EditText) findViewById(R.id.main_account_et);
        etPwd = (EditText) findViewById(R.id.main_pwd_et);
    }

    @Override
    public void setAllData() {
        if (preference.getBoolean("is_login", false)) {
            finish();
            doStartOter(ServerInfoActivity.class);
            return;
        }
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
        params.put("userName", account);
        params.put("password", pwd);
        doShowProgress();
        NetworkCore.doGet("login", params, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                doLogMsg("login:" + msg.obj);
                doDismiss();
                if (msg.what <= 0) {
                    doShowMesage(msg.obj+"");
                    return;
                }
                preference.edit().putString("user_account", account).commit();
                preference.edit().putBoolean("is_login", true).commit();
                finish();
                doStartOter(ServerInfoActivity.class);
            }
        },1);
    }
}
