package com.aaa.sys.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.aaa.sys.domain.Systeminfo;
import com.aaa.sys.service.SysteminfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aaa.sys.common.DataGridView;
import com.aaa.sys.common.ResultObj;
import com.aaa.sys.common.WebUtils;
import com.aaa.sys.domain.Notice;
import com.aaa.sys.domain.User;
import com.aaa.sys.service.NoticeService;
import com.aaa.sys.vo.NoticeVo;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
	
	@Autowired
	private NoticeService noticeService;
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
	 * 查询
	 */
	@RequestMapping("loadAllNotice")
	public DataGridView loadAllNotice(NoticeVo noticeVo) {
		IPage<Notice> page=new Page<>(noticeVo.getPage(), noticeVo.getLimit());
		QueryWrapper<Notice> queryWrapper=new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(noticeVo.getTitle()), "title", noticeVo.getTitle());
		queryWrapper.like(StringUtils.isNotBlank(noticeVo.getOpername()), "opername", noticeVo.getOpername());
		queryWrapper.ge(noticeVo.getStartTime()!=null, "createtime", noticeVo.getStartTime());
		queryWrapper.le(noticeVo.getEndTime()!=null, "createtime", noticeVo.getEndTime());
		queryWrapper.orderByDesc("createtime");
		this.noticeService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}
	
	
	/**
	 * 添加
	 */
	@RequestMapping("addNotice")
	public ResultObj addNotice(NoticeVo noticeVo) {
		try {
			noticeVo.setCreatetime(new Date());
			User user = (User) WebUtils.getSession().getAttribute("user");
			noticeVo.setOpername(user.getName());
			systemLog("添加","添加通知（"+noticeVo.getTitle()+")");
			this.noticeService.save(noticeVo);
			return ResultObj.ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.ADD_ERROR;
		}
	}
	/**
	 * 修改
	 */
	@RequestMapping("updateNotice")
	public ResultObj updateNotice(NoticeVo noticeVo) {
		try {
			this.noticeService.updateById(noticeVo);
			systemLog("修改","修改通知（"+noticeVo.getTitle()+")");
			return ResultObj.UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.UPDATE_ERROR;
		}
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("deleteNotice")
	public ResultObj deleteNotice(Integer id) {
		try {
			this.noticeService.removeById(id);
			systemLog("删除","删除通知（"+id+")");
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}
	
	
	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteNotice")
	public ResultObj batchDeleteNotice(NoticeVo noticeVo) {
		try {
			Collection<Serializable> idList=new ArrayList<Serializable>();
			for (Integer id : noticeVo.getIds()) {
				idList.add(id);
			}
			this.noticeService.removeByIds(idList);
			systemLog("删除","批量删除通知");
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}
}

