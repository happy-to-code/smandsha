package chain.tj.service;

import chain.tj.common.response.RestResponse;
import chain.tj.model.query.FileDto;
import chain.tj.service.impl.SmDataProcessing;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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

        RestResponse restResponse = smDataProcessing.fileToHash(fileDto);
        // 441bc95c706af0063943ca42d7e43df19a898a88b8bbe732b4b8b25b701dfa38
        // 4f3043f2b8c747b878b6fcee2c742933b4d80e7fe88f524ccd686e0c67005234
        System.out.println("restResponse = " + restResponse);
    }

    @Test
    public void testFileSign() throws Exception {
        FileDto fileDto = new FileDto();
        fileDto.setFilePath("E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\test.txt");

        RestResponse restResponse = smDataProcessing.signFile("4f3043f2b8c747b878b6fcee2c742933b4d80e7fe88f524ccd686e0c67005234");
        // 3046022100b2d82e1cb7e2d5158f77d05485ae9e14447fa892a1a77aa4712ba33242aa7936022100ea825428521224fea15d6145c2da064d20ea488637188765b496acd5dfcb9400
        System.out.println("restResponse = " + restResponse);
    }

    @Test
    public void testVerifyFile() throws Exception {

        String sign = "3046022100b2d82e1cb7e2d5158f77d05485ae9e14447fa892a1a77aa4712ba33242aa7936022100ea825428521224fea15d6145c2da064d20ea488637188765b496acd5dfcb9400";
        String fileHash = "4f3043f2b8c747b878b6fcee2c742933b4d80e7fe88f524ccd686e0c67005234";

        RestResponse restResponse = smDataProcessing.verifyFile(sign, fileHash);
        System.out.println("restResponse = " + restResponse);
    }

}