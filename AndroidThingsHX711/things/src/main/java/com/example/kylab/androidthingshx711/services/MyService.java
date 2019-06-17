package com.example.kylab.androidthingshx711.services;

import android.app.Service;


import com.example.kylab.androidthingshx711.design.MyUAObServer;
import com.example.kylab.androidthingshx711.design.MyUASubject;
import com.example.kylab.androidthingshx711.tools.DataType;

import java.util.ArrayList;


public abstract class MyService extends Service implements MyUASubject {


    private ArrayList<MyUAObServer> myUaObservers;

    public MyService() {
        myUaObservers = new ArrayList<>();
    }


    /**
     * 观察者模式 注册
     * **/
    @Override
    public void registerObserver(MyUAObServer o) {
        if(myUaObservers!=null&&o!=null)
        {
            myUaObservers.add(o);
        }
    }
    /**
     * 观察者模式 移除
     * **/
    @Override
    public void removeObserver(MyUAObServer o) {
        if(o!=null) {
            int i = myUaObservers.indexOf(o);
            myUaObservers.remove(i);
        }

    }
    /**
     * 观察者模式 通知观察者
     * **/
    @Override
    public void notifyObserver(int dataType,String messagedata) {
        for(int i = 0 ;i<myUaObservers.size();i++)
        {
            MyUAObServer observer = (MyUAObServer)myUaObservers.get(i);
            if(observer.getWeightDataPrm()&&(dataType== DataType.WEIGHTDATA)) {
                observer.updata(dataType, messagedata);
            }
            if(observer.getNFCDataPrm()&&dataType== DataType.NFCDATA)
            {
                observer.updata(dataType, messagedata);
            }
        }

    }





}
