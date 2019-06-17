package com.example.kylab.androidthingshx711.design;



public interface MyUASubject {
     void registerObserver(MyUAObServer o);
     void removeObserver(MyUAObServer o);
     void notifyObserver(int dataType, String messagedata);
}
