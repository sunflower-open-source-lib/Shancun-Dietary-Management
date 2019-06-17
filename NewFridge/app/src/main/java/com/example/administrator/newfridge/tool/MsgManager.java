package com.example.administrator.newfridge.tool;

import java.util.HashMap;
import java.util.Map;

public class MsgManager implements MyHandlerMsg {

    private Map<String, Integer> msgMap;

    public MsgManager() {
        msgMap = new HashMap<> ();
        initValue ();
    }

    private void initValue() {
        msgMap.put ( "login", LOGIN_SUCCESS );
        msgMap.put ( "getiden", GETIDEN_SUCCESS );
        msgMap.put ( "register", REGISTER_SUCCESS );
        msgMap.put ( "getBoxId", GETICEID_SUCCESS );
        msgMap.put ( "getFoodList", GETFOOD_SUCCESS );
        msgMap.put ( "getFamilyInfo", GETFAMILYINFO_SUCCESS );
        msgMap.put ( "createFamily", CREATEFAMILY_SUCCESS );
        msgMap.put ( "createIce", CREATEICEBOX_SUCCESS );
        msgMap.put ( "delectIce", DELECTICEBOX_SUCCESS );
        msgMap.put ( "outFamily", DELECTFAMILY_SUCCESS );
        msgMap.put ( "menus", MENUS_SUCCESS );
        msgMap.put ( "iceBoxInfo", ICEBOXINFO_SUCCESS );
        msgMap.put ( "invitation", INVITATION_SUCCESS );
        msgMap.put ( "connectBox", CONNECTBOX_SUCCESS );
    }

    public int getMsg(String key){
        return msgMap.get ( key );
    }
}
