package com.sky.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.sky.properties.AliOssProperties;
import org.springframework.stereotype.Component;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.OSSClientBuilder;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.EnvironmentVariableCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.aliyun.sdk.service.oss2.models.PutObjectResult;
import com.aliyun.sdk.service.oss2.transport.BinaryData;

/**
 * 阿里云oss上传文件
 */
public class AliOssUtil {

       /*  @Value("${aliyun.oss.region}")
        private String region; // The region in which the bucket is located.不要加oss-前缀！
        
        @Value("${aliyun.oss.endpoint}")
        private String endpoint; // The domain names that other services can use to access OSS.
        
        @Value("${aliyun.oss.bucket}")
        private String bucket; // The name of the bucket. */
        
        private AliOssProperties aliOSSProperties;
        
        public AliOssUtil(AliOssProperties aliOSSProperties){
                this.aliOSSProperties = aliOSSProperties;
        }
        
        
        /**
         * 文件上传
         * @param fileIstream
         * @param objectName
         * @return
         * @throws IOException
         */
        public String upload(InputStream fileIstream, String objectName) throws IOException {
                
                String region = aliOSSProperties.getRegion();
                String bucket = aliOSSProperties.getBucketName();
                String endpoint = aliOSSProperties.getEndpoint();
                
                //客户端
                CredentialsProvider provider = new EnvironmentVariableCredentialsProvider();
                OSSClientBuilder clientBuilder = OSSClient.newBuilder()
                .credentialsProvider(provider)
                .region(region);
                
                if (endpoint != null) {
                        clientBuilder.endpoint(endpoint);
                }
                
                //uuid文件名字
//                String originalFileName = file.getOriginalFilename();
//                String newFileName = UUID.randomUUID().toString() + originalFileName.substring( originalFileName.lastIndexOf('.'));
//                String key = newFileName;// The name of the object.定义在oss服务器上呈现的目录结构
//                上面是objectName
                
                try (OSSClient client = clientBuilder.build()) {
                        
                        PutObjectResult result = client.putObject(PutObjectRequest.newBuilder()
                        .bucket(bucket)
                        .key(objectName)
                        .body(BinaryData.fromStream(fileIstream))
                        .build());
                        
                        System.out.printf("status code:%d, request id:%s, eTag:%s\n",
                        result.statusCode(), result.requestId(), result.eTag());
                        
                } catch (Exception e) {
                        // If the exception is caused by ServiceException, detailed information can be
                        // obtained in this way.
                        // ServiceException se = ServiceException.asCause(e);
                        // if (se != null) {
                        // System.out.printf("ServiceException: requestId:%s, errorCode:%s\n",
                        // se.requestId(), se.errorCode());
                        // }
                        System.out.printf("error:\n%s", e);
                        throw new IOException(e);
                }
                
                String url = "https://" + bucket + "." + endpoint + "/" + objectName ; //https://tlias-lvannig.oss-cn-beijing.aliyuncs.com/%E7%8F%8D%E7%8F%A0.png
                return url;
        }
}






/*
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {
        
        private String endpoint;
        private String region;
        private String bucketName;
//        */
//        /**
//         * 文件上传
//         * @param bytes
//         * @param objectName
//         * @return
//         */
       /* public String upload(byte[] bytes, String objectName) {
                
                // 创建OSSClient实例。
                OSS ossClient = new OSSClientBuilder().build(endpoint, region, bucketName);
                
                try {
                        // 创建PutObject请求。
                        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
                } catch (OSSException oe) {
                        System.out.println("Caught an OSSException, which means your request made it to OSS, "
                        + "but was rejected with an error response for some reason.");
                        System.out.println("Error Message:" + oe.getErrorMessage());
                        System.out.println("Error Code:" + oe.getErrorCode());
                        System.out.println("Request ID:" + oe.getRequestId());
                        System.out.println("Host ID:" + oe.getHostId());
                } catch (ClientException ce) {
                        System.out.println("Caught an ClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with OSS, "
                        + "such as not being able to access the network.");
                        System.out.println("Error Message:" + ce.getMessage());
                } finally {
                        if (ossClient != null) {
                                ossClient.shutdown();
                        }
                }
                
                //文件访问路径规则 https://BucketName.Endpoint/ObjectName
                StringBuilder stringBuilder = new StringBuilder("https://");
                stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);
                
                log.info("文件上传到:{}", stringBuilder.toString());
                
                return stringBuilder.toString();
        }
}*/
