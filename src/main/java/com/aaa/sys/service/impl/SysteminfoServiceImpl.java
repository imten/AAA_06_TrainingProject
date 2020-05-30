package com.aaa.sys.service.impl;

import com.aaa.sys.domain.Systeminfo;
import com.aaa.sys.mapper.SysteminfoMapper;
import com.aaa.sys.service.SysteminfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
@Transactional
public class SysteminfoServiceImpl extends ServiceImpl<SysteminfoMapper, Systeminfo> implements SysteminfoService {

}
