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

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText text_name;
    private EditText text_age;
    private EditText text_teleno;
    private Button button_submit;
    private Button button_cancel;

    private SharedPreferences saved_information;

    private String old_name;
    private String old_age;
    private String old_teleno;

    private String edit_url = "http://111.229.63.204/EditServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        saved_information = getSharedPreferences("user", Context.MODE_PRIVATE);

        text_name = findViewById(R.id.edit_text_name);
        text_age = findViewById(R.id.edit_text_age);
        text_teleno = findViewById(R.id.edit_text_teleno);
        button_submit = findViewById(R.id.edit_button_submit);
        button_cancel = findViewById(R.id.edit_button_cancel);

        button_submit.setOnClickListener(this);
        button_cancel.setOnClickListener(this);

        old_name = saved_information.getString("old_name", "");
        old_age = saved_information.getString("old_age", "");
        old_teleno = saved_information.getString("old_teleno", "");

        text_name.setText(old_name);
        text_age.setText(old_age);
        text_teleno.setText(old_teleno);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_button_submit:
                edit();
                break;
            case R.id.edit_button_cancel:
                finish();
                break;
        }
    }

    public void edit() {
        if (!checkInformation()) return;

        final String name = text_name.getText().toString();
        final String age = text_age.getText().toString();
        final String teleno = text_teleno.getText().toString();

        System.out.println(old_name);
        System.out.println(name);

        if (name.equals(old_name) && age.equals(old_age) && teleno.equals(old_teleno)) {
            Toast.makeText(EditActivity.this, "没有改变任何信息", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = saved_information.getString("username", "");

        RequestParams params = new RequestParams(); // 绑定参数
        params.put("username", username);
        params.put("name", name);
        params.put("age", age);
        params.put("teleno", teleno);
        params.put("client", "Android");

        HttpUtil.get(edit_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("state")) {
                        SharedPreferences.Editor editor = saved_information.edit();
                        editor.putString("old_name", name);
                        editor.putString("old_age", age);
                        editor.putString("old_teleno", teleno);
                        editor.apply();
                        Toast.makeText(EditActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(EditActivity.this, WelcomeActivity.class));
                        finish();
                    } else
                        Toast.makeText(EditActivity.this, "修改信息失败..", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(EditActivity.this, "网络错误...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkInformation() {

        String name = text_name.getText().toString();
        String age = text_age.getText().toString();
        String teleno = text_teleno.getText().toString();

        //检查姓名
        if (name.isEmpty()) {
            Toast.makeText(EditActivity.this, "姓名为空", Toast.LENGTH_LONG).show();
            return false;
        }

        if (name.length() > 15) {
            Toast.makeText(EditActivity.this, "姓名太长了哦", Toast.LENGTH_LONG).show();
            return false;
        }

        //检查age
        String pattern4 = "^([1-9]\\d|\\d)$";
        boolean match4 = Pattern.matches(pattern4, age);
        if (!age.isEmpty() && !match4) {
            Toast.makeText(EditActivity.this, "年龄只能为0-99的整数", Toast.LENGTH_LONG).show();
            return false;
        }

        //检查电话
        String pattern5 = "^((13[0-9])|(14[4-9])|(15[^4])|(16[6-7])|(17[^9])|(18[0-9])|(19[1|8|9]))\\d{8}$";
        boolean match5 = Pattern.matches(pattern5, teleno);
        if (!teleno.isEmpty() && !match5) {
            Toast.makeText(EditActivity.this, "请输入正确的电话号码", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}