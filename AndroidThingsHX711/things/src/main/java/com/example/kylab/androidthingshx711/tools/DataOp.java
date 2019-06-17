package com.example.kylab.androidthingshx711.tools;


public  class DataOp
 {
     private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
             '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
     /**
      * 方法二：
      * byte[] to hex string
      *
      * @param bytes
      * @return
      */
     public static String bytesToHexFun2(byte[] bytes) {
         char[] buf = new char[bytes.length * 2];
         int index = 0;
         for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
             buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
             buf[index++] = HEX_CHAR[b & 0xf];
         }

         return new String(buf);
     }

     public static String toDataString(String data) {
         return ("S" + data + "N");
     }



     public static String asciiToString(String value) {
         StringBuffer sbu = new StringBuffer(value);
         StringBuffer res = new StringBuffer();
         //String[] chars = value.split(",");
         int index;
         for (index = 2; index < sbu.length(); index += 3) {
             sbu.insert(index, ',');
         }
         String[] array = sbu.toString().split(",");
         //Log.d(TAG, "array:" + sbu.toString());
         for (String string : array) {
             //System.out.print(string+"--");
             res.append((char) Integer.parseInt(string, 16));
         }
         return res.toString();
     }


}
