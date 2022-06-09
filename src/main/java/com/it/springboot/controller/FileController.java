package com.it.springboot.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.springboot.common.Result;
import com.it.springboot.entity.Files;
import com.it.springboot.service.IFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author XiHu
 * @since 2022-06-02
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Value("${server.ip}")
    private String serverIp;

    @Resource
    private IFileService fileService;


    /**
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
//    @PostMapping("/upload")
//    public String upload(@RequestParam MultipartFile file) throws IOException {
//        String originalFilename = file.getOriginalFilename();
//        String type = FileUtil.extName(originalFilename);
//        long size = file.getSize();
//
//        //定义一个文件唯一的标识码
//        String uuid = IdUtil.fastSimpleUUID();
//        String fileUUID = uuid + StrUtil.DOT + type;
//        File uploadFile = new File(fileUploadPath + fileUUID);
//        //判断配置的文件目录是否存在，若不存在创建一个新的目录
//        if (!uploadFile.getParentFile().exists()){
//            uploadFile.getParentFile().mkdirs();
//        }
//
//
//        //获取文件的md5
//        String md5 = SecureUtil.md5(file.getInputStream());
//        Files files = getFileByMd5(md5);
//
//        //获取文件的url
//        String url;
//        if (files != null){
//            url = files.getUrl();
//        }else {
//            //把获取到的文件存储到磁盘目录
//            file.transferTo(uploadFile);
//            url = "http://"+serverIp+":9090/file/" + fileUUID;
//        }
//
//
//        //存储数据库
//        Files saveFile = new Files();
//        saveFile.setName(originalFilename);
//        saveFile.setType(type);
//        saveFile.setSize(size/1024);
//        saveFile.setUrl(url);
//        saveFile.setMd5(md5);
//        fileService.getBaseMapper().insert(saveFile);
//        return url;
//    }
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://" + serverIp + ":9090/file/" + fileUUID;
        }


        // 存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024); // 单位 kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileService.getBaseMapper().insert(saveFile);
        return url;
    }



    /**
     * 文件下载接口   http://localhost:9090/file/{fileUUID}
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");
        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5){
        //查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<Files> filesList = fileService.getBaseMapper().selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        return Result.success(fileService.getBaseMapper().updateById(files));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files files = fileService.getBaseMapper().selectById(id);
        files.setIsDelete(true);
        fileService.updateById(files);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> files = fileService.getBaseMapper().selectList(queryWrapper);
        for (Files file : files) {
            file.setIsDelete(true);
            fileService.getBaseMapper().updateById(file);
        }
        return Result.success();
    }

    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {

        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // 查询未删除的记录
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(fileService.getBaseMapper().selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

