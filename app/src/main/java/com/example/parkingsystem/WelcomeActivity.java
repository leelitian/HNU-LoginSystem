package com.example.parkingsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_cancel;
    private Button button_edit;
    private Button button_modify;
    private TextView text_welcome;

    private SharedPreferences saved_information;

    private String login_url = "http://111.229.63.204:8080/LoginServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        saved_information = getSharedPreferences("user", Context.MODE_PRIVATE);

        text_welcome = (TextView) findViewById(R.id.welcome_text);
        button_cancel = (Button) findViewById(R.id.welcome_btn_return);
        button_edit = (Button) findViewById(R.id.welcome_btn_edit);
        button_modify = (Button) findViewById(R.id.welcome_btn_modify);

        button_cancel.setOnClickListener(this);
        button_modify.setOnClickListener(this);
        button_edit.setOnClickListener(this);

        saved_information = getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = saved_information.getString("username", "");
        text_welcome.setText("欢迎你，亲爱的" + username);

        login_check();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.welcome_btn_return:
                startActivity(new Intent(this, LoginActivity.class));
                SharedPreferences.Editor editor = saved_information.edit();
                editor.putBoolean("login_state", false);
                editor.apply();
                Toast.makeText(WelcomeActivity.this, "退出成功！", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.welcome_btn_edit:
                startActivity(new Intent(this, EditActivity.class));
                break;

            case R.id.welcome_btn_modify:
                startActivity(new Intent(this, ModifyActivity.class));
                break;
        }
    }

    public void login_check() {                                                    //登录按钮监听事件

        final String username = saved_information.getString("username", "");
        final String password = saved_information.getString("password", "");

        RequestParams params = new RequestParams(); // 绑定参数
        params.put("user", username);
        params.put("pwd", password);
        params.put("client", "Android");

        //登陆请求的网址接口
        HttpUtil.get(login_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (!response.getBoolean("state")) {
                        Toast.makeText(WelcomeActivity.this, "密码错误，请重新登录", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                        SharedPreferences.Editor editor = saved_information.edit();
                        editor.putBoolean("remember_password", false);
                        editor.apply();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(WelcomeActivity.this, "网络连接超时，请重新登录", Toast.LENGTH_SHORT).show();
                //切换Welcome Activity至login Activity
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            }
        });
    }


}
