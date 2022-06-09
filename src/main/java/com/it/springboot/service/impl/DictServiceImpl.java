package com.it.springboot.service.impl;

import com.it.springboot.entity.Dict;
import com.it.springboot.mapper.DictMapper;
import com.it.springboot.service.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XiHu
 * @since 2022-06-04
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

}
