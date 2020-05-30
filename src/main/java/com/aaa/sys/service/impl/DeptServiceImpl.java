package com.aaa.sys.service.impl;

import java.io.Serializable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aaa.sys.domain.Dept;
import com.aaa.sys.mapper.DeptMapper;
import com.aaa.sys.service.DeptService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
@Transactional
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {
	
	@Override
	public Dept getById(Serializable id) {
		return super.getById(id);
	}
	
	@Override
	public boolean updateById(Dept entity) {
		return super.updateById(entity);
	}
	
	@Override
	public boolean removeById(Serializable id) {
		return super.removeById(id);
	}
	
	@Override
	public boolean save(Dept entity) {
		return super.save(entity);
	}

}
