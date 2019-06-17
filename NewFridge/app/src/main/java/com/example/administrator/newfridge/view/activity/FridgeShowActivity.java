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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.newfridge.R;
import com.example.administrator.newfridge.model.IceBox.IceBoxIdBean;
import com.example.administrator.newfridge.model.IceBox.IceBoxInfo;
import com.example.administrator.newfridge.model.familymodel.FamilyBean;
import com.example.administrator.newfridge.model.familymodel.FamilyList;
import com.example.administrator.newfridge.okhttp.GetBoxIdRequest;
import com.example.administrator.newfridge.okhttp.PostConnectBoxRq;
import com.example.administrator.newfridge.okhttp.PostCreateIceBoxRq;
import com.example.administrator.newfridge.okhttp.PostDelectIceB;
import com.example.administrator.newfridge.tool.JsonTool;
import com.example.administrator.newfridge.tool.MyHandlerMsg;
import com.example.administrator.newfridge.view.adapter.EmptyRecyclerView;
import com.example.administrator.newfridge.view.adapter.FridgeAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import cn.bingoogolapple.qrcode.zbardemo.TestScanActivity;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * @author RollingZ
 * 管理冰箱
 */
public class FridgeShowActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionMenu mActionMenu;
    protected EmptyRecyclerView mRecyclerView;
    private View mEmptyView;
    private static String TAG = "FridgeShowActivity";
    private List<IceBoxInfo> mList;
    private String fakeIceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_fridgeshow );
        Toolbar toolbar = findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        initView ();
        initData ();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i ( TAG, "onActivityResult: 回传信息"  + requestCode);
        switch (requestCode){
            case 2:
                if(resultCode == RESULT_OK){
                    String dataMacId = data.getStringExtra ( "macip" );
                    if(!dataMacId.equals ( "" )){
                        String macId = dataMacId.substring ( 6, dataMacId.length () );
                        Log.i ( TAG, "onActivityResult: " + macId );
                        connectBoxRq ( macId, fakeIceId );
                    }
                }
                break;
        }
    }

    private void initView() {
        FloatingActionButton fridgeadd = findViewById ( R.id.fridgeaddAction );
        FloatingActionButton fridagedelect = findViewById ( R.id.fridgeDelectAction );
        mActionMenu = findViewById ( R.id.fridgeactionmenu );
        mRecyclerView = findViewById ( R.id.recyclerview );
        mEmptyView = findViewById ( R.id.empty_view );

        mActionMenu.setVisibility ( View.VISIBLE );
        fridgeadd.setOnClickListener ( this );
        fridagedelect.setOnClickListener ( this );
    }

    private void initData() {

        Log.i ( TAG, "initData: 初始化页面数据" );

        IceBoxIdBean iceBoxIdBean = IceBoxIdBean.getIceBoxIdBean ();
        mList = iceBoxIdBean.getIceId_list ();

        mRecyclerView.setLayoutManager ( new LinearLayoutManager ( this, LinearLayoutManager.VERTICAL, false ) );
        GridLayoutManager gridLayoutManager = new GridLayoutManager ( this, 1 );
        mRecyclerView.setLayoutManager ( gridLayoutManager );
        FridgeAdapter fridgeAdapter = new FridgeAdapter ( this, mList );

        fridgeAdapter.setOnBaseClickListener ( new FridgeAdapter.OnFridgeClickListener () {
            @Override
            public void onClick(int position, List mList) {

            }
        } );


        mRecyclerView.setAdapter ( fridgeAdapter );
        mRecyclerView.setmEmptyView ( mEmptyView );
        mRecyclerView.setNestedScrollingEnabled ( false );
        mRecyclerView.hideEmptyView ();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()) {
            case R.id.fridgeaddAction:
                mActionMenu.close ( true );
                editDialog ();
                break;
            case R.id.fridgeDelectAction:
                mActionMenu.close ( true );
                deleteIceBoxDialog ();
                break;
        }
    }


    /**
     * 新建冰箱文本输入框
     */
    public void editDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder ( this );
        builder.setTitle ( "创建新冰箱" );
        final EditText et = new EditText ( this );
        et.setHint ( "请输入新冰箱名称" );
        et.setSingleLine ( true );
        builder.setView ( et );
        builder.setNegativeButton ( "取消", null );
        builder.setPositiveButton ( "确定", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nickname = et.getText ().toString ();
                if (nickname.equals ( "" )) {
                    Toast.makeText ( FridgeShowActivity.this, "冰箱名称不能为空", Toast.LENGTH_SHORT ).show ();
                } else {
                    createIceRq ( nickname );
                }
            }
        } );
        AlertDialog alertDialog = builder.create ();
        alertDialog.show ();
    }

    private String getCookie() {
        SharedPreferences sharedPreferences = getSharedPreferences ( "cookie",
                MODE_PRIVATE );
        return sharedPreferences.getString ( "cookie", "" );
    }

    /**
     * 创建手机虚拟冰箱
     * @param nickname 用户对冰箱起的名字
     */
    private void createIceRq(String nickname) {

        FamilyList familyList = FamilyList.getFamilyList ();
        List<FamilyBean> list = familyList.getList ();
        String fid = list.get ( 0 ).getFid ();
        MyHandler myHandler = new MyHandler ();

        Log.i ( TAG, "createIceRq: " + nickname );

        RequestBody requestBody = new FormBody.Builder ()
                .add ( "nickName", nickname )
                .add ( "fid", fid )
                .build ();

        new PostCreateIceBoxRq ( requestBody, myHandler, getCookie () );

    }

    /**
     * 将手机的虚拟冰箱关联实体冰箱
     * @param boxId 实体冰箱id，通过扫描二维码获得
     * @param fakeId 虚拟冰箱id
     */
    private void connectBoxRq(String boxId, String fakeId) {

        MyHandler myHandler = new MyHandler ();

        Log.i ( TAG, "connectBoxRq: fakeId" + fakeId );
        Log.i ( TAG, "connectBoxRq: boxId"  + boxId );

        RequestBody requestBody = new FormBody.Builder ()
                .add ( "iceId0", fakeId )
                .add ( "iceId1", boxId )
                .build ();

        new PostConnectBoxRq ( requestBody, myHandler, getCookie () );
    }

    /**
     * 删除冰箱多选提示框
     */
    public void deleteIceBoxDialog() {
        final String item[] = new String[mList.size ()];
        final boolean[] bools = new boolean[mList.size ()];
        AlertDialog.Builder builder = new AlertDialog.Builder ( this );
        builder.setTitle ( "请选择" );

        for (int i = 0; i < mList.size (); i++) {
            item[i] = "删除冰箱：" + mList.get ( i ).getIceName ();
        }

        builder.setMultiChoiceItems ( item, bools, new DialogInterface.OnMultiChoiceClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                bools[which] = isChecked;
            }
        } );

        builder.setPositiveButton ( "确定", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder sb = new StringBuilder ();
                for (int i = 0; i < item.length; i++) {
                    if (bools[i]) {
                        sb.append ( mList.get ( i ).getIceName () ).append ( " " );
                    }
                }
                warnDialog ( sb.toString (), bools );
            }
        } );

        builder.setNegativeButton ( "取消", null );
        AlertDialog alertDialog = builder.create ();
        alertDialog.show ();
    }


    /**
     * 提示是否删除冰箱
     *
     * @param warnString 选中的冰箱名称
     * @param bools      记录被选中冰箱的index；
     */
    private void warnDialog(String warnString, final boolean bools[]) {
        AlertDialog.Builder dialog = new AlertDialog.Builder ( FridgeShowActivity.this );
        dialog.setTitle ( "删除" );
        dialog.setMessage ( "是否删除已选中的" + warnString );
        dialog.setCancelable ( false );

        dialog.setPositiveButton ( "确定", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < bools.length; i++) {
                    if (bools[i]) {
                        String iceId = mList.get ( i ).getIceId ();
                        Log.i ( TAG, "onClick: " + iceId );
                        RequestBody requestBody = new FormBody.Builder ()
                                .add ( "iceId", iceId )
                                .build ();

                        new PostDelectIceB ( requestBody, new MyHandler (), getCookie () );
                    }
                }
            }
        } );

        dialog.setNegativeButton ( "取消", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText ( FridgeShowActivity.this, "已取消删除冰箱", Toast.LENGTH_SHORT )
                        .show ();
            }
        } );

        AlertDialog alertDialog = dialog.create ();
        alertDialog.show ();
    }

    /**
     * 提示是否关联冰箱
     */
    private void connectBoxDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder ( FridgeShowActivity.this );
        dialog.setTitle ( "提示" );
        dialog.setMessage ( "是否将虚拟冰箱与实体冰箱关联？" );
        dialog.setCancelable ( false );

        dialog.setPositiveButton ( "确定", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent ();
                intent.putExtra ( "order", "box" );
                intent.setClass ( FridgeShowActivity.this, TestScanActivity.class );
                startActivityForResult ( intent,2 );
                }
        } );

        dialog.setNegativeButton ( "取消", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText ( FridgeShowActivity.this, "已取消关联冰箱", Toast.LENGTH_SHORT )
                        .show ();
                new GetBoxIdRequest ( new MyHandler (), getCookie () );
            }
        } );

        AlertDialog alertDialog = dialog.create ();
        alertDialog.show ();
    }

    @SuppressLint("HandlerLeak")
    public class MyHandler extends Handler implements MyHandlerMsg {
        JsonObject jsonObject;
        String message;
        String code;

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_FAIL:
                    Toast.makeText ( FridgeShowActivity.this, "网络请求失败", Toast.LENGTH_SHORT )
                            .show ();
                    break;

                case CREATEICEBOX_SUCCESS:
                    Log.i ( TAG, "handleMessage: " + msg.obj.toString () );
                    jsonObject = (JsonObject)new JsonParser() .parse(msg.obj.toString ());
                    message = jsonObject.get ( "msg" ).getAsString ();
                    code = jsonObject.get ( "code" ).getAsString ();
                    fakeIceId = jsonObject.get ( "iceId" ).getAsString ();
                    if (code.equals ( "1" )){
                        Toast.makeText ( FridgeShowActivity.this, message, Toast.LENGTH_SHORT )
                                .show ();

                        connectBoxDialog();
                    }
                    break;

                case DELECTICEBOX_SUCCESS:
                    Toast.makeText ( FridgeShowActivity.this, "删除冰箱成功", Toast.LENGTH_SHORT )
                            .show ();
                    jsonObject = (JsonObject)new JsonParser() .parse(msg.obj.toString ());
                    message = jsonObject.get ( "msg" ).getAsString ();
                    code = jsonObject.get ( "code" ).getAsString ();
                    if (code.equals ( "1" )){
                        Toast.makeText ( FridgeShowActivity.this, message, Toast.LENGTH_SHORT )
                                .show ();
                         new GetBoxIdRequest ( new MyHandler (), getCookie () );
                    }
                    break;

                case CONNECTBOX_SUCCESS:
                    Toast.makeText ( FridgeShowActivity.this, msg.obj.toString (), Toast.LENGTH_SHORT )
                            .show ();
                    jsonObject = (JsonObject)new JsonParser() .parse(msg.obj.toString ());
                    message = jsonObject.get ( "msg" ).getAsString ();
                    code = jsonObject.get ( "code" ).getAsString ();
                    if (code.equals ( "1" )){
                        Toast.makeText ( FridgeShowActivity.this, message, Toast.LENGTH_SHORT )
                                .show ();
                        new GetBoxIdRequest ( new MyHandler (), getCookie () );
                    }
                    break;

                case GETICEID_SUCCESS:
                    JsonTool jsonTool = new JsonTool ( msg.obj.toString (), GETICEID_SUCCESS );
                    if (jsonTool.judgeMsg ().equals ( "0" )){
                        initData ();
                    }
                    break;
            }
        }
    }

}
