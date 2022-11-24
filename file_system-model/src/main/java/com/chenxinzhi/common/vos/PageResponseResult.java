package com.chenxinzhi.common.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@ApiModel("分页请求的响应数据")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseResult<T> extends ResponseResult<T> implements Serializable {
    @ApiModelProperty("当前页")
    private Long currentPage;
    @ApiModelProperty("页长")
    private Long pageSize;
    @ApiModelProperty("总页数")
    private Long total;

}
