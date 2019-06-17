package com.example.administrator.newfridge.view.activity;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.model.familymodel.FamilyBean;
import com.example.administrator.newfridge.model.familymodel.FamilyList;
import com.example.administrator.newfridge.okhttp.GetCreateFamilyRq;

import com.example.administrator.newfridge.okhttp.GetFamilyInfoRq;
import com.example.administrator.newfridge.okhttp.PostInvitation;
import com.example.administrator.newfridge.okhttp.PostOutFamilyRq;
import com.example.administrator.newfridge.tool.JsonTool;
import com.example.administrator.newfridge.tool.MyHandlerMsg;
import com.example.administrator.newfridge.view.adapter.EmptyRecyclerView;
import com.example.administrator.newfridge.view.adapter.FamilyAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.zbardemo.TestScanActivity;
import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * @author LG32
 * 我的家庭
 * @date 2019-02-01
 */
public class FamilyActivity extends AppCompatActivity {

    protected EmptyRecyclerView mRecyclerView;
    @BindView(R.id.toolbarfamily)
    Toolbar toolbarfamily;
    private View mEmptyView;
    protected FamilyAdapter mFamilyAdapter;
    protected ArrayList<FamilyBean> mList = new ArrayList<> ();
    private MyHandler myHandler = new MyHandler ();
    private static String TAG = "FamilyActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_family );
        ButterKnife.bind ( this );
        setSupportActionBar ( toolbarfamily );

        initView ();
        initData ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.menu_family, menu );//加载menu布局
        return true;
    }

    public void onViewClicked() {
        toolbarfamily.setOnMenuItemClickListener ( new Toolbar.OnMenuItemClickListener () {

            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId ()) {

                    case R.id.create_family:
                        editDialog();
                        break;

                    case R.id.join_family:
                        Intent intent = new Intent ();
                        intent.putExtra ( "order", "family" );
                        intent.setClass ( FamilyActivity.this, TestScanActivity.class );
                        startActivityForResult ( intent,1 );
                        break;

                    case R.id.exit_family:
                        deleteFamilyDialog ();
                        break;

                    case R.id.refresh_family:
                        refreshFamily();
                        break;
                }
                return true;
            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i ( TAG, "onActivityResult: 回传信息"  + requestCode);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String dataFid = data.getStringExtra ( "fid" );
                    if(!dataFid.equals ( "" )){
                        String fid = dataFid.substring ( 4, dataFid.length () );
                        Log.i ( TAG, "onActivityResult: " + fid );
                        RequestBody requestBody = new FormBody.Builder ()
                                .add ( "fid" , fid)
                                .build ();

                        new PostInvitation ( requestBody, myHandler, getCookie ());
                    }
                }
                break;
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {

        mRecyclerView = findViewById ( R.id.recyclerview );
        mEmptyView = findViewById ( R.id.empty_view );
        onViewClicked();

        FamilyList familyList = FamilyList.getFamilyList ();
        mList = familyList.getList ();

        mRecyclerView.setLayoutManager ( new LinearLayoutManager ( this, LinearLayoutManager.VERTICAL, false ) );
        GridLayoutManager gridLayoutManager = new GridLayoutManager ( this, 1 );
        mRecyclerView.setLayoutManager ( gridLayoutManager );

        mFamilyAdapter = new FamilyAdapter ( this, mList );
        mRecyclerView.setAdapter ( mFamilyAdapter );
        mRecyclerView.setmEmptyView ( mEmptyView );
        mRecyclerView.setNestedScrollingEnabled ( false );
        mRecyclerView.hideEmptyView ();
    }


    /**
     * 初始化页面数据
     */
    private void initData() {

        Log.i ( TAG, "initData: 初始化页面列表数据" );

        mFamilyAdapter.setOnMsgClickListener ( new FamilyAdapter.OnMsgClickListener () {
            @Override
            public void onClick(int position, FamilyBean familyBean) {
                Intent familyIntent = new Intent ( FamilyActivity.this, QrshowActivity.class );
                familyIntent.putExtra ( "familyName", familyBean.getName () );
                familyIntent.putExtra ( "urlParam", familyBean.getFid () );
                startActivity ( familyIntent );
            }
        } );
    }



    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler implements MyHandlerMsg {

        public void handleMessage(Message msg) {

            JsonTool jt ;

            switch (msg.what) {
                case REQUEST_FAIL:
                    Toast.makeText ( FamilyActivity.this, "网络请求失败", Toast.LENGTH_SHORT )
                            .show ();
                    break;

                case CREATEFAMILY_SUCCESS:
                    jt = new JsonTool ( msg.obj.toString (), REQUEST_SUCCESS );
                    switch (jt.judgeMsg()){
                        case "0":
                            Toast.makeText ( FamilyActivity.this, "创建失败", Toast.LENGTH_SHORT )
                                    .show ();
                            break;
                        case  "1":
                            Toast.makeText ( FamilyActivity.this, "创建成功", Toast.LENGTH_SHORT )
                                    .show ();
                            break;
                        case "2":
                            Toast.makeText ( FamilyActivity.this, "已创建过相同家庭", Toast.LENGTH_SHORT )
                                    .show ();
                            break;
                    }

                    break;

                case DELECTFAMILY_SUCCESS:
                    jt = new JsonTool ( msg.obj.toString (), REQUEST_SUCCESS );
                    switch (jt.judgeMsg()) {
                        case "0":
                            Toast.makeText ( FamilyActivity.this, "该成员为管理者", Toast.LENGTH_SHORT )
                                    .show ();
                            break;
                        case "1":
                            Toast.makeText ( FamilyActivity.this, "删除成功", Toast.LENGTH_SHORT )
                                    .show ();
                            break;
                        case "2":
                            Toast.makeText ( FamilyActivity.this, "家庭不存在", Toast.LENGTH_SHORT )
                                    .show ();
                            break;
                    }
                    break;

                case GETFAMILYINFO_SUCCESS:
                    Log.i ( TAG, "handleMessage: 网络请求成功" );
                    getFamilyInfo ( msg.obj.toString () );
                    break;

                case INVITATION_SUCCESS:
                    analysisAddRq( msg.obj.toString () );
                    break;

            }
        }
    }

    private String getCookie() {
        SharedPreferences sharedPreferences = getSharedPreferences ( "cookie",
                MODE_PRIVATE );
        return sharedPreferences.getString ( "cookie", "" );
    }

    public void editDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder ( this );
        builder.setTitle ( "创建家庭组" );
        final EditText et = new EditText ( this );
        et.setHint ( "请输入家庭名称" );
        et.setSingleLine ( true );
        builder.setView ( et );
        builder.setNegativeButton ( "取消", null );
        builder.setPositiveButton ( "确定", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = et.getText ().toString ();
                if (password.equals ( "" )) {
                    Toast.makeText ( FamilyActivity.this, "家庭组名称不能为空", Toast.LENGTH_SHORT ).show ();
                } else {
                    Log.i ( TAG, "createOnClick: " + et.getText ().toString () );
                    new GetCreateFamilyRq (myHandler, getCookie (), et.getText ().toString ());
                }
            }
        } );
        AlertDialog alertDialog = builder.create ();
        alertDialog.show ();
    }


    /**
     * 删除家庭多选提示框
     */
    public void deleteFamilyDialog(){
        final String item[] = new String [mList.size ()];
        final boolean[] bools = {false,false,false,false,false};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择");

        for (int i = 0; i < mList.size (); i++){
            item[i] = "删除家庭：" + mList.get ( i ).getName ();
        }

        builder.setMultiChoiceItems( item, bools, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                bools[which] = isChecked;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder sb = new StringBuilder ();
                for (int i = 0; i < item.length; i++) {
                    if (bools[i]) {
                        sb.append ( mList.get ( i ).getName () ).append ( " " );
                        warnDialog ( sb.toString (), i );
                    }
                }

            }
        });

        builder.setNegativeButton("取消",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /**
     * 提示是否删除家庭
     * @param warnString 选中的家庭名称
     * @param i 选中的家庭序号
     */
    private void warnDialog(String warnString, int i){
        AlertDialog.Builder dialog = new AlertDialog.Builder ( FamilyActivity.this );
        dialog.setTitle ( "删除" );
        dialog.setMessage ( "是否删除已选中的" + warnString ) ;
        dialog.setCancelable ( false );
        final int checkItem = i;

        dialog.setPositiveButton ( "确定",  new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buildRqBody (checkItem);
            }
        } );

        dialog.setNegativeButton ( "取消", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText ( FamilyActivity.this , "已取消删除家庭组", Toast.LENGTH_SHORT)
                        .show ();
            }
        } );

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void buildRqBody(int checkItem){

        Log.i ( TAG, "buildRqBody: " + mList.get ( checkItem ).getFid () );

        RequestBody requestBody = new FormBody .Builder ()
                .add ( "fid", mList.get ( checkItem ).getFid () )
                .build ();

        new PostOutFamilyRq (requestBody , myHandler, getCookie ());
    }

    private void refreshFamily() {

        new GetFamilyInfoRq ( myHandler, getCookie () );

    }

    /**
     * 得到家庭组信息
     * @param json 返回的未被解析的json格式数据
     */
    private void getFamilyInfo(String json) {

        ArrayList<FamilyBean> familyInfo;

        FamilyList familyList = FamilyList.getFamilyList ();

        Gson gson = new Gson ();

        familyInfo = gson.fromJson ( json, new TypeToken<List<FamilyBean>> () {
        }
                .getType () );

        familyList.setList ( familyInfo );

        for (FamilyBean family : familyInfo) {

            Log.i ( TAG, "getFamilyInfo: " + family.getName () );
        }
        initData ();
    }

    /**
     * 解析添加家庭组返回的json
     * @param json 返回的未被解析的json格式数据
     */
    private void analysisAddRq(String json){
        JsonObject jsonObject =  (JsonObject) new JsonParser ().parse ( json );
        String msg = jsonObject.get ( "msg" ).getAsString ();
//        String code = jsonObject.get ( "code" ).getAsString ();

        Toast.makeText ( FamilyActivity.this, msg, Toast.LENGTH_SHORT )
                .show ();
    }
}
