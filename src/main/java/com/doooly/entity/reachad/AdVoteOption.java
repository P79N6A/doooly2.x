package com.doooly.entity.reachad;

/**
 * @Description: 投票选项
 * @author: qing.zhang
 * @date: 2017-05-31
 */
public class AdVoteOption {
    private int id;
    private int sortNum;// 排序号
    private String optionName;// 选项名称
    private int voteCount;//投票数
    private String imageUrl;// 选项图片地址
    private String optionContent;// 选项文字内容
    private int isVote;//改用户是否给该选项投过票 1投过 ， 2，没有
    private Integer activityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsVote() {
        return isVote;
    }

    public void setIsVote(int isVote) {
        this.isVote = isVote;
    }

    public String getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
    
}
