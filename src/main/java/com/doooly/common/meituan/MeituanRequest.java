package com.doooly.common.meituan;

import com.business.common.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wanghai on 2018/12/11.
 */
public class MeituanRequest {

    private static Logger logger = LoggerFactory.getLogger(MeituanRequest.class);

    private String token = MeituanConstants.TOKEN;

    private String content;

    private String version = MeituanConstants.VERSION;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String doPost(String url) throws Exception{
        logger.info("content : {}",content);
        content = new SimpleStringCypher().encrypt(content);
        url = url + "?token="+token+
                "&content="+content+"&version="+version;
        String ret = HttpClientUtil.doPost(url,"");
        logger.info("post : {},返回结果：{}",url,ret);
        return ret;
    }

}

