package com.chenxinzhi.file.dto;

import com.chenxinzhi.file.pojo.File_;
import lombok.Data;

@SuppressWarnings("Lombok")
@Data
public class FileDto extends File_ {
    private Long pageSize;
    private Long page;
    public void check(){
        if (page == null||page<1) {
            page= 1L;
        }
        if (pageSize == null||pageSize<1|| pageSize > 100) {
            pageSize=10L;
        }
    }
}
