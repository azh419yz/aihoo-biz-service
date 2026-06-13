package com.aihoo.api.admin.controller;

import com.aihoo.domain.sys.model.entity.SysMenu;
import com.aihoo.domain.sys.service.SysMenuService;
import com.aihoo.common.BaseController;
import com.aihoo.common.JsonResult;
import com.aihoo.common.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
@RestController
@SuppressWarnings({"unchecked", "rawtypes"})
@RequestMapping("/api/v1/sys/menu")
public class SysMenuController extends BaseController {

    @Resource
    private SysMenuService sysMenuService;

/*
    @RequiresPermissions("menu:view")
    @RequiresRoles("admin")
    @RequestMapping()
    public String authorities(Model model) {
        List<SysMenu> authorities = sysMenuService.list(new QueryWrapper<SysMenu>().eq("is_menu", 0).orderByAsc("order_number"));
        model.addAttribute("authorities", authorities);
        return "system/menu.html";
    }
*/

    /**
     * 查询所有权限
     **/
    @RequestMapping("/list")
    public PageResult<SysMenu> list() {
        try {
//            long page = 1;
//            long limit = 10;
//            QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
//            wrapper.orderByDesc("order_number");
//            wrapper.eq("deleted",0);
//            IPage<SysMenu> iPage = sysMenuService.page(new Page<>(page, limit), wrapper);
//            return new PageResult<>(iPage.getRecords(),iPage.getTotal());
        	  return sysMenuService.getPage();
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<>("列表查询出错");
        }
    }

    /**
     * 添加权限
     */
    @RequestMapping("/add")
    public JsonResult add(@RequestBody SysMenu authorities) {
        try {
            if (null == authorities.getMenuName() || authorities.getMenuName().equals("")){
                return error("权限名称必填");
            }
            if (null == authorities.getParentId() || authorities.getParentId().equals("")){
                return error("父id必传");
            }
            if (null == authorities.getIsMenu() || authorities.getIsMenu().equals("")){
                return error("权限类型必填");
            }
            if (sysMenuService.saveDto(authorities)) {
                return ok("添加成功");
            }
            return error("添加失败");
        } catch (Exception e) {
            e.printStackTrace();
            return error("添加失败");
        }
    }

    /**
     * 修改权限
     */
    @RequestMapping("/update")
    public JsonResult update(@RequestBody SysMenu authorities) {
        try {
            if (null == authorities.getId()){
                return error("id必传");
            }
            if (null != authorities.getDeleted()){
                authorities.setDeleted("0");
            }
            if (sysMenuService.updateByIdDto(authorities)) {
                return JsonResult.ok("修改成功");
            }
            return JsonResult.error("修改失败");
        } catch (Exception e) {
            e.printStackTrace();
            return error("修改权限出错");
        }
    }

    /**
     * 删除权限
     */
    @RequestMapping("/delete")
    public JsonResult delete(@RequestBody Map<String,Object> map) {
        try {
            if (null == map.get("id") || "".equals(map.get("id"))){
                return error("必须携带id");
            }
//            QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
//            wrapper.eq("id",map.get("id"));
//            SysMenu sysMenu = new SysMenu();
//            sysMenu.setDeleted("1");
//            if (sysMenuService.update(sysMenu,wrapper)) {
//                return ok("删除成功");
//            }
            
          if (sysMenuService.updateDto(map)) {
        	  return ok("删除成功");
          }
            return error("删除失败");
            
        } catch (Exception e) {
            e.printStackTrace();
            return error("删除权限出错");
        }
    }
}
