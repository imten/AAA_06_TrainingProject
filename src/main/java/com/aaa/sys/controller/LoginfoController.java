package com.aaa.sys.controller;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aaa.sys.common.DataGridView;
import com.aaa.sys.common.ResultObj;
import com.aaa.sys.domain.Loginfo;
import com.aaa.sys.service.LoginfoService;
import com.aaa.sys.vo.LoginfoVo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 */
@RestController
@RequestMapping("/loginfo")
public class LoginfoController {
	
	@Autowired
	private LoginfoService loginfoService;
	
	/**
	 * 全查询
	 */
	@RequestMapping("loadAllLoginfo")
	public DataGridView loadAllLoginfo(LoginfoVo loginfoVo) {
		IPage<Loginfo> page=new Page<>(loginfoVo.getPage(), loginfoVo.getLimit());
		QueryWrapper<Loginfo> queryWrapper=new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginname()),"loginname", loginfoVo.getLoginname());
		queryWrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginip()), "loginip",loginfoVo.getLoginip());
		queryWrapper.ge(loginfoVo.getStartTime()!=null, "logintime", loginfoVo.getStartTime());
		queryWrapper.le(loginfoVo.getEndTime()!=null, "logintime", loginfoVo.getEndTime());
		queryWrapper.orderByDesc("logintime");
		this.loginfoService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}
	
	
	/**
	 * 删除
	 */
	@RequestMapping("deleteLoginfo")
	public ResultObj deleteLoginfo(Integer id) {
		try {
			this.loginfoService.removeById(id);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}
	
	
	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteLoginfo")
	public ResultObj batchDeleteLoginfo(LoginfoVo loginfoVo) {
		try {
			Collection<Serializable> idList=new ArrayList<Serializable>();
			for (Integer id : loginfoVo.getIds()) {
				idList.add(id);
			}
			this.loginfoService.removeByIds(idList);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}
	/**
	 * 得到登陆验证码
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping("getCode")
	public void getCode(HttpServletResponse response, HttpSession session) throws IOException {
		//定义图形验证码的长和宽
		LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(116, 36,4,5);
		session.setAttribute("code",lineCaptcha.getCode());
		try {
			ServletOutputStream outputStream = response.getOutputStream();
			lineCaptcha.write(outputStream);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

