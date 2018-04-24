package com.doooly.common.util;

import java.io.File;

import com.doooly.business.common.utils.ReqTransFormatUtils;

/**
 * 项目根目录路径获取工具类
 * @author 赵清江
 * @date 2016年7月22日
 * @version 1.0
 */
public class ProjectRootPathUtils {

	/**
	 * 获取根目录路径
	 * @return
	 */
	public static String getRootPath() {
		String classPath = ProjectRootPathUtils.class.getClassLoader().getResource("/").getPath();
		String rootPath = "";
		//windows
		if("\\".equals(File.separator)){
		rootPath = classPath.substring(1,classPath.indexOf("/WEB-INF"));
		rootPath = rootPath.replace("%20", " ");
		}
		//linux
		if("/".equals(File.separator)){
		rootPath = classPath.substring(0,classPath.indexOf("/WEB-INF"));
		}
		
		return rootPath;
	}
	
}
