package com.ppyy.android.mc;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wangtao.universallylibs.BaseActivity;

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
        String account = etAccount.getText().toString();
        if (TextUtils.isEmpty(account)) {
            doShowToastLong("账号不能为空");
            return;
        }
        String pwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            doShowToastLong("密码不能为空");
            return;
        }
        preference.edit().putString("user_account", account).commit();
        finish();
        doStartOter(ServerInfoActivity.class);
    }
}
