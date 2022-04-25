package com.shanjupay.common.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

public class QiniuUtils {
   /* private static final String accessKey = "GSXRQjW0Yhno2szNOizD2rcKnmJrjuOW0ouQceyN";
    private static final String secretKey = "xQaGiabRm5ry8xiu1JnUMvqIEUM5QiRGdsYuh9v-";*/
    //private static final String bucket = "shangjuzhifu";  // 空间名

    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuUtils.class);

    /**
     * 文件上传的工具方法
     * @param bucket
     * @param bytes
     * @param fileName  // 外部传进的文件名，七牛云上的文件名和此保持一致
     */
    public static void  upload2qiniu(String accessKey,String secretKey,String bucket, byte[] bytes,String fileName) throws RuntimeException{

        //构造一个带指定 Region 对象的配置类，指定存储区域，和存储空间选择的区域一致
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        try {

            //认证
            Auth auth = Auth.create(accessKey, secretKey);
            //认证通过后得到token（令牌）
            String upToken = auth.uploadToken(bucket);
            try {
                //上传文件,参数：字节数组，key，token令牌
                //key: 建议我们自已生成一个不重复的名称
                Response response = uploadManager.put(bytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                LOGGER.error("上传文件到七牛：{}",ex.getMessage());
                try {
                    LOGGER.error(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
                throw new RuntimeException(r.bodyString());
            }
        } catch (Exception ex) {
            LOGGER.error("上传文件到七牛：{}",ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }





    // 测试用例
    private static void upLoad() {
        Configuration cfg = new Configuration();
        UploadManager uploadManager = new UploadManager(cfg);

        String key = UUID.randomUUID() + ".jpg";

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(new File("D:\\BaiduNetdiskDownload\\叫什么.jpg"));
            byte[] uploadBytes = IOUtils.toByteArray(fileInputStream);
            Auth auth = Auth.create("GSXRQjW0Yhno2szNOizD2rcKnmJrjuOW0ouQceyN", "xQaGiabRm5ry8xiu1JnUMvqIEUM5QiRGdsYuh9v");
            String upToken = auth.uploadToken("shangjuzhifu");

            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
//解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),
                        DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);


            } catch (QiniuException e) {
                Response r = e.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
//ignore
                }
            }
        } catch (IOException ex) {
//ignore
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

 /*   public static void main(String[] args) {
        upLoad();
    }*/
}
