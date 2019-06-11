package com.batsoft.core.common.file;


import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.common.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/16.
 */
@Api(value = "FileUploadController",description = "文件上传 controller")
@Controller("fileUploadController")
@RequestMapping("/upload")
@Slf4j
public class FileUploadController {

    public static String FILETYPE = "filetype";
    public static String SUCCESS = "success";



    /**
     * 单个文件上传
     *
     * @param file
     * @param request
     * @return
     */
    @ApiOperation(value = "上传单个文件")
    @RequestMapping(value = "/file",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> upLoadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        Map<String, Object> map = null;
        String fileType = request.getParameter(FILETYPE);
        String filePath = Constants.UPLOAD_ATTACH;
        try {
            if (null != fileType) {
                filePath = filePath + "/" + fileType;
            }
            map = FileUpload.fileUploadOss(file,filePath,request);
        } catch (Exception e) {
            e.printStackTrace();
           // log.info("file upload err==" + e.getMessage());
        }

        return map;
    }

    /**
     * 批量上传
     *
     * @param files
     * @param request
     * @return
     */
    @ApiOperation(value = "上传多个文件")
    @RequestMapping(value = "/files",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> upLoadFiles(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) {
        Map<String, Object> map = null;
        String fileType = request.getParameter(FILETYPE);
        String filePath = "";
        try {
            if (null != fileType) {
                filePath = Constants.UPLOAD_ATTACH + "/" + fileType;
            }
            map = FileUpload.fileUpload(files, ApplicationConfigure.FILE_BASEPATH, filePath, request, 1);
            Boolean uploadResult = (Boolean) map.get("success");
            String webPath = "";
            if (uploadResult) {
                webPath = (String) map.get("result");
               // log.info("webpath>>>>>>>>>>>>>>>>>" + webPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;

    }
}
