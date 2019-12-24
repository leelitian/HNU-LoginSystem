package com.example.parkingsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText text_username;                        //用户名编辑
    private EditText text_password;                            //密码编辑
    private Button button_register;                   //注册按钮
    private Button button_login;                      //登录按钮
    private Button button_forget;

    private CheckBox checkbox_remember_username;                  //记住用户名
    private CheckBox checkbox_remember_password;                  //记住用户名

    private SharedPreferences saved_information;

    private String login_url = "http://111.229.125.198/LoginServlet";
    private String list_url = "http://111.229.125.198/ListServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //通过id找到相应的控件
        text_username = findViewById(R.id.login_edit_account);
        text_password = findViewById(R.id.login_edit_pwd);
        button_register = findViewById(R.id.login_btn_register);
        button_login = findViewById(R.id.login_btn_login);
        button_forget = findViewById(R.id.login_btn_forget);
        checkbox_remember_username = findViewById(R.id.login_remember_username);
        checkbox_remember_password = findViewById(R.id.login_remember_password);

        //采用OnClickListener方法设置不同按钮按下之后的监听事件
        button_login.setOnClickListener(this);
        button_register.setOnClickListener(this);
        button_forget.setOnClickListener(this);
        checkbox_remember_password.setOnClickListener(this);
        checkbox_remember_username.setOnClickListener(this);

        saved_information = getSharedPreferences("user", Context.MODE_PRIVATE);

        String name = saved_information.getString("username", "");
        String pass = saved_information.getString("password", "");
        boolean remember_username = saved_information.getBoolean("remember_username", false);
        boolean remember_password = saved_information.getBoolean("remember_password", false);

        if (remember_username) {
            text_username.setText(name);
            checkbox_remember_username.setChecked(true);
        }

        if (remember_password) {
            text_password.setText(pass);
            checkbox_remember_password.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                login();
                break;
            case R.id.login_btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.login_remember_password:
                if (checkbox_remember_password.isChecked())
                    checkbox_remember_username.setChecked(true);
                break;
            case R.id.login_remember_username:
                if (!checkbox_remember_username.isChecked())
                    checkbox_remember_password.setChecked(false);
                break;
            case R.id.login_btn_forget:
                startActivity(new Intent(this, ResetActivity.class));
                break;
        }
    }

    public void login() {                                                    //登录按钮监听事件

        if (!login_check()) return;

        final String username = text_username.getText().toString();               //获取当前输入的用户名和密码信息
        final String password = text_password.getText().toString();

        RequestParams params = new RequestParams(); // 绑定参数
        params.put("user", username);
        params.put("pwd", password);
        params.put("client", "Android");

        //登陆请求的网址接口
        HttpUtil.get(login_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("state")) {
                        //记住用户
                        SharedPreferences.Editor editor = saved_information.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putBoolean("remember_username", checkbox_remember_username.isChecked());
                        editor.putBoolean("remember_password", checkbox_remember_password.isChecked());
                        editor.putBoolean("login_state", true);
                        editor.apply();

                        list();
                        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                        finish();
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(LoginActivity.this, "网络连接超时...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean login_check() {
        String username = text_username.getText().toString();               //获取当前输入的用户名和密码信息
        String password = text_password.getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void list() {
        String username = saved_information.getString("username", "");

        RequestParams params = new RequestParams(); // 绑定参数
        params.put("username", username);
        params.put("client", "Android");

        HttpUtil.get(list_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    SharedPreferences.Editor editor = saved_information.edit();
                    editor.putString("old_name", response.getString("name"));
                    editor.putString("old_age", response.getString("age"));
                    editor.putString("old_teleno", response.getString("teleno"));
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(LoginActivity.this, "请检查您的网络连接...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
