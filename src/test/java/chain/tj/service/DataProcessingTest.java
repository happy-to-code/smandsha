package chain.tj.service;

import chain.tj.common.response.RestResponse;
import chain.tj.model.query.FileDto;
import chain.tj.service.impl.SmDataProcessing;
import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 16:04
 * @description：
 * @modified By：
 * @version:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataProcessingTest extends TestCase {
    @Resource
    private SmDataProcessing smDataProcessing;


    @Test
    public void testFileToHash() {
        FileDto fileDto = new FileDto();
        fileDto.setFilePath("E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\test.txt");

        RestResponse restResponse = smDataProcessing.fileToHash(fileDto, "sha");
        // 441bc95c706af0063943ca42d7e43df19a898a88b8bbe732b4b8b25b701dfa38
        // 4f3043f2b8c747b878b6fcee2c742933b4d80e7fe88f524ccd686e0c67005234
        // 65386636376465353539636130636339363731616233326266316561363964663831393937323432396434383431306263373639383463666531626131636666
        // 30336337336531386366323734396366366562306535663839643864643062366338393864326434326430663663373365653237353166613763663830376533
        // 30336337336531386366323734396366366562306535663839643864643062366338393864326434326430663663373365653237353166613763663830376533
        System.out.println("restResponse = " + restResponse);
    }

    @Test
    public void testFileSign() throws Exception {
        FileDto fileDto = new FileDto();
        fileDto.setFilePath("E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\test.txt");

        RestResponse restResponse = smDataProcessing.signFile("30336337336531386366323734396366366562306535663839643864643062366338393864326434326430663663373365653237353166613763663830376533");
        // 3046022100b2d82e1cb7e2d5158f77d05485ae9e14447fa892a1a77aa4712ba33242aa7936022100ea825428521224fea15d6145c2da064d20ea488637188765b496acd5dfcb9400
        System.out.println("restResponse = " + restResponse);
    }

    @Test
    public void testVerifyFile() throws Exception {

        String sign = "30460221008aacf76c9305ca6db3dddcd1f82a8d4c3529cd76cfbf34819a7ecee79bcfc858022100e42a43dfb4fe4400908734cbf7083f91fe55083ce71ff29afb57fadf862a22ab";
        String fileHash = "30336337336531386366323734396366366562306535663839643864643062366338393864326434326430663663373365653237353166613763663830376533";

        RestResponse restResponse = smDataProcessing.verifyFile(sign, fileHash);
        System.out.println("restResponse = " + restResponse);
    }


    @Test
    public void doGetTestOne() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet("http://10.1.5.226:58080/getblockbyheight?number=5");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void doPostTestTwo() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Post请求
        HttpPost httpPost = new HttpPost("http://10.1.5.226:58080/store");

        Map<String, String> map = new HashMap<>(2);
        map.put("Data", "123xxmmm");

        String jsonString = JSON.toJSONString(map);

        StringEntity entity = new StringEntity(jsonString, "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}