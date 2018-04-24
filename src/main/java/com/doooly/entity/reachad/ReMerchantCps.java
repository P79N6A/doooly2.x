package com.doooly.entity.reachad;

/**
 * re_merchant_cps商家营销表POJO类
 * 
 * @author tangzhiyuan
 *
 */
public class ReMerchantCps {

	private Long id;
	private Long adBusinessId;
	// '是否设置(0:未设置;1:已设置)',
	private int setStatus;
	// '营销属性(0:固定价格;1:品类返点)',
	private int cpsStatus;

	public ReMerchantCps() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAdBusinessId() {
		return adBusinessId;
	}

	public void setAdBusinessId(Long adBusinessId) {
		this.adBusinessId = adBusinessId;
	}

	public int getSetStatus() {
		return setStatus;
	}

	public void setSetStatus(int setStatus) {
		this.setStatus = setStatus;
	}

	public int getCpsStatus() {
		return cpsStatus;
	}

	public void setCpsStatus(int cpsStatus) {
		this.cpsStatus = cpsStatus;
	}
}
