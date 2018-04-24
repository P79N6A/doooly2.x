package com.doooly.publish.rest.life.impl;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.doooly.publish.rest.life.AppRestServiceI;

/**
 * 下载Doooly app接口
 * @author 赵清江
 * @date 2016年7月20日
 * @version 1.0
 */
@Component
@Path("/app")
public class AppRestService implements AppRestServiceI {

	@GET
	@Path(value="/download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@Context ServletContext application){
		String path = application.getRealPath("/apk/1");
		File file = new File(path, "doooly-app-1.0.0.apk");
		String mediaType = new MimetypesFileTypeMap().getContentType(file);
		
		return Response
                .ok(file, mediaType)
                .header("Content-disposition","attachment;filename=" + file.getName())
                .header("pragma", "No-cache").header("Cache-Control", "no-cache").build();
	}

	@Override
	public String upload() {
		// TODO Auto-generated method stub
		return null;
	}

	@GET
	@Path(value="/update/{version}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response update(@PathParam("version") String version) {
		// TODO Auto-generated method stub
		return null;
	}

}
