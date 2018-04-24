package com.doooly.entity.reachad;
/**
 * 
    * @ClassName: AdBlocGroupLogin  
    * @Description: 集团企业快捷登录配置表  
    * @author hutao  
    * @date 2017年10月19日  
    *
 */
public class AdBlocGroupLogin {
	private Integer blocId;//集团公司ID，关联ad_bloc
	private String blocChannel;//集团企业渠道标识，用于与企业接口传输（唯一性）
	private String secretKey;//信任密钥
	private Integer actionPeriod;//登录验证有效最大秒数，单位秒
	private String blackGroupName;//黑名单员工存放企业名称
	
	public AdBlocGroupLogin() {
		super();
	}
	public AdBlocGroupLogin(Integer blocId, String blocChannel, String secretKey, Integer actionPeriod,
			String blackGroupName) {
		super();
		this.blocId = blocId;
		this.blocChannel = blocChannel;
		this.secretKey = secretKey;
		this.actionPeriod = actionPeriod;
		this.blackGroupName = blackGroupName;
	}
	public Integer getBlocId() {
		return blocId;
	}
	public void setBlocId(Integer blocId) {
		this.blocId = blocId;
	}
	public String getBlocChannel() {
		return blocChannel;
	}
	public void setBlocChannel(String blocChannel) {
		this.blocChannel = blocChannel;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public Integer getActionPeriod() {
		return actionPeriod;
	}
	public void setActionPeriod(Integer actionPeriod) {
		this.actionPeriod = actionPeriod;
	}
	public String getBlackGroupName() {
		return blackGroupName;
	}
	public void setBlackGroupName(String blackGroupName) {
		this.blackGroupName = blackGroupName;
	}
}
