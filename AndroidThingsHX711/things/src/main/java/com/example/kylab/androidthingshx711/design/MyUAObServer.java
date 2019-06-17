package com.example.kylab.androidthingshx711.design;



public interface MyUAObServer {
     boolean getWeightDataPrm();
     boolean getNFCDataPrm();
     void updata(int dataType, String messagedata);
}
