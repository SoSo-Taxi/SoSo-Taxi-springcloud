package com.apicaller.sosotaxi.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/20 6:42 下午
 * @updateTime:
 */
@Component
public class QiNiuUtil {
    private static final Logger logger = LoggerFactory.getLogger(QiNiuUtil.class);

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.path}")
    private String path;

    @Value("${qiniu.cdn}")
    private String cdn;

    /**
     * 将图片上传到七牛云
     * @param file
     * @param key 保存在空间中的名字，如果为空会使用文件的hash值为文件名
     * @return
     */
    public String uploadImg(FileInputStream file, String key) {
        //region要和所选的地区对应
        Configuration cfg = new Configuration(Region.region2());

        UploadManager uploadManager = new UploadManager(cfg);

        //生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(file, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                String returnPath = path + "/" + putRet.key;
                return returnPath;

            } catch (QiniuException ex) {
                Response r = ex.response;
                logger.info(r.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取七牛下载链接
     * @param filePath
     * @return
     */
    public String getDownloadUrl(String filePath) {

        if(filePath == null) {
            return null;
        }
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(filePath, "utf-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        String publicUrl = String.format("%s/%s", cdn, encodedFileName);
        Auth auth = Auth.create(accessKey, secretKey);
        //链接过期时间
        long expireInSeconds = 3600;

        return auth.privateDownloadUrl(publicUrl, expireInSeconds);
    }
}

