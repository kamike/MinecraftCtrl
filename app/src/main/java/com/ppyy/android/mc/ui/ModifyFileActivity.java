package com.ppyy.android.mc.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ppyy.android.mc.R;
import com.wangtao.universallylibs.BaseActivity;
import com.wangtao.universallylibs.utils.FileUtils;

public class ModifyFileActivity extends BaseActivity {

    private ListView listview;

    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_modify_file);
        listview = (ListView) findViewById(R.id.modify_file_listview);

    }

    private String[] array = {"server.proper","whitelist.json","essass/init.txt","xxxxxx"};

    @Override
    public void setAllData() {
        doSetTitle(R.id.modify_file_include, "修改配置文件");
        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, array));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doShowMesage("是否更改此配置文件\n" + array[position], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);

                        try {
                            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1);
                        } catch (android.content.ActivityNotFoundException ex) {
                            doShowToast("未安装文件选择器");
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = FileUtils.getRealPathFromURI(this, uri);
                    doShowMesage("要替换的文件："+path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
