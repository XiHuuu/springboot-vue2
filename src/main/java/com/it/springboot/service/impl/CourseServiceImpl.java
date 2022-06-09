package com.it.springboot.service.impl;

import com.it.springboot.entity.Course;
import com.it.springboot.mapper.CourseMapper;
import com.it.springboot.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XiHu
 * @since 2022-06-09
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

}
