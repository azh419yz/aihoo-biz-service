package com.aihoo.domain.payment.service;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.MdtOrder;
import com.aihoo.domain.payment.model.request.SearchMdtOrderRequest;
import com.aihoo.domain.payment.model.vo.MdtOrderTradeInfoVo;
import com.aihoo.domain.payment.model.vo.MdtOrderVo;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * @Classname MdtOrderService
 * @Description hf
 * @Date 2020/9/30 13:19
 * @Created by ad
 */
public interface MdtOrderService extends IService<MdtOrder> {
    PageResult<MdtOrderVo> getPage(PageParam<MdtOrder> pageParam, SearchMdtOrderRequest request);

    Long count(List<String> statusList);

    /**
     * 分页
     *
     * @param param
     * @return
     * @throws Exception
     */
    PageResult<MdtOrderTradeInfoVo> page(Map<String, Object> param) throws Exception;

    void mdtOrderOutExecl(Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 打印PDF
     *
     * @param orderId 订单ID
     * @return pdf url
     */
    String printPdf(String orderId);

    /**
     * 打印快递标签
     *
     * @param orderId 订单ID
     * @return 操作结果
     */
    boolean printExpress(String orderId);

    /**
     * 完成调配
     *
     * @param orderId 订单ID
     * @return 操作结果
     */
    boolean completeAllocation(String orderId);

    /**
     * 保存备注和图片
     *
     * @param orderId 订单ID
     * @param remark  药师备注
     * @param picList 图片URL列表
     * @return 操作结果
     */
    boolean saveRemarkAndPic(String orderId, String remark, List<String> picList);


    void export(SearchMdtOrderRequest orderRequest, HttpServletRequest request, HttpServletResponse response);

    void drugExport(SearchMdtOrderRequest orderRequest, HttpServletRequest request, HttpServletResponse response);

}