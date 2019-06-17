package com.example.administrator.newfridge.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.okhttp.GetIdenRequest;
import com.example.administrator.newfridge.okhttp.RegisterRequest;
import com.example.administrator.newfridge.tool.JsonTool;
import com.example.administrator.newfridge.tool.MyHandlerMsg;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * @author LG32
 * 注册功能
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText telephone;
    //private EditText set_id_code;
    private EditText password;
    private EditText ill;
    private EditText confirm_password;
    private EditText username;

    //private Button get_id_code;
    private String sex_str = "女性";
    private int btnTime = 60;
    private RegisterHandler handler = new RegisterHandler ();

    private Map<String, String> information = new HashMap<> ();

    private static final int INFORMATION_OK = 0;//输入的信息ok
    private static final int INFORMATION_NULL = 1;//输入的信息有空的
    private static final int PASSWORD_MISS = 2;//输入的密码有误（两次输入不相同）
    private static final String TAG = "注册页面";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_register );
        initView ();
    }

    private void initView() {
        Toolbar toolbar = findViewById ( R.id.toolbar );
        toolbar.setTitle("注册");
        telephone = findViewById ( R.id.telephone );
        //set_id_code = findViewById ( R.id.set_id_code );
        password = findViewById ( R.id.password );
        confirm_password = findViewById ( R.id.confirm_password );
        ill = findViewById ( R.id.ill );
        username = findViewById ( R.id.username );
        RadioGroup sex = findViewById ( R.id.sex );
        //get_id_code = findViewById ( R.id.get_id_code );
        Button finish = findViewById ( R.id.finish );

        setSupportActionBar ( toolbar );
        //get_id_code.setOnClickListener ( this );
        finish.setOnClickListener ( this );

        sex.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        sex_str = "男性";
                        break;
                    case R.id.female:
                        sex_str = "女性";
                        break;
                }
            }
        } );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()) {
            case R.id.finish:
               /* getEditTextInfo ();
                switch (judgeInformation ( information )) {
                    case INFORMATION_NULL:
                        Toast.makeText ( this, "请输入完整的信息", Toast.LENGTH_SHORT ).show ();
                        break;
                    case PASSWORD_MISS:
                        Toast.makeText ( this, "两次输入的密码不相同", Toast.LENGTH_SHORT ).show ();
                        break;
                    case INFORMATION_OK:
                        Log.i ( TAG, "信息正确" );
                        new RegisterRequest ( requestBodyBuild (), handler );
                        break;
                }*/
               if (telephone.getText().toString().length()!=0 && password.getText().toString().length()!=0 && confirm_password.getText().toString().length()!=0) {
                   Intent intent = new Intent();
                   intent.setClass(RegisterActivity.this, LoginActivity.class);
                   startActivity(intent);
                   Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                   finish();
               }else{
                   Toast.makeText(this, "请正确输入信息", Toast.LENGTH_SHORT).show();
               }
                break;

           /* case R.id.get_id_code:
                String tel = telephone.getText ().toString ();
                if (tel.length () == 11) {
                    RequestBody requestBody = new FormBody.Builder ()
                            .add ( "tel", tel )
                            .build ();
                    new GetIdenRequest ( requestBody, handler );
                } else {
                    Toast.makeText ( this, "请输入正确的手机号码", Toast.LENGTH_SHORT ).show ();
                }
                break;*/
        }
    }

    private RequestBody requestBodyBuild() {
        return new FormBody.Builder ()
                .add ( "tel", information.get ( "telephone" ) )
                .add ( "password", information.get ( "password" ) )
                .add ( "iden", information.get ( "set_id_code" ) )
                .add ( "message", information.get ( "ill" )
                        + information.get ( "sex" ) )
                .add ( "username", information.get ( "username" ) )
                .build ();
    }

    private void getEditTextInfo() {
        information.put ( "telephone", telephone.getText ().toString () );
        //information.put ( "set_id_code", set_id_code.getText ().toString () );
        information.put ( "password", password.getText ().toString () );
        information.put ( "confirm_password", confirm_password.getText ().toString () );
        information.put ( "ill", ill.getText ().toString () );
        information.put ( "username", username.getText ().toString () );
        information.put ( "sex", sex_str );
    }

    /*private int judgeInformation(Map<String, String> information) {
        for (Map.Entry<String, String> entry : information.entrySet ()) {
               return INFORMATION_NULL;
            }
        }
        if (information.get ( "password" ).equals ( information.get ( "confirm_password" ) )) {
            return INFORMATION_OK;
        } else {
            return PASSWORD_MISS;
        }
    }*/


    @SuppressLint("HandlerLeak")
    public class RegisterHandler extends Handler implements MyHandlerMsg {

        JsonTool jsonTool;

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_FAIL:
                    Toast.makeText ( RegisterActivity.this, "网络请求失败", Toast.LENGTH_SHORT )
                            .show ();
                    break;

                case REGISTER_SUCCESS:
                    jsonTool = new JsonTool ( msg.obj.toString (), REQUEST_SUCCESS );
                    resultJudge ( jsonTool.judgeMsg () );
                    break;

                case GETIDEN_SUCCESS:
                    jsonTool = new JsonTool ( msg.obj.toString (), REQUEST_SUCCESS );
                    //idenJudge ( jsonTool.judgeMsg () );
                    break;
            }
        }
    }

    /**
     * 判断是否注册成功
     *
     * @param code 服务器返回的code
     */
    private void resultJudge(String code) {
        Log.i ( "loginJudge: ", code );
        switch (code) {
            case "0":
                Toast.makeText ( RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT )
                        .show ();
                break;

            case "1":
                Toast.makeText ( RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT )
                        .show ();
                Intent loginIntent = new Intent ();
                loginIntent.setClass ( RegisterActivity.this, LoginActivity.class );
                startActivity ( loginIntent );
                finish ();
                break;

            case "2":
                Toast.makeText ( RegisterActivity.this, "验证码失效", Toast.LENGTH_SHORT )
                        .show ();
                break;

            case "3":
                Toast.makeText ( RegisterActivity.this, "手机号已被注册", Toast.LENGTH_SHORT )
                        .show ();
                break;
        }
    }

    /*private void idenJudge(String code) {
        switch (code) {
            case "0":
                Toast.makeText ( this, "验证码发送失败", Toast.LENGTH_SHORT ).show ();
                break;

            case "1":
                Toast.makeText ( RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT )
                        .show ();
                lockIdenBtn ();
                break;

            case "404":
                Toast.makeText ( RegisterActivity.this, "24小时内获取验证码次数过多", Toast.LENGTH_SHORT )
                        .show ();
                break;
        }
    }*/

    /*@SuppressLint({"SetTextI18n", "CheckResult"})
    private void lockIdenBtn() {

        get_id_code.setEnabled ( false );
        get_id_code.setBackgroundColor ( R.drawable.btn_enabled_false );
        get_id_code.setText ( btnTime + " s后" );

        Observable.timer ( 1, TimeUnit.SECONDS )
                .repeat ()
                .observeOn ( AndroidSchedulers.mainThread () )
                .subscribe ( new Observer<Long> () {

                    private Disposable mDisposable;


                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (btnTime > 0) {
                            get_id_code.setText ( btnTime + " s后" );
                            btnTime--;
                        }
                        if (btnTime == 0) {
                            Log.i ( TAG, "onNext: timer finish" );
                            get_id_code.setText ( "获取验证码" );
                            get_id_code.setEnabled ( true );
                            get_id_code.setBackgroundColor ( R.drawable.buttom_enabled_true );

                            closeTimer ();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        closeTimer ();
                    }

                    @Override
                    public void onComplete() {}

                    private void closeTimer() {
                        if (mDisposable != null) {
                            mDisposable.dispose ();
                            btnTime = 60;
                        }
                    }
                } );*/
    }
