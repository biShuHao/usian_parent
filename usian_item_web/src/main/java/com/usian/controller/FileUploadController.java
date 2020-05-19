package com.usian.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.usian.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestMapping("file")
@RestController
public class FileUploadController {

    @Autowired
    private FastFileStorageClient storageClient;

    private static final List<String> CONTENT_TYPES = Arrays.asList("image/jpeg","image/gif");

    @RequestMapping("upload")
    public Result fileUpload(MultipartFile file) throws IOException {
        //获取文件名称
        String filename = file.getOriginalFilename();
        //校验文件类型
        String contentType = file.getContentType();
        System.out.println("contentType="+contentType);
        if(!CONTENT_TYPES.contains(contentType)){
            return Result.error("文件类型不合法");
        }
        //校验文件大小
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if(bufferedImage == null){
            return Result.error("文件内容不合法："+filename);
        }
        //上传，保存到服务器
        /*
            file.transferTo(new File("D:\\images\\" + originalFilename));
            String ext = filename.substring(filename.lastIndexOf("."));
        */
        //获取后缀名
        String ext = StringUtils.substringAfterLast(filename, ".");
        // 上传并保存图片，参数：1-上传的文件流 2-文件的大小 3-文件的后缀 4-可以不管他
        StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(),ext,null);
        //生成url地址，返回                        获取带分组的路径
        return Result.ok("http://image.usian.com/"+storePath.getFullPath());


    }

}
