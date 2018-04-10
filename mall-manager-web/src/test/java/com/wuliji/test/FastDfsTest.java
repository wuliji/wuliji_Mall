package com.wuliji.test;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.wuliji.common.utils.FastDFSClient;

public class FastDfsTest {
	
	@Test
	public void testUpload() throws Exception{
		//创建一个配置文件，内容就是tracker服务器地址
		//使用全局对象加载配置文件
		ClientGlobal.init("E:\\git_repositories\\wuliji_Mall\\mall-manager-web\\src\\main\\resources\\conf\\client.conf");
		//创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//通过TrackClient获得一个TrackServer对象
		TrackerServer trackServer = trackerClient.getConnection();
		//创建一个StorageServer的引用，可以是null
		StorageServer storageServer = null;
		//创建一个StorageClient，参数需要TrackerServer和StorageServer
		StorageClient storageClient = new StorageClient(trackServer, storageServer);
		//使用StorageClient上传文件
		String[] file = storageClient.upload_file("E:\\upload\\haha.jpg", "jpg", null);
		for (String string : file) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testFastDfsClient() throws Exception {
		FastDFSClient test = new FastDFSClient("E:\\git_repositories\\wuliji_Mall\\mall-manager-web\\src\\main\\resources\\conf\\client.conf");
		String uploadFile = test.uploadFile("E:\\Documents\\Desktop\\01.参考资料\\广告图片\\9a80e2d06170b6bb01046233ede701b3.jpg");
		System.out.println(uploadFile);
		
	}
}
