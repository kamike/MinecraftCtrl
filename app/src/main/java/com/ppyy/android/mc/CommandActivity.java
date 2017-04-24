package com.ppyy.android.mc;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wangtao.universallylibs.BaseActivity;


public class CommandActivity extends BaseActivity {
    private Spinner sp;
    private String[] list={"/say 对所有玩家发送一句话","/list 查看玩家列表","/give 给玩家一些物品","/op 给玩家op权限","",""};
    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_command);
        doSetTitle(R.id.cammand_include,"发送命令");
        sp= (Spinner) findViewById(R.id.cammand_list_spinner);
    }

    @Override
    public void setAllData() {
        ArrayAdapter   adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adapter);
    }
}
