package com.example.parkingsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText text_username;
    private EditText text_name;
    private EditText text_age;
    private EditText text_teleno;
    private EditText text_pass;
    private EditText text_pass2;
    private Button button_ok;
    private Button button_cancel;
    private SharedPreferences saved_information;
    private String register_url = "http://111.229.63.204:8080/RegisterServlet";
//    private String list_url = "http://111.229.125.198:8080/ParkingSystem/ListServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        text_username = findViewById(R.id.register_edit_username);
        text_name = findViewById(R.id.register_edit_name);
        text_age = findViewById(R.id.register_edit_age);
        text_teleno = findViewById(R.id.register_edit_teleno);
        text_pass = findViewById(R.id.register_edit_pass);
        text_pass2 = findViewById(R.id.register_edit_pass_confirm);
        button_ok = findViewById(R.id.register_btn_sure);
        button_cancel = findViewById(R.id.register_btn_cancel);
        saved_information = getSharedPreferences("user", Context.MODE_PRIVATE);

        button_ok.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn_sure:
                register();
                break;
            case R.id.register_btn_cancel:
                //切换Register Activity至Login Activity
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    public void register() {

        if (!checkInformation()) return;

        final String username = text_username.getText().toString();
        final String password = text_pass.getText().toString();
        final String name = text_name.getText().toString();
        final String age = text_age.getText().toString();
        final String teleno = text_teleno.getText().toString();

        RequestParams params = new RequestParams(); // 绑定参数
        params.put("username", username);
        params.put("name", name);
        params.put("password", password);
        params.put("age", age);
        params.put("teleno", teleno);
        params.put("client", "Android");

        HttpUtil.get(register_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("state")) {
                        //切换Login Activity至User Activity
                        SharedPreferences.Editor editor = saved_information.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putBoolean("remember_username", true);
                        editor.putBoolean("remember_password", false);
                        editor.putBoolean("login_state", true);
                        editor.putString("old_name", name);
                        editor.putString("old_age", age);
                        editor.putString("old_teleno", teleno);
                        editor.apply();

                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, WelcomeActivity.class));
                        finish();
                    } else
                        Toast.makeText(RegisterActivity.this, "用户名已存在..", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(RegisterActivity.this, "网络连接失败...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean checkInformation() {
        String username = text_username.getText().toString();
        String name = text_name.getText().toString();
        String age = text_age.getText().toString();
        String teleno = text_teleno.getText().toString();
        String pass1 = text_pass.getText().toString();
        String pass2 = text_pass2.getText().toString();

        //检查username
        if (username.length() < 5 || username.length() > 10) {
            Toast.makeText(RegisterActivity.this, "用户名长度需5-10位", Toast.LENGTH_LONG).show();
            return false;
        }
        String pattern1 = "^[a-zA-Z][a-zA-Z\\d_]*";
        String pattern2 = ".*[A-Z].*";
        boolean match1 = Pattern.matches(pattern1, username);
        boolean match2 = Pattern.matches(pattern2, username);
        if (!match1) {
            Toast.makeText(RegisterActivity.this, "用户名需要以英文字母开头，由英文字母数字和_组成", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!match2) {
            Toast.makeText(RegisterActivity.this, "用户名必须包含至少一个大写字母", Toast.LENGTH_LONG).show();
            return false;
        }

        //检查姓名
        if (name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "姓名为空", Toast.LENGTH_LONG).show();
            return false;
        }

        if (name.length() > 15) {
            Toast.makeText(RegisterActivity.this, "姓名太长了哦", Toast.LENGTH_LONG).show();
            return false;
        }

        //检查age
        String pattern4 = "^([1-9]\\d|\\d)$";
        boolean match4 = Pattern.matches(pattern4, age);
        if (!age.isEmpty() && !match4) {
            Toast.makeText(RegisterActivity.this, "年龄只能为0-99的整数", Toast.LENGTH_LONG).show();
            return false;
        }

        //检查电话
        String pattern5 = "^((13[0-9])|(14[4-9])|(15[^4])|(16[6-7])|(17[^9])|(18[0-9])|(19[1|8|9]))\\d{8}$";
        boolean match5 = Pattern.matches(pattern5, teleno);
        if (!teleno.isEmpty() && !match5) {
            Toast.makeText(RegisterActivity.this, "请输入正确的电话号码", Toast.LENGTH_LONG).show();
            return false;
        }

        //检查密码
        if (!pass1.equals(pass2)) {
            Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
            return false;
        }

        if (pass1.length() < 6 || pass2.length() > 12 || pass1.length() > 12 || pass2.length() < 6) {
            Toast.makeText(RegisterActivity.this, "密码长度需6-12位", Toast.LENGTH_LONG).show();
            return false;
        }

        String pattern6 = "[a-zA-Z\\d_]*";
        boolean match6 = Pattern.matches(pattern6, pass1);
        if (!match6) {
            Toast.makeText(RegisterActivity.this, "密码应由英文字母数字和_组成", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    public void list() {
//        String username = saved_information.getString("username", "");
//
//        RequestParams params = new RequestParams(); // 绑定参数
//        params.put("username", username);
//        params.put("client", "Android");
//
//        HttpUtil.get(list_url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                try {
//                    SharedPreferences.Editor editor = saved_information.edit();
//                    editor.putString("old_name", response.getString("name"));
//                    editor.putString("old_age", response.getString("age"));
//                    editor.putString("old_teleno", response.getString("teleno"));
//                    editor.apply();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Toast.makeText(RegisterActivity.this, "请检查您的网络连接...", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
