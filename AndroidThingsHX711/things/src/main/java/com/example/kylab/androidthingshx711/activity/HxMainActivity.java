package com.example.kylab.androidthingshx711.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.kylab.androidthingshx711.R;
import com.example.kylab.androidthingshx711.services.BoardDefaults;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;

import java.io.IOException;

/**
 * Example activity that provides a UART loopback on the
 * specified device. All data received at the specified
 * baud rate will be transferred back out the same UART.
 */
public class HxMainActivity extends Activity {
    private static final String TAG = "LoopbackActivity";

    // UART Configuration Parameters
    private static final int BAUD_RATE = 115200;
    private static final int DATA_BITS = 8;
    private static final int STOP_BITS = 1;
    private static final int CHUNK_SIZE = 32;

    private HandlerThread mInputThread;
    private Handler mInputHandler;
    private UartDevice mLoopbackDevice;
    TextView tv ;
    android.widget.Button bu,bu_reset;

    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                String str = tv.getText().toString();
                tv .setText(str+msg.obj.toString());
            }
        }
    };

    private Runnable mTransferUartRunnable = new Runnable() {
        @Override
        public void run() {
            transferUartData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Loopback Created");
        setContentView(R.layout.activity_food_weighting);
        tv = findViewById(R.id.textView);
        bu = findViewById(R.id.button);
        bu_reset = findViewById(R.id.button_res);
        // Create a background looper thread for I/O
        mInputThread = new HandlerThread("InputThread");
        mInputThread.start();
        mInputHandler = new Handler(mInputThread.getLooper());
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessagetoUA(1);
                Log.d(TAG,"Button DJ");
            }
        });
        bu_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("称重显示");
            }
        });
        // Attempt to access the UART device
        try {
            openUart(BoardDefaults.getUartName(), BAUD_RATE);
            // Read any initially buffered data
            mInputHandler.post(mTransferUartRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Unable to open UART device", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Loopback Destroyed");

        // Terminate the worker thread
        if (mInputThread != null) {
            mInputThread.quitSafely();
        }

        // Attempt to close the UART device
        try {
            closeUart();
        } catch (IOException e) {
            Log.e(TAG, "Error closing UART device:", e);
        }
    }

    /**
     * Callback invoked when UART receives new incoming data.
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

    /* Private Helper Methods */

    /**
     * Access and configure the requested UART device for 8N1.
     *
     * @param name Name of the UART peripheral device to open.
     * @param baudRate Data transfer rate. Should be a standard UART baud,
     *                 such as 9600, 19200, 38400, 57600, 115200, etc.
     *
     * @throws IOException if an error occurs opening the UART port.
     */
    private void openUart(String name, int baudRate) throws IOException {
        mLoopbackDevice = PeripheralManager.getInstance().openUartDevice(name);
        // Configure the UART
        mLoopbackDevice.setBaudrate(baudRate);
        mLoopbackDevice.setDataSize(DATA_BITS);
        mLoopbackDevice.setParity(UartDevice.PARITY_NONE);
        mLoopbackDevice.setStopBits(STOP_BITS);
        mLoopbackDevice.registerUartDeviceCallback(mInputHandler, mCallback);
    }

    /**
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
     * Loop over the contents of the UART RX buffer, transferring each
     * one back to the TX buffer to create a loopback service.
     *
     * Potentially long-running operation. Call from a worker thread.
     */
    private void transferUartData() {
        String record="";
        Message myMessage ;
        if (mLoopbackDevice != null) {
            // Loop until there is no more data in the RX buffer.
            try {
                byte[] buffer = new byte[CHUNK_SIZE];
                int read;
                while ((read = mLoopbackDevice.read(buffer, buffer.length)) > 0) {
                    Log.d(TAG,read+"!!");
                    //mLoopbackDevice.write(buffer, read);
                    for(int i = 0 ;i<=read;i++)
                    {
                        char res = (char) buffer[i];
                        record = record + res;
                    }
                    record = bytes2HexString(buffer)+"|";
                    Log.d(TAG,record);
                    if(!record.equals(""))
                    {
                        Log.d(TAG,record);
                        myMessage = new Message();
                        myMessage.what=1;
                        myMessage.obj=record;
                        myHandler.sendMessage(myMessage);
                    }
                }
            } catch (IOException e) {
                Log.w(TAG, "Unable to transfer data over UART", e);
            }
        }
    }
    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[ i ] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public  String printHexString( byte[] b) {
        String strHex = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            strHex+=hex.toUpperCase() + " ";
        }
        return strHex;
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
}

