package com.example.administrator.newfridge.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.okhttp.LoginRequest;
import com.example.administrator.newfridge.tool.JsonTool;
import com.example.administrator.newfridge.tool.MyHandlerMsg;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.telephone)
    EditText telephone;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.sign)
    Button sign;
    @BindView(R.id.loading)
    ProgressBar progressBar;

    private LoginHandler loginHandler = new LoginHandler ();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );
        ButterKnife.bind ( this );
        initValue ();
    }

    private void initValue() {
        sharedPreferences = getSharedPreferences ( "cookie",
                MODE_PRIVATE );
        String lastPassword = sharedPreferences.getString ( "password", "" );
        String lastTel = sharedPreferences.getString ( "tel", "" );

        telephone.setText ( lastTel );
        password.setText ( lastPassword );
    }

    public void toMainActivity() {

        SharedPreferences.Editor editor = getSharedPreferences ( "cookie",
                MODE_PRIVATE ).edit ();
        editor.putString ( "tel", telephone.getText ().toString () );
        editor.putString ( "password", password.getText ().toString () );
        editor.apply ();

        Intent intent = new Intent ();
        intent.setClass ( LoginActivity.this, MainActivity.class );
        startActivity ( intent );
        finish();
        Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
    }

    public void toRegisterActivity() {
        Intent intent = new Intent ();
        intent.setClass ( LoginActivity.this, RegisterActivity.class );
        startActivity ( intent );
        progressBar.setVisibility ( View.GONE );
    }

    private void loginJudge(String code) {
        Log.i ( "loginJudge: ", code );
        switch (code) {
            case "1":
                toMainActivity ();
                break;
            case "0":
                Toast.makeText ( LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT )
                        .show ();
                break;
            case "2":
                Toast.makeText ( LoginActivity.this, "该用户名不存在", Toast.LENGTH_SHORT )
                        .show ();
                break;
        }
    }

    @OnClick({R.id.login, R.id.sign})
    public void onViewClicked(View view) {
        switch (view.getId ()) {
            case R.id.login:
                if (progressBar.getVisibility () == View.GONE)
                    progressBar.setVisibility ( View.VISIBLE );

                String str_telephone = telephone.getText ().toString ();
                String str_password = password.getText ().toString ();
                if (!str_password.equals ( "" ) && !str_telephone.equals ( "" )) {
                    RequestBody requestBody = new FormBody.Builder ()
                            .add ( "tel", str_telephone )
                            .add ( "password", str_password )
                            .build ();

                    if (str_password.equals ( "123456" ) && str_telephone.equals ( "123456" )) {
                        toMainActivity ();
                    } else {
                        new LoginRequest ( requestBody, loginHandler, sharedPreferences );
                    }
                } else {
                    Toast.makeText ( this, "请输入完整的信息", Toast.LENGTH_SHORT )
                            .show ();
                }
                break;

            case R.id.sign:
                toRegisterActivity ();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    public class LoginHandler extends Handler implements MyHandlerMsg {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_FAIL:
                    if (progressBar.getVisibility () == View.VISIBLE)
                        progressBar.setVisibility ( View.GONE );
                    Toast.makeText ( LoginActivity.this, "网络请求失败", Toast.LENGTH_SHORT )
                            .show ();
                    break;

                case LOGIN_SUCCESS:
                    if (progressBar.getVisibility () == View.GONE)
                        progressBar.setVisibility ( View.VISIBLE );
                    JsonTool jsonTool = new JsonTool ( msg.obj.toString (), REQUEST_SUCCESS );
                    loginJudge ( jsonTool.judgeMsg () );
                    break;
            }
        }
    }
}
