package com.ppyy.android.mc.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ppyy.android.mc.R;
import com.ppyy.android.mc.bean.SendInfoBean;
import com.ppyy.android.mc.utils.NetworkCore;

import java.util.ArrayList;
import java.util.logging.LogRecord;

/**
 * Created by Administrator on 2017/5/1.
 */

public class AdapterWhiteslit extends BaseAdapter {
    private final WhitelistActivity context;
    private final ArrayList<String> list;

    public AdapterWhiteslit(WhitelistActivity c, ArrayList<String> list) {
        this.context = c;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh=null;
        if(convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.view_item_whitelist,null);
            vh.tvName= (TextView) convertView.findViewById(R.id.whitelist_item_tv);
            vh.btn= (Button) convertView.findViewById(R.id.whitelist_item_delete_btn);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        vh.tvName.setText(list.get(position));
        vh.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkCore.doGet("write?command=\"whitelist remove " +list.get(position)+"\"", null, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what <= 0) {
                            context.doShowMesage(msg.obj + "");
                            return;
                        }
                        context.doShowMesage("删除白名单成功！");
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                }, SendInfoBean.class);


            }
        });

        return convertView;
    }
    private class ViewHolder{
        TextView tvName;
        Button btn;
    }

}
