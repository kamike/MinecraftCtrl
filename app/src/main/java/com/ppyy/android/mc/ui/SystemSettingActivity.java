package com.ppyy.android.mc.ui;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ppyy.android.mc.R;
import com.ppyy.android.mc.bean.SendInfoBean;
import com.ppyy.android.mc.utils.NetworkCore;
import com.wangtao.universallylibs.BaseActivity;

public class SystemSettingActivity extends BaseActivity {
    private ListView listView;


    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_system_setting);
        doSetTitle(R.id.system_setting_include, "游戏设置");
        listView = (ListView) findViewById(R.id.system_setting_listview);

    }

    @Override
    public void setAllData() {
        listView.setAdapter(new AdapterSystem());
    }

    private String[] arrayName = {"启用/禁用火的蔓延", "死亡不掉落", "启用/禁用生物掉落物", "启用/禁用生物生成", "启用/禁用生命恢复", "启用/禁用日夜交替"};
    private String[] arrayRule = {"doFireTick", "keepInventory", "doMobLoot", "doMobSpawning", "naturalRegeneration", "doDaylightCycle"};

    private class AdapterSystem extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayName.length;
        }

        @Override
        public Object getItem(int position) {
            return arrayName[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = LayoutInflater.from(SystemSettingActivity.this).inflate(R.layout.view_item_system_setting, null);
                vh.tvName = (TextView) convertView.findViewById(R.id.system_setting_item_tv);
                vh.btnYes = (Button) convertView.findViewById(R.id.system_setting_item_yes_btn);
                vh.btnNo = (Button) convertView.findViewById(R.id.system_setting_item_no_btn);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tvName.setText(arrayName[position]);
            vh.btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doShowProgress();
                    NetworkCore.doGet("write?command=\"gamerule " + arrayRule[position] + " true\"", null, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what <= 0) {
                                doShowMesage(msg.obj + "");
                                return;
                            }
                            doDismiss();
                            doShowMesage("修改成功，执行的命令是：\n"+"gamerule " + arrayRule[position] + " true");

                        }
                    }, SendInfoBean.class);
                }
            });
            vh.btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doShowProgress();
                    NetworkCore.doGet("write?command=\"gamerule " + arrayRule[position] + " false\"", null, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what <= 0) {
                                doShowMesage(msg.obj + "");
                                return;
                            }
                            doDismiss();
                            doShowMesage("修改成功，执行的命令是：\n"+"gamerule " + arrayRule[position] + " false");

                        }
                    }, SendInfoBean.class);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private TextView tvName;
            private Button btnYes, btnNo;
        }
    }
}
