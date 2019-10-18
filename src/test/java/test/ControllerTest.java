package test;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.util.concurrent.CountDownLatch;


/**
 * @Author: zxj
 * @Date: 2019/9/27 8:29 下午
 * @desc
 */
public class ControllerTest {

   private static final CountDownLatch countDownLatch = new CountDownLatch(400);

    public static void main(String[] args)throws Exception {

        HttpClient httpClient = new HttpClient();

        String uri = "http://localhost:8082/test/get";
        HttpMethod method = new GetMethod(uri);
        httpClient.executeMethod(method);

        //服务器返回状态
        System.out.println(method.getStatusLine());
        //返回的内容
        System.out.println(method.getResponseBodyAsString());
        //释放连接
        method.releaseConnection();
    }
}
