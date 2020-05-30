package com.aaa.sys.service.impl;

import com.aaa.sys.domain.Role;
import com.aaa.sys.mapper.RoleMapper;
import com.aaa.sys.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

	
	@Override
	public boolean removeById(Serializable id) {
		//根据角色ID删除tbl_role_permission
		this.getBaseMapper().deleteRolePermissionByRid(id);
		//根据角色ID删除tbl_role_user
		this.getBaseMapper().deleteRoleUserByRid(id);
		return super.removeById(id);
	}

	/**
	 * 根据角色ID查询当前角色拥有的所有的权限或菜单ID
	 */
	@Override
	public List<Integer> queryRolePermissionIdsByRid(Integer roleId) {
		return this.getBaseMapper().queryRolePermissionIdsByRid(roleId);
	}

	/**
	 * 保存角色和菜单权限之间的关系
	 */
	@Override
	public void saveRolePermission(Integer rid, Integer[] ids) {
		RoleMapper roleMapper = this.getBaseMapper();
		//根据rid删除tbl_role_permission
		roleMapper.deleteRolePermissionByRid(rid);
		if(ids!=null&&ids.length>0) {
			for (Integer pid : ids) {
				roleMapper.saveRolePermission(rid,pid);
			}
		}
	}

	/**
	 * 查询当前用户拥有的角色ID集合
	 */
	@Override
	public List<Integer> queryUserRoleIdsByUid(Integer id) {
		return this.getBaseMapper().queryUserRoleIdsByUid(id);
	}
}
