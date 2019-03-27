package com.doooly.service.doooly;

import com.doooly.business.product.entity.AdSelfProductSku;
import com.doooly.entity.doooly.DlTemplateFloorItem;

import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.Wu
 * @Date: 2019/3/21
 */
public interface TemplateCacheServiceI {

    /**
     * 获得标准楼层items
     * @param map
     *      floor: 楼层id
     * @return
     */
    public List<DlTemplateFloorItem> getStandardFloorItem(Map<String, Object> map);

    /**
     * 获得自营商品信息
     * @param map
     *      skuId: 自营商品SKU Id
     * @return
     */
    public AdSelfProductSku getSelfProductSku(Map<String, Object> map);

    /**
     * 获得热门商户楼层并缓存
     * @param map
     *      userId
     *      address
     *      groupId
     * @return
     */
    public List<DlTemplateFloorItem> getHotBusinessFloor(Map<String, Object> map);

    /**
     * 获得广告位楼层items
     * @param map
     *      groupId: 企业id
     *      type: 广告类型
     *      version: 广告显示版本
     * @return
     */
    public List<DlTemplateFloorItem> getAdAdItemsByTypeAndGroup(Map<String, Object> map);

    /**
     * 获得礼包楼层items
     *
     * @param map
     *      groupId
     * @return
     */
    public Map<String, Object> getDooolyGiftBagItemsByGroup(Map<String, Object> map);
}
