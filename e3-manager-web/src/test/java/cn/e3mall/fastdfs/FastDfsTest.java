package cn.e3mall.fastdfs;

import cn.e3mall.common.utils.FastDFSClient;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.IOException;

import static javafx.scene.input.KeyCode.L;

public class FastDfsTest {

    @Test
    public void testUpload() throws IOException, MyException {
        //创建一个配置文件。文件名任意，内容就是tracker的地址
        //创建全局对象加载配置文件
        ClientGlobal.init("G:/Projects/e3-Shopping/e3-manager-web/src/main/resources/conf/client.conf");
        //创建一个TrackClient对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrcakClient 获得一个TrackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //创建一个StorageServer引用，可以为null
        StorageServer storageServer = null;
        //创建一个StorageClient对象，参数需要TrackServer和StorageServer
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //使用StorageClient上传文件
//        String[] strings = storageClient.upload_file("G:/test.jpg", "jpg", null);
//        for (String s : strings) {
//            System.out.println(s);
//        }
    }

    @Test
    public void testFastDfsClient() throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("G:/Projects/e3-Shopping/e3-manager-web/src/main/resources/conf/client.conf");
//        String string = fastDFSClient.uploadFile("G:/test1.jpg");
//        System.out.println(string);
    }

}
