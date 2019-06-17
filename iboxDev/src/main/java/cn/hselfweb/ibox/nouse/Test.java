package cn.hselfweb.ibox.nouse;

import cn.hselfweb.ibox.utils.QrCodeUtil;
import cn.hselfweb.ibox.utils.UpLoadUtil;

public class Test {
    public static void main(String[] args) {
        QrCodeUtil qrCodeUtil = new QrCodeUtil();
        String iceId = "89f3218e64df440aa2a556fd2e5aabc1";
        String url = "iceId="+"89f3218e64df440aa2a556fd2e5aabc1";
        String path = "D://testQrcode//";
        String fileName = "ice.jpg";
        qrCodeUtil.createQrCode(url, path, fileName);
        //存储到七牛云
        UpLoadUtil upLoadUtil = new UpLoadUtil();
        upLoadUtil.setKey(""+"ice.jpg");
        //upLoadUtil.setLocalFilePath(path+fileName);
        upLoadUtil.setLocalFilePath("D://testQrcode//"+fileName);
        upLoadUtil.upload();
    }
}
