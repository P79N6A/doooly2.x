package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.home.v2.servcie.IndexServiceI;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.VersionConstants;
import com.doooly.dto.common.MessageDataBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 兜礼首页接口（微信端/app）
 * 接口版本管理
 */
@Component
@Path("/wechat/indexService")
public class IndexRestService {
	private static Logger logger = LoggerFactory.getLogger(IndexRestService.class);
	@Autowired
	private IndexServiceI indexService;
    @Autowired
    private StringRedisTemplate redisTemplate;

	/**
	 * 首页楼层查询
	 * 
	 * 接口v2.2:首页接口新增楼层
	 * 
	* @author  hutao 
	* @date 创建时间：2018年10月23日 下午4:13:36 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	@POST
	@Path(value = "/index/v2_2")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String indexV2_2(JSONObject params, @Context HttpServletRequest request) {
        String userToken = request.getHeader(Constants.TOKEN_NAME);
        logger.info("selectFloorsByV2_2() userToken={}", userToken);
        if (StringUtils.isEmpty(userToken)) {
            return new MessageDataBean("1001", "userToken is null").toJsonString();
        }
        String userId = String.valueOf(redisTemplate.opsForValue().get(userToken));
        String address = params.getString("address");
        try {
            logger.info("selectFloorsByV2_2() userToken={},userId={},params={}", userToken, userId, params);
            return indexService.selectFloorsByV2_2(userId, address);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("indexV2_2() obj={} exception={}", params, e.getMessage());
            return new MessageDataBean(MessageDataBean.failure_code, e.getMessage()).toJsonString();
        }
	}

	/**
	 * 花积分页面楼层接口
	 *
	 * @author wuzhangyi
	 * @date 2018-11-11
	 * @param params
	 * @param request
	 * @return
	 */
	@POST
	@Path(value = "index/spendIntegral")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String spendIntegral(JSONObject params, @Context HttpServletRequest request) {
        String userToken = request.getHeader(Constants.TOKEN_NAME);
        logger.info("spendIntegral() userToken={}", userToken);
        if (StringUtils.isEmpty(userToken)) {
            return new MessageDataBean("1001", "userToken is null").toJsonString();
        }
        String userId = String.valueOf(redisTemplate.opsForValue().get(userToken));
        String address = params.getString("address");

        try {
            logger.info("spendIntegral() userToken={},userId={},params={}", userToken, userId,
                    params);
            return indexService.listSpendIntegralFloors(userId, address);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("spendIntegral() obj={} exception={}", params, e.getMessage());
            return new MessageDataBean(MessageDataBean.failure_code, e.getMessage()).toJsonString();
        }
	}
	
	/**
	 * 首页楼层查询
	 * 
	 * 接口v2：新增每日特惠
	 * 
	 * @author  hutao 
	 * @date 创建时间：2018年10月23日 下午4:13:36 
	 * @version 1.0 
	 * @parameter  
	 * @since  
	 * @return
	 */
	@POST
	@Path(value = "/index/v2")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String indexV2(JSONObject params, @Context HttpServletRequest request) {
		return indexService.selectFloorsByVersion(params, request, VersionConstants.INTERFACE_VERSION_V2);
	}

	/**
	 * 首页楼层查询
	 * 
	 * 接口v1
	 * 
	* @author  hutao 
	* @date 创建时间：2018年10月23日 下午4:15:10 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	@POST
	@Path(value = "/index")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String index(JSONObject params, @Context HttpServletRequest request) {
		return indexService.selectFloorsByVersion(params, request, VersionConstants.INTERFACE_VERSION_V1);
	}
}
