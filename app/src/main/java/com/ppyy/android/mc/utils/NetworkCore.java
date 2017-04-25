package com.ppyy.android.mc.utils;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.ppyy.android.mc.bean.SendInfoBean;
import com.wangtao.universallylibs.BaseActivity;
import com.wangtao.universallylibs.ConfigProperties;
import com.wangtao.universallylibs.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by NetworkCore on 2016/7/11.
 * QQ：751190264
 */
public class NetworkCore {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8;");
    private static final MediaType MEDIA_TYPE_IMG = MediaType.parse("image/jpg");

    public static void doGet(String urlName, HashMap<String, Object> params, final Handler handler, final int flag) {
        try {
            OkHttpClient client = new OkHttpClient();
            StringBuffer sb = new StringBuffer();
            if (params != null) {
                sb.append("?");
                for (String key : params.keySet()) {
                    sb.append(key).append("=").append(params.get(key)).append("&");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            String url = ConfigProperties.baseUrl + urlName + sb.toString();
            LogUtils.logNetwork(url);
            Request req = new Request.Builder().url(url).build();
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (handler == null) {
                        return;
                    }
                    Message msg = new Message();
                    msg.obj = BaseActivity.NETWORK_EXCEPTION;
                    msg.what = -1;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();

                    LogUtils.logNetwork("onResponse:" + res);
                    if (handler == null) {
                        return;
                    }
                    Message msg = new Message();
                        try {
                            SendInfoBean send = JSON.parseObject(res, SendInfoBean.class);
                            if(send==null){
                                return;
                            }
                            if (!"success".equals(send.msg)) {
                                msg.what = 0;
                                msg.obj=send.remind;
                                handler.sendMessage(msg);
                                return;
                            }
                            msg.obj = send.obj;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    msg.what = flag;
                    handler.sendMessage(msg);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doGet(String urlName, HashMap<String, Object> params, final Handler handler, final Class t) {
        doGet(urlName, params, handler, 1);
    }

    public static void doPost(String urlName, HashMap<String, Object> params, final Handler handler, final int flag, final Class t) {
        try {
            OkHttpClient client = new OkHttpClient();
            StringBuffer sb = new StringBuffer("?");
            FormBody.Builder formbody = new FormBody.Builder();
//            if (params != null) {
//                for (String key : params.keySet()) {
//                    if (ConfigProperties.isDebug) {
//                        sb.append(key).append("=").append(params.get(key));
//                        sb.append("&");
//                    }
//                    formbody.add(key, params.get(key) + "");
//                }
//                sb.deleteCharAt(sb.length() - 1);
//            }
            LogUtils.logNetwork(ConfigProperties.baseUrl + "/" + urlName + params);
            FormBody body = formbody.build();
            LogUtils.logNetwork(body.contentType().toString());
            Request req = new Request.Builder().url(ConfigProperties.baseUrl + "/" + urlName).post(body).build();
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtils.logNetwork("onFailure" + e.getMessage());
                    if (handler == null) {
                        return;
                    }
                    Message msg = new Message();
                    msg.obj = null;
                    msg.what = flag;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    LogUtils.logNetwork("onResponse:" + res);
                    if (handler == null) {
                        return;
                    }
                    Message msg = new Message();
                    msg.obj = res;
                    if (t != null) {
                        try {
                            msg.obj = new Gson().fromJson(res, t);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    msg.what = flag;
                    handler.sendMessage(msg);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doPost(String urlName, HashMap<String, Object> params, final Handler handler, final Class t) {
        doPost(urlName, params, handler, 1, t);
    }

    public static void doUpdLoadFile(String urlName, HashMap<String, Object> params, final Handler handler, final Class t, File[] filePathArray) {
        if (params == null) {
            params = new HashMap<>();
        }

        try {
            OkHttpClient client = new OkHttpClient();
            StringBuffer sb = new StringBuffer("?");
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            if (params != null) {
                for (String key : params.keySet()) {
                    sb.append(key).append("=").append(params.get(key));
                    sb.append("&");
                    builder.addFormDataPart(key, params.get(key) + "");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            if (filePathArray != null) {
                for (File file : filePathArray) {
                    builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_IMG, file));
                }
            }
            LogUtils.logNetwork(ConfigProperties.baseUrl + "/" + urlName + sb.toString());
            Request req = new Request.Builder().url(ConfigProperties.baseUrl + "/" + urlName).post(builder.build()).build();
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (handler == null) {
                        return;
                    }
                    Message msg = new Message();
                    msg.obj = null;
                    msg.what = -1;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    LogUtils.logNetwork("上传文件：" + res);
                    if (handler == null) {
                        return;
                    }
                    Message msg = new Message();
                    if (t == null) {
                        msg.obj = res;
                    } else {
                        msg.obj = new Gson().fromJson(res, t);
                    }
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
