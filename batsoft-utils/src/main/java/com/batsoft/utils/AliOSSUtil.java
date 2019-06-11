package com.batsoft.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/27.
 */
@Slf4j
@Component("aliOSSUtil")
public class AliOSSUtil {
    @Autowired
    private  AliOSSConfig aliOSSConfig;
    private static AliOSSConfig config;

    @PostConstruct
    public void init() {
        config = aliOSSConfig;
    }

    public static void bucket(OSSClient ossClient) {
        // 创建OSS客户端
        // 判断文件容器是否存在，不存在则创建
        if (!ossClient.doesBucketExist(config.getBUCKET_NAME())) {
            ossClient.createBucket(config.getBUCKET_NAME());
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(config.getBUCKET_NAME());
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
        }
    }

    /**
     * 方法描述:上传文件
     *
     * @author leon 2016年12月16日 上午11:40:34
     * @param file 文件对象
     * @return
     */
    public static String upload(File file,String filePath) {
        if (file == null) {
            return null;
        }
        // 创建OSS客户端
        OSSClient ossClient = new OSSClient(config.getEND_POINT(), config.getACCESS_KEY_ID(), config.getACCESS_KEY_SECRET());
        try {
            bucket(ossClient);
            // 上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(config.getBUCKET_NAME(), filePath, file));
            if (null != result) {
                return  filePath;
            }
        } catch (OSSException oe) {
            log.info(oe.getMessage());
        } catch (ClientException ce) {
            log.info(ce.getMessage());
        } finally {
            // 关闭OSS服务，一定要关闭
            ossClient.shutdown();
        }
        return null;
    }

    /**
     * 方法描述:上传文件
     *
     * @author leon 2016年12月26日 下午3:33:13
     * @param inputStream 文件流
     * @return
     * @throws FileNotFoundException
     */
    public static String upload(InputStream inputStream,String filePath) throws FileNotFoundException {
        if (inputStream == null) {
            return null;
        }
        // 创建OSS客户端
        OSSClient ossClient = new OSSClient(config.getEND_POINT(), config.getACCESS_KEY_ID(), config.getACCESS_KEY_SECRET());
        try {
            bucket(ossClient);
            // 上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(config.getBUCKET_NAME(), filePath, inputStream));
            if (null != result) {
                return  filePath;
            }
        } catch (OSSException oe) {
            log.info(oe.getMessage());
        } catch (ClientException ce) {
            log.info(ce.getMessage());
        } finally {
            // 关闭OSS服务，一定要关闭
            ossClient.shutdown();
        }
        return null;

    }
    public static URL getURL(String key){
        OSSClient ossClient = new OSSClient(config.getEND_POINT(), config.getACCESS_KEY_ID(), config.getACCESS_KEY_SECRET());
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(config.getBUCKET_NAME(), key, HttpMethod.GET);
        //设置过期时间
        long curren = System.currentTimeMillis();
        curren += 30 * 60 * 1000;
        Date da = new Date(curren);
        request.setExpiration(da);
        // 生成URL签名(HTTP GET请求)
        System.out.println("访问地址===="+ossClient .generatePresignedUrl(request));

        return ossClient .generatePresignedUrl(request);
    }
    public static InputStream download(String key){
        OSSClient ossClient = new OSSClient(config.getEND_POINT(), config.getACCESS_KEY_ID(), config.getACCESS_KEY_SECRET());
        OSSObject ossObject = ossClient.getObject(config.getBUCKET_NAME(), key);
        InputStream inputStream = null;
        StringBuilder objectContent = new StringBuilder();
        try {

            inputStream = ossObject.getObjectContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                objectContent.append(line);
            }

            System.out.println("Object：" + key + "的内容是：" + objectContent);
        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return inputStream;
    }
}
