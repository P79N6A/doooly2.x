package com.doooly.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.alibaba.druid.util.StringUtils;

/**
 * 
 * 文件工具类
 * 
 * @author yuelou.zhang
 * @version 2017年7月24日
 */
public class FileUtils {

	/**
	 * 生成UUID
	 * 
	 */
	public static String create_nonce_str() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "");
		if (uuid.length() >= 32) {
			uuid = uuid.substring(0, 32);
		}
		return uuid;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * 
	 */
	public static void createFolder(String path) throws IOException {
		File file = new File(path);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
	}

	/**
	 * 写文件流到指定目录
	 * 
	 * @param in
	 *            输入流
	 * @param targetPath
	 *            目标路径
	 * 
	 */
	public static void writeFile(InputStream in, String targetPath) {
		if (in == null) {
			return;
		}
		if (StringUtils.isEmpty(targetPath)) {
			return;
		}
		OutputStream os = null;
		try {
			File file = new File(targetPath);
			if (file.isDirectory()) {
				throw new RuntimeException("[" + targetPath + "]是一个目录，而非一个文件路径");
			}
			os = new FileOutputStream(file);
			int len = 0;
			byte[] ch = new byte['?'];
			while ((len = in.read(ch)) != -1) {
				os.write(ch, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
