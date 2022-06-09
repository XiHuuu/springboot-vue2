package com.it.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.it.springboot.entity.Files;
import com.it.springboot.mapper.FileMapper;
import com.it.springboot.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XiHu
 * @since 2022-06-02
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements IFileService {
//    @Resource
//    private FileMapper fileMapper;
//
//    @Override
//    public int insert(Files saveFile) {
//        return fileMapper.insert(saveFile);
//    }
//
//    @Override
//    public List<Files> selectList(QueryWrapper<Files> queryWrapper) {
//        return fileMapper.selectList(queryWrapper);
//    }


}
