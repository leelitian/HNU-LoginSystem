package com.example.parkingsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText text_username;
    private EditText text_teleno;
    private EditText text_code;
    private EditText text_new;                            //密码编辑
    private EditText text_repeat;                       //密码编辑
    private Button button_code;
    private Button button_sure;                       //确定按钮
    private Button button_cancel;                     //取消按钮
    private CheckBox checkBox_display;
    private String send_url = "http://111.229.63.204:8080/SendCodeServlet";
    private String modify_url = "http://111.229.63.204:8080/CheckCodeServlet";
    private SharedPreferences saved_information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        saved_information = getSharedPreferences("user", Context.MODE_PRIVATE);

        text_username = findViewById(R.id.reset_edit_username);
        text_teleno = findViewById(R.id.reset_edit_teleno);
        text_code = findViewById(R.id.reset_edit_code);
        text_new = findViewById(R.id.reset_edit_new);
        text_repeat = findViewById(R.id.reset_edit_repeat);

        button_code = findViewById(R.id.reset_button_code);
        button_sure = findViewById(R.id.reset_button_sure);
        button_cancel = findViewById(R.id.reset_button_cancel);
        checkBox_display = findViewById(R.id.reset_checkbox_display);

        button_sure.setOnClickListener(this);      //注册界面两个按钮的监听事件
        button_cancel.setOnClickListener(this);
        checkBox_display.setOnClickListener(this);
        button_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_button_sure:                       //确认按钮的监听事件
                reset();
                break;
            case R.id.reset_button_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
//                startActivity(new Intent(ModifyActivity.this, WelcomeActivity.class));
                finish();
                break;
            case R.id.reset_checkbox_display:
                if (checkBox_display.isChecked()) {
                    text_new.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    text_repeat.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    text_new.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    text_repeat.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.reset_button_code:
                send_code();
                break;
        }
    }


    public void reset() {

        if (!check()) return;

        String new_pass = text_new.getText().toString();
        String username = text_username.getText().toString();
        String code = text_code.getText().toString();
        RequestParams params = new RequestParams(); // 绑定参数
        params.put("username", username);
        params.put("code", code);
        params.put("password", new_pass);
        params.put("client", "Android");

        //登陆请求的网址接口
        HttpUtil.get(modify_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("state")) {
                        startActivity(new Intent(ResetActivity.this, LoginActivity.class));
                        finish();
                        SharedPreferences.Editor editor = saved_information.edit();
                        editor.putBoolean("login_state", false);
                        editor.putBoolean("remember_password", false);
                        editor.apply();
                        Toast.makeText(ResetActivity.this, "重置密码成功，请重新登陆", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(ResetActivity.this, "验证码输入错误", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ResetActivity.this, "网络连接超时...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean check() {

        String new_pass = text_new.getText().toString();
        String repeat_pass = text_repeat.getText().toString();

        if (!new_pass.equals(repeat_pass)) {           //两次密码输入不一样
            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (new_pass.length() < 6 || new_pass.length() > 12) {
            Toast.makeText(ResetActivity.this, "密码长度需6-12位", Toast.LENGTH_LONG).show();
            return false;
        }

        String pattern6 = "[a-zA-Z\\d_]*";
        boolean match6 = Pattern.matches(pattern6, new_pass);
        if (!match6) {
            Toast.makeText(ResetActivity.this, "密码应由英文字母数字和_组成", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void send_code() {

        final String username = text_username.getText().toString();
        String teleno = text_teleno.getText().toString();

        RequestParams params = new RequestParams(); // 绑定参数
        params.put("username", username);
        params.put("teleno", teleno);

        if (username.isEmpty()) {
            Toast.makeText(ResetActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (teleno.isEmpty()) {
            Toast.makeText(ResetActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        params.put("client", "Android");

        //登陆请求的网址接口
        HttpUtil.get(send_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String msg = "出了啥子问题";
                try {
                    msg = response.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(ResetActivity.this, msg, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ResetActivity.this, "网络连接超时...", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
