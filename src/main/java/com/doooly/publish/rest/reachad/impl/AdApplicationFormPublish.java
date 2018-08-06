package com.doooly.publish.rest.reachad.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dao.reachad.AdApplicationFormDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdApplicationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by WANG on 2018/7/27.
 */
@Component
@Path("/applicationFormService")
public class AdApplicationFormPublish {

    @Autowired
    protected AdApplicationFormDao adApplicationFormDao;

    /***
     * 企业申请
     * @param params
     * @return
     */
    @POST
    @Path(value = "/enterpriseForm")
    @Produces(MediaType.APPLICATION_JSON)
    public String enterpriseForm(JSONObject params) {
        AdApplicationForm form = new AdApplicationForm();
        form.setEnterpriseName(params.getString("enterpriseName"));
        form.setEnterpriseScale(params.getString("enterpriseScale"));
        form.setApplicationProducts(params.getString("applicationProducts"));
        form.setName(params.getString("name"));
        form.setPhone(params.getString("phone"));
        form.setJob(params.getString("job"));
        form.setEmail(params.getString("email"));
        form.setRemarks(params.getString("remarks"));
        form.setType(1);
        int i = adApplicationFormDao.insert(form);
        if (i > 0) {
            return new MessageDataBean(MessageDataBean.success_code, "success").toJsonString();
        } else {
            return new MessageDataBean(MessageDataBean.failure_code, "failed").toJsonString();
        }
    }

    /***
     * 供应商申请
     * @param params
     * @return
     */
    @POST
    @Path(value = "/supplierForm")
    @Produces(MediaType.APPLICATION_JSON)
    public String supplierForm(JSONObject params) {
        AdApplicationForm form = new AdApplicationForm();
        form.setEnterpriseName(params.getString("enterpriseName"));
        form.setName(params.getString("name"));
        form.setPhone(params.getString("phone"));
        form.setJob(params.getString("job"));
        form.setEmail(params.getString("email"));
        form.setRemarks(params.getString("remarks"));
        form.setType(2);
        int i = adApplicationFormDao.insert(form);
        if (i > 0) {
            return new MessageDataBean(MessageDataBean.success_code, "success").toJsonString();
        } else {
            return new MessageDataBean(MessageDataBean.failure_code, "failed").toJsonString();
        }
    }
}
