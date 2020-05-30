package com.aaa.sys.controller;

import java.util.*;

import com.aaa.sys.common.*;
import com.aaa.sys.domain.Systeminfo;
import com.aaa.sys.domain.User;
import com.aaa.sys.service.SysteminfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aaa.sys.domain.Permission;
import com.aaa.sys.service.PermissionService;
import com.aaa.sys.vo.PermissionVo;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;
	@Autowired
	private SysteminfoService SysteminfoService;

	/**
	 * 系统日志记录
	 */
	public void systemLog(String type,String servicesname) {
		User us=(User)(WebUtils.getSession().getAttribute("user"));
		Systeminfo entity=new Systeminfo();
		entity.setLoginname(us.getName()+"-"+us.getLoginname());
		entity.setType(type);
		entity.setServicesname(servicesname);
		entity.setDateline(new Date());
		SysteminfoService.save(entity);
	}

	/**************** 权限管理开始 ****************/

	/**
	 * 加载权限管理左边的权限树的json
	 */
	@RequestMapping("loadPermissionManagerLeftTreeJson")
	public DataGridView loadPermissionManagerLeftTreeJson(PermissionVo permissionVo) {
		QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("type", Constast.TYPE_MNEU);
		List<Permission> list = this.permissionService.list(queryWrapper);
		List<TreeNode> treeNodes = new ArrayList<>();
		for (Permission permission : list) {
			Boolean spread = permission.getOpen() == 1 ? true : false;
			treeNodes.add(new TreeNode(permission.getId(), permission.getPid(), permission.getTitle(), spread));
		}
		return new DataGridView(treeNodes);
	}

	/**
	 * 查询
	 */
	@RequestMapping("loadAllPermission")
	public DataGridView loadAllPermission(PermissionVo permissionVo) {
		IPage<Permission> page = new Page<>(permissionVo.getPage(), permissionVo.getLimit());
		QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("type", Constast.TYPE_PERMISSION);// 只能查询权限
		queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()), "title", permissionVo.getTitle());
		queryWrapper.like(StringUtils.isNotBlank(permissionVo.getPercode()), "percode", permissionVo.getPercode());
		queryWrapper.eq(permissionVo.getId() != null,
				"pid", permissionVo.getId());
		queryWrapper.orderByAsc("ordernum");
		this.permissionService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}

	/**
	 * 加载最大的排序码
	 * 
	 * @param permissionVo
	 * @return
	 */
	@RequestMapping("loadPermissionMaxOrderNum")
	public Map<String, Object> loadPermissionMaxOrderNum() {
		Map<String, Object> map = new HashMap<String, Object>();

		QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("ordernum");
		IPage<Permission> page = new Page<>(1, 1);
		List<Permission> list = this.permissionService.page(page, queryWrapper).getRecords();
		if (list.size() > 0) {
			map.put("value", list.get(0).getOrdernum() + 1);
		} else {
			map.put("value", 1);
		}
		return map;
	}

	/**
	 * 添加
	 * 
	 * @param permissionVo
	 * @return
	 */
	@RequestMapping("addPermission")
	public ResultObj addPermission(PermissionVo permissionVo) {
		try {
			permissionVo.setType(Constast.TYPE_PERMISSION);// 设置添加类型
			this.permissionService.save(permissionVo);
			systemLog("添加","添加权限（"+permissionVo.getTitle()+")");
			return ResultObj.ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.ADD_ERROR;
		}
	}

	/**
	 * 修改
	 * 
	 * @param permissionVo
	 * @return
	 */
	@RequestMapping("updatePermission")
	public ResultObj updatePermission(PermissionVo permissionVo) {
		try {
			this.permissionService.updateById(permissionVo);
			systemLog("修改","修改权限（"+permissionVo.getTitle()+")");
			return ResultObj.UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.UPDATE_ERROR;
		}
	}

	/**
	 * 删除
	 * 
	 * @param permissionVo
	 * @return
	 */
	@RequestMapping("deletePermission")
	public ResultObj deletePermission(PermissionVo permissionVo) {
		try {
			this.permissionService.removeById(permissionVo.getId());
			systemLog("删除","删除权限（"+permissionVo.getTitle()+")");
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}

	/**************** 权限管理结束 ****************/

}
