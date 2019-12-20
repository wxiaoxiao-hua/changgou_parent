package com.changgou.file.controller;

import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.file.api.FileApi;
import com.changgou.file.util.FastDFSClient;
import com.changgou.file.util.FastDFSFile;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController implements FileApi{

    // 上传文件
    @PostMapping("/upload")
    public Result uploadFile(MultipartFile file){ // 获取前端传送过来的文件
       try{
           // 获取文件的完整的名称
           String originalFilename = file.getOriginalFilename();
           if(StringUtils.isEmpty(originalFilename)){
               throw new RuntimeException("文件不存在");
           }
           // 获取文件的拓展名,从最后一个.号开始,索引向后面加一位,获取到拓展名
           String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
           // 获取文件的内容
           byte[] content = file.getBytes();
           // 创建文件上传的封装实体类
           FastDFSFile fastDFSFile = new FastDFSFile(originalFilename,content,extName);

           // 是用工具类上传的,要接收它返回的参数
           String[] uploadResult = FastDFSClient.upload(fastDFSFile);
           // 封装返回的结果,FastDFSClient.getTrackerUrl()是tracker的服务地址,
           // uploadResult[0] 是组名,uploadResult[1]是存储文件的路径
           String url = FastDFSClient.getTrackerUrl()+uploadResult[0]+"/"+uploadResult[1];

           return new Result(true, StatusCode.OK,"文件上传成功",url);

       }catch (Exception e){
           e.printStackTrace();
       }

        return new Result(false, StatusCode.ERROR,"文件上传失败");
    }
}
