package com.aaa.sys.controller;


import com.aaa.sys.common.DataGridView;
import com.aaa.sys.common.ResultObj;
import com.aaa.sys.domain.Loginfo;
import com.aaa.sys.domain.Systeminfo;
import com.aaa.sys.service.LoginfoService;
import com.aaa.sys.service.SysteminfoService;
import com.aaa.sys.vo.LoginfoVo;
import com.aaa.sys.vo.SysteminfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 *  前端控制器
 * </p>
 */
@RestController
@RequestMapping("/systeminfo")
public class SysteminfoController {

	@Autowired
	private SysteminfoService SysteminfoService;
	
	/**
	 * 全查询
	 */
	@RequestMapping("loadAllSysteminfo")
	public DataGridView loadAllLoginfo(SysteminfoVo systeminfoVo) {
		IPage<Systeminfo> page=new Page<>(systeminfoVo.getPage(), systeminfoVo.getLimit());
		QueryWrapper<Systeminfo> queryWrapper=new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(systeminfoVo.getLoginname()),"loginname", systeminfoVo.getLoginname());
		queryWrapper.like(StringUtils.isNotBlank(systeminfoVo.getServicesname()), "servicesname",systeminfoVo.getServicesname());
		queryWrapper.ge(systeminfoVo.getStartTime()!=null, "dataline", systeminfoVo.getStartTime());
		queryWrapper.le(systeminfoVo.getEndTime()!=null, "dateline", systeminfoVo.getEndTime());
		queryWrapper.orderByDesc("dateline");
		this.SysteminfoService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}


	/**
	 * 删除
	 */
	@RequestMapping("deleteSysteminfo")
	public ResultObj deleteLoginfo(Integer id) {
		try {
			this.SysteminfoService.removeById(id);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}


	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteSysteminfo")
	public ResultObj batchDeleteLoginfo(LoginfoVo loginfoVo) {
		try {
			Collection<Serializable> idList=new ArrayList<Serializable>();
			for (Integer id : loginfoVo.getIds()) {
				idList.add(id);
			}
			this.SysteminfoService.removeByIds(idList);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}

}

