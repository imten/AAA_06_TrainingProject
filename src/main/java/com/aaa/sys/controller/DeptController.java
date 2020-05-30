package com.aaa.sys.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.aaa.sys.domain.Dept;
import com.aaa.sys.service.DeptService;
import com.aaa.sys.vo.DeptVo;

/**
 * <p>
 *  前端控制器
 * </p>
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

	@Autowired
	private DeptService deptService;
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

	/**
	 * 加载部门管理左边的部门树的json
	 */
	@RequestMapping("loadDeptManagerLeftTreeJson")
	public DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo) {
		List<Dept> list = this.deptService.list();
		List<TreeNode> treeNodes=new ArrayList<>();
		for (Dept dept : list) {
			Boolean spread=dept.getOpen()==1?true:false;
			treeNodes.add(new TreeNode(dept.getId(), dept.getPid(), dept.getTitle(), spread));
		}
		return new DataGridView(treeNodes);
	}
	
	/**
	 * 查询
	 */
	@RequestMapping("loadAllDept")
	public DataGridView loadAllDept(DeptVo deptVo) {
		IPage<Dept> page=new Page<>(deptVo.getPage(), deptVo.getLimit());
		QueryWrapper<Dept> queryWrapper=new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(deptVo.getTitle()), "title", deptVo.getTitle());
		queryWrapper.like(StringUtils.isNotBlank(deptVo.getAddress()), "address", deptVo.getAddress());
		queryWrapper.like(StringUtils.isNotBlank(deptVo.getRemark()), "remark", deptVo.getRemark());
		queryWrapper.eq(deptVo.getId()!=null, "id", deptVo.getId()).or().eq(deptVo.getId()!=null,"pid", deptVo.getId());
		queryWrapper.orderByAsc("ordernum");
		this.deptService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}
	
	/**
	 * 加载最大的排序码
	 * @return
	 */
	@RequestMapping("loadDeptMaxOrderNum")
	public Map<String,Object> loadDeptMaxOrderNum(){
		Map<String, Object> map=new HashMap<String, Object>();
		
		QueryWrapper<Dept> queryWrapper=new QueryWrapper<>();
		queryWrapper.orderByDesc("ordernum");
		IPage<Dept> page=new Page<>(1, 1);
		List<Dept> list = this.deptService.page(page, queryWrapper).getRecords();
		if(list.size()>0) {
			map.put("value", list.get(0).getOrdernum()+1);
		}else {
			map.put("value", 1);
		}
		return map;
	}


	/**
	 * 添加
	 * @param deptVo
	 * @return
	 */
	@RequestMapping("addDept")
	public ResultObj addDept(DeptVo deptVo) {
		try {
			deptVo.setCreatetime(new Date());
			this.deptService.save(deptVo);
			systemLog("添加","添加部门（"+deptVo.getTitle()+")");
			return ResultObj.ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.ADD_ERROR;
		}
	}
	

	/**
	 * 修改
	 * @param deptVo
	 * @return
	 */
	@RequestMapping("updateDept")
	public ResultObj updateDept(DeptVo deptVo) {
		try {
			this.deptService.updateById(deptVo);
			systemLog("修改","修改部门（"+deptVo.getTitle()+")");
			return ResultObj.UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.UPDATE_ERROR;
		}
	}
	
	
	/**
	 * 查询当前的ID的部门有没有子部门
	 */
	@RequestMapping("checkDeptHasChildrenNode")
	public Map<String,Object> checkDeptHasChildrenNode(DeptVo deptVo){
		Map<String, Object> map=new HashMap<String, Object>();
		
		QueryWrapper<Dept> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("pid", deptVo.getId());
		List<Dept> list = this.deptService.list(queryWrapper);
		if(list.size()>0) {
			map.put("value", true);
		}else {
			map.put("value", false);
		}
		return map;
	}
	
	/**
	 * 删除
	 * @param deptVo
	 * @return
	 */
	@RequestMapping("deleteDept")
	public ResultObj deleteDept(DeptVo deptVo) {
		try {
			this.deptService.removeById(deptVo.getId());
			systemLog("删除","删除部门（"+deptVo.getTitle()+")");
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}
}

