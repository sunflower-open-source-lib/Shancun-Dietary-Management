package com.example.kylab.androidthingshx711.services;


import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import com.example.kylab.androidthingshx711.tools.DataOp;
import com.example.kylab.androidthingshx711.tools.DataType;
import com.example.kylab.androidthingshx711.tools.MyQueue;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;
import java.io.IOException;


public class MyUaService extends MyService {
    boolean start_flag = false;
    private String record = "";
    private static final int BAUD_RATE = 38400;
    private static final int DATA_BITS = 8;
    private static final int STOP_BITS = 1;
    private static final int CHUNK_SIZE = 32;
    private HandlerThread mUartInputThread;
    private Handler mUartInputHandler;
    private UartDevice mLoopbackDevice;
    private MyQueue toserData;


    private static final String TAG = "MyUAService";
    private Runnable mTransferUartRunnable = new Runnable() {
        @Override
        public void run() {
            transferUartData();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        toserData = new MyQueue();
        uartInit();

    }

    /**
     * UART
     * UART设备及通讯线程开启
     */
    private void uartInit() {
        // Create a background looper thread for I/O
        mUartInputThread = new HandlerThread("InputThread");
        mUartInputThread.start();
        mUartInputHandler = new Handler(mUartInputThread.getLooper());
        // Attempt to access the UART device
        try {
            openUart(BoardDefaults.getUartName(), BAUD_RATE);
            // Read any initially buffered data
            mUartInputHandler.post(mTransferUartRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Unable to open UART device", e);
        }
    }

    /**
     * UART
     * 设备UART收到信息后回调transferUartData()函数.
     */
    private UartDeviceCallback mCallback = new UartDeviceCallback() {
        @Override
        public boolean onUartDeviceDataAvailable(UartDevice uart) {
            // Queue up a data transfer
            transferUartData();
            //Continue listening for more interrupts
            return true;
        }

        @Override
        public void onUartDeviceError(UartDevice uart, int error) {
            Log.w(TAG, uart + ": Error event " + error);
        }
    };

    /**
     * UART
     * 初始化访问设备UART
     * Access and configure the requested UART device for 8N1.
     *
     * @param name     Name of the UART peripheral device to open.
     * @param baudRate Data transfer rate. Should be a standard UART baud,
     *                 such as 9600, 19200, 38400, 57600, 115200, etc.
     * @throws IOException if an error occurs opening the UART port.
     */
    private void openUart(String name, int baudRate) throws IOException {

        mLoopbackDevice = PeripheralManager.getInstance().openUartDevice(name);
        // Configure the UART
        mLoopbackDevice.setBaudrate(baudRate);
        mLoopbackDevice.setDataSize(DATA_BITS);
        mLoopbackDevice.setParity(UartDevice.PARITY_NONE);
        mLoopbackDevice.setStopBits(STOP_BITS);
        mLoopbackDevice.registerUartDeviceCallback(mUartInputHandler, mCallback);
    }

    /**
     * UART
     * 退出时关闭设备UART链接
     * Close the UART device connection, if it exists
     */
    private void closeUart() throws IOException {
        if (mLoopbackDevice != null) {
            mLoopbackDevice.unregisterUartDeviceCallback(mCallback);
            try {
                mLoopbackDevice.close();
            } finally {
                mLoopbackDevice = null;
            }
        }
    }

    /**
     * UART
     * 设备UART数据读取
     * Loop over the contents of the UART RX buffer, transferring each
     * one back to the TX buffer to create a loopback service.
     * <p>
     * Potentially long-running operation. Call from a worker thread.
     */
    private void transferUartData() {

        if (mLoopbackDevice != null) {
            // Loop until there is no more data in the RX buffer.
            try {
                byte[] buffer = new byte[CHUNK_SIZE];
                int read;
                while ((read = mLoopbackDevice.read(buffer, buffer.length)) > 0) {
                    String a = DataOp.bytesToHexFun2(buffer);
                    Log.d(TAG, "收到" + read + "b数据：" + a);
                    for (int i = 0; i < read; i++) {
                        if (buffer[i] == (byte) 0x53) {//s
                            start_flag = true;
                            //Log.d(TAG,"开始！");
                        } else if (buffer[i] == (byte) 0x4e) {//n
                            start_flag = false;
                            Log.d(TAG, "record:" + record);
                            if (record != "") {
                                //判断NFC数据与weight数据
                                if (record.length() > 10) {
                                    notifyObserver(DataType.NFCDATA, record);
                                } else {
                                    toserData.enQueue(record);
                                    notifyObserver(DataType.WEIGHTDATA, record);
                                }
                            }
                            record = "";
                        } else if (start_flag && ((buffer[i] >= (byte) 0x30 && buffer[i] <= (byte) 0x39) ||buffer[i] == (byte) 0x74|| (buffer[i] >= (byte) 0x41 && buffer[i] <= (byte) 0x46))) {
                            char res = (char) buffer[i];
                            record = record + res;
                        }
                    }
                }
            } catch (IOException e) {
                Log.w(TAG, "Unable to transfer data over UART", e);
            }
        }
    }

    /**
     * UART
     * 设备UART数据发送（）
     **/
    public void sendMessagetoUA(int command) {
        if (mLoopbackDevice != null) {
            byte[] byteNum = new byte[4];
            for (int ix = 0; ix < 4; ++ix) {
                int offset = 32 - (ix + 1) * 8;
                byteNum[ix] = (byte) ((command >> offset) & 0xff);
            }
            try {
                mLoopbackDevice.write(byteNum, 4);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new LocalBinder();
    }

    public final class LocalBinder extends Binder {
        public MyUaService getService() {
            Log.d(TAG,"getService");
            return MyUaService.this;

        }
    }

    /**
     * UART
     * 关闭UART线程
     */
    public void closeUartThread() {
        if (mUartInputThread != null) {
            mUartInputThread.quitSafely();
        }
    }


    public void stopMyService() {
        // Terminate the worker thread
        closeUartThread();
        // Attempt to close the UART device
        try {
            closeUart();
        } catch (IOException e) {
            Log.e(TAG, "Error closing UART device:", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "MyUAWFService Destroy!");
        stopMyService();
    }

}
