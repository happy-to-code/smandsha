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

        RestResponse restResponse = smDataProcessing.fileToHash(fileDto,"sha");
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

}