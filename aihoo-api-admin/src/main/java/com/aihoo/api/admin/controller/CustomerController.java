package com.aihoo.api.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.aihoo.api.admin.controller.vo.CustomerVo;
import com.aihoo.api.admin.controller.vo.MdtFqVo;
import com.aihoo.domain.im.model.entity.ImCustomerMsg;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.domain.sys.service.CustomerService;
import com.aihoo.domain.consultation.service.MdtFqService;
import com.aihoo.common.BaseController;
import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import com.alibaba.fastjson2.JSONArray;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Classname CustomerController
 * @Description 客服
 * @Date 2020/11/10 11:05
 * @Created by ad
 */

@RestController
@SuppressWarnings({"unchecked", "rawtypes"})
@RequestMapping("/api/v1/customer")
public class CustomerController extends BaseController {


    @Resource
    private CustomerService customerService;
	    
	@Resource
	private MdtFqService mdtFqService;
    

    /**
     * 客服列表 （其实就是拥有客服权限的系统用户）
     * @param map 入参
     * @return {}
     */
    @PostMapping("/customerList")
    public PageResult<SysUser> customerList(@RequestBody Map<String,Object> map){
        try {
            if (StringUtils.isEmpty(map.get("page"))){
                map.put("page", "1");
            }
            if (StringUtils.isEmpty(map.get("limit"))){
                map.put("limit", "10");
            }
            return (PageResult) (Object) this.customerService.customerList(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("客服列表查询出错");
        }
    }

    /**
     *
     * 查看某个客服的所有患者列表
     * @param map 入参
     * @return {}
     */
    @PostMapping("/customer/details")
    public PageResult<CustomerVo> customerDetails(@RequestBody Map<String,Object> map){
        try {
            return (PageResult) (Object) this.customerService.customerDetails(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("客服与患者列表查询出错");
        }
    }

    /**
     *
     * 查看某个客服的聊天记录
     * @param map 入参
     * @return {}
     */
    @PostMapping("/chattingRecords")
    public JsonResult chattingRecords(@RequestBody Map<String,Object> map){
        try {
            if (StringUtils.isEmpty(map.get("patientId"))){
                return error("必须携带患者id");
            }
            if (StringUtils.isEmpty(map.get("adminId"))){
                return error("必须携带客服id");
            }
            List<ImCustomerMsg> imCustomerMsgs = (List<ImCustomerMsg>) (Object) this.customerService.chattingRecords(map);
            return ok("查询成功").put("data",imCustomerMsgs);
        } catch (Exception e) {
            e.printStackTrace();
            return error("聊天记录查询出错");
        }
    }
    
    /**
     * 没有被删除，启用， 返回：id,name，price，入参：name
     * @param map
     * @return
     */
    @PostMapping("/fuzzyQueryMdt")
    public PageResult<MdtFqVo> fuzzyQueryMdt(@RequestBody Map<String,Object> map){
    	try {
	    	if (map.get("name")==null) {
	    		 return new PageResult<>("缺少参数：name");
			}	
    	 return (PageResult) (Object) mdtFqService.fuzzQueryMdt(map);
		} catch (Exception e) {
			 e.printStackTrace();
			 return new PageResult<>("模糊查询mdt出错");
		}
    }

    /**
     * mdt关键字搜索-> 医生、医院、团队、疾病
     * 条件搜索
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/search")
    public JsonResult search(@RequestBody Map<String, Object> map) {
        try {
            List data = (List) (Object) customerService.search(map);
            return JsonResult.ok().put("data", CollUtil.isEmpty(data) ? new JSONArray() : data);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("MDT搜索出错");
        }
    }

    /**
     * mdt标签和子标签
     *
     *
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/mdtTag")
    public JsonResult mdtTag() {
        try {
            JSONArray mdtList = (JSONArray) (Object) customerService.mdtList();
            return JsonResult.ok().put("data", mdtList);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("MDT疾病查询出错");
        }
    }


}
