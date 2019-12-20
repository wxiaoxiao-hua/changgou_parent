package com.changgou.file.api;

import com.changgou.common.entity.Result;
import io.swagger.annotations.*;
import org.springframework.web.multipart.MultipartFile;


@Api(value="图片,文件管理接口",description = "图片,文件管理接口，提供文件的上传,下载")
public interface FileApi {

    @ApiOperation("上传图片")
    @ApiImplicitParams({
           // @ApiImplicitParam(name="file",value = "文件",required=true,paramType="form",dataType="file")
    })
    public Result uploadFile(@ApiParam(value = "file", required = true)MultipartFile file);
}
