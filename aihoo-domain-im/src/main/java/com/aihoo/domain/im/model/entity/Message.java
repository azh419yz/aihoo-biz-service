package com.aihoo.domain.im.model.entity;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * <p>
 * 发布信息表
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
@TableName("t_message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 用户id
     */
    private String wechatUserId;

    /**
     * 发布信息名称
     */
    private String name;

    /**
     * 交易方式
     */
    private String payType;

    /**
     * 富文本内容
     */
    private String content;

    /**
     * 国家CODE
     */
    private String countryCode;

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 地区CODE
     */
    private String cityCode;

    /**
     * 地区名称
     */
    private String cityName;

    /**
     * 街区CODE
     */
    private String blocksCode;

    /**
     * 街区名称
     */
    private String blocksName;

    /**
     * 详情地址
     */
    private String address;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 浏览量
     */
    private String browseCount;

    /**
     * 是否上架 1:上架 0:下架
     */
    private String isListing;

    /**
     * 是否删除 1:已删除 0:可用
     */
    private String isDelete;

    /**
     * 审核状态 WAIT:待审核 PASS:审核通过 REJECT:审核驳回
     */
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWechatUserId() {
        return wechatUserId;
    }

    public void setWechatUserId(String wechatUserId) {
        this.wechatUserId = wechatUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getBlocksCode() {
        return blocksCode;
    }

    public void setBlocksCode(String blocksCode) {
        this.blocksCode = blocksCode;
    }

    public String getBlocksName() {
        return blocksName;
    }

    public void setBlocksName(String blocksName) {
        this.blocksName = blocksName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(String browseCount) {
        this.browseCount = browseCount;
    }

    public String getIsListing() {
        return isListing;
    }

    public void setIsListing(String isListing) {
        this.isListing = isListing;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Message{" +
        ", id=" + id +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", wechatUserId=" + wechatUserId +
        ", name=" + name +
        ", payType=" + payType +
        ", content=" + content +
        ", countryCode=" + countryCode +
        ", countryName=" + countryName +
        ", cityCode=" + cityCode +
        ", cityName=" + cityName +
        ", blocksCode=" + blocksCode +
        ", blocksName=" + blocksName +
        ", address=" + address +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        ", browseCount=" + browseCount +
        ", isListing=" + isListing +
        ", isDelete=" + isDelete +
        ", state=" + state +
        "}";
    }
}
