package com.it.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author XiHu
 * @since 2022-06-02
 */
@Data
@TableName("sys_file")
public class Files implements Serializable {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("文件大小(kb)")
    private Long size;

    @ApiModelProperty("下载链接")
    private String url;

    @ApiModelProperty("文件md5")
    private String md5;

    @ApiModelProperty("是否删除")
    private Boolean isDelete;

    @ApiModelProperty("是否禁用链接")
    private Boolean enable;
}
