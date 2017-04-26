package com.ppyy.android.mc.ui;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ppyy.android.mc.R;
import com.ppyy.android.mc.bean.SendInfoBean;
import com.ppyy.android.mc.utils.NetworkCore;
import com.wangtao.universallylibs.BaseActivity;


public class CommandActivity extends BaseActivity {
    private Spinner sp;
    private String[] list = {"/say 对所有玩家发送一句话", "/clear 清空玩家物品栏", "/op 给玩家op权限", "/deop 删除op", "/ban 封禁玩家", "/unban 解封玩家"};
    private EditText etCammand;
    private String[] listCammand = {"say", "clear", "op", "deop", "ban", "unban"};

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_command);
        doSetTitle(R.id.cammand_include, "发送命令");
        sp = (Spinner) findViewById(R.id.cammand_list_spinner);
        etCammand = (EditText) findViewById(R.id.cammand_content_et);
        etCammand.setHint("输入要发送的内容");
    }

    @Override
    public void setAllData() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        etCammand.setHint("输入要发送的内容");
                        break;
                    case 1:
                        etCammand.setHint("输入要清空的玩家名称");
                        break;
                    case 2:
                        etCammand.setHint("输入要给OP的玩家名称");
                        break;
                    case 3:
                        etCammand.setHint("输入要删除OP的玩家名称");
                        break;
                    case 4:
                        etCammand.setHint("输入要封禁的玩家名称");
                        break;
                    case 5:
                        etCammand.setHint("输入要解禁的玩家名称");
                        break;
                }
                currentPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int currentPosition = 0;

    public void onclickSendCmd(View view) {
        String str = etCammand.getText().toString();
        if (TextUtils.isEmpty(str)) {
            doShowToastLong("输入的内容为空！");
            return;
        }
        NetworkCore.doGet("write?command=" + listCammand[currentPosition] + " " + str, null, handler, SendInfoBean.class);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            doLogMsg("cammand:" + msg.obj);
            if(msg.what<=0){
                doShowMesage(msg.obj+"");
                return;
            }
            doShowMesage("执行成功！");
        }
    };
}
