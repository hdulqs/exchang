package com.batsoft.core.common.file;

import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.utils.AliOSSUtil;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.IdGen;
import com.batsoft.utils.StringUtils;
import com.google.common.collect.Maps;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


public class FileUpload extends org.apache.commons.io.FileUtils{

	private static Logger logger = LoggerFactory.getLogger(FileUpload.class);

	public static final String success = "success";
	public static final String result = "result";
	public static final String fileName = "filename";
	public static final String ext = "fileExt";
	public static final String totalSize = "totalSize";
	public static final String originalfilename="originalfilename";


	private RedisService redisService;


	/**
	 * 上传文件-多图上传
	 * @param myFiles
	 * @param targetDir
	 * @param fileDir
	 * @param rename  是否重命名 0：原文件名 1：文件重命名
	 * @return
	 */
	public static Map<String,Object> fileUpload(MultipartFile[] myFiles,
						    String targetDir,String fileDir,HttpServletRequest request,int rename) throws IOException {
		Map<String,Object> map = Maps.newHashMap();
		String originalFilename = "";
		try {
			for(MultipartFile myFile : myFiles){
				Map<String,Object> map1 = fileUpload(myFile, targetDir, fileDir,request,rename);
				if("true".equals(map1.get(success).toString())){
					String imgPath = map1.get(result).toString();
					originalFilename += imgPath + ",";
				}else{
					map.put(success, false);
					map.put(result, map1.get(result));
					return map;
				}
			}
			map.put(success, true); // 成功或者失败
			if(originalFilename.length()>0){
				map.put(result, originalFilename.substring(0, originalFilename.length() - 1)); // 上传成功的所有的图片地址的路径
			}else{
				map.put(result, originalFilename); // 上传成功的所有的图片地址的路径
			}
		} catch (NullPointerException e) {
			map.put(success,false);
			map.put(result, "上传失败");
			e.printStackTrace();
		} catch (IOException e) {
			map.put(success,false);
			map.put(result, "上传失败");
			e.printStackTrace();
		}

		return map;
	}


	/**
	 * 上传文件-单图上传
	 * @param myFile
	 * @param targetDir
	 * @param fileDir
	 * @param rename  是否重命名 0：原文件名 1：文件重命名
	 * @return
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static Map<String,Object> fileUpload(MultipartFile myFile, String targetDir,String fileDir,HttpServletRequest request,int rename) throws IOException,NullPointerException {
		Map<String,Object> map = Maps.newHashMap();
		String originalFilename ;
		map.put(success,false);
		// boolean errorFlag = true;
		// 获取内容类型
		String contentType = request.getContentType();
		int contentLength = request.getContentLength();
		// 文件保存目录路径
		String savePath = targetDir;
		// 文件保存目录URL
		File uploadDir = new File(savePath);
		String fileExt = myFile.getOriginalFilename().substring(myFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
		RedisService  redisService=(RedisService) SpringContextUtil.getBean("redisService");
		String fileType=redisService.get(Constants.CACHE_CONFIG_KEY+MyFile.ALLOWFILE);
		Long maxsize=Long.valueOf(redisService.get(Constants.CACHE_CONFIG_KEY+MyFile.MAXSIZE));

		if(!StringUtils.isEmpty(fileType)){
		 MyFile.fileType=fileType;
		}
		if(maxsize!=null) {
			MyFile.maxSize=(Long)maxsize;
		}

		if(myFile.isEmpty()){
			//上传图片为空
			map.put(result, "请选择文件后上传");
		}else if (!Arrays.<String> asList(MyFile.fileType.split(",")).contains(fileExt)) {// 检查扩展名
			map.put(result, "上传文件扩展名是不允许的扩展名。\n只允许" + MyFile.fileType + "格式。");
		} else if (contentType == null || !contentType.startsWith("multipart")) {
			System.out.println("请求不包含multipart/form-data流");
			map.put(result, "请求不包含multipart/form-data流");
		} else if (MyFile.maxSize < contentLength) {
			System.out.println("上传文件大小超出文件最大大小");
			map.put(result, "上传文件大小超出文件最大大小[" + com.batsoft.utils.FileUtils.convertFileSize(MyFile.maxSize) + "]");
		} else if (!ServletFileUpload.isMultipartContent(request)) {
			map.put(result, "请选择文件");
		} else if (!uploadDir.isDirectory()) {// 检查目录
			map.put(result, "上传目录[" + savePath + "]不存在");
		} else if (!uploadDir.canWrite()) {
			map.put(result, "上传目录[" + savePath + "]没有写权限");
		} else{
			fileDir=fileDir+"/"+DateUtils.getDateStr(DateUtils.DEFAULT_FORMAT_YYYYMMDD);
			com.batsoft.utils.FileUtils.mkdir(targetDir + fileDir);
			if(rename==0){
				originalFilename = myFile.getOriginalFilename();
				org.apache.commons.io.FileUtils.copyInputStreamToFile(myFile.getInputStream(),
						new File(targetDir + fileDir, originalFilename));
				map.put(totalSize, com.batsoft.utils.FileUtils.getSize((double) myFile.getInputStream().available()));
				map.put(result, fileDir + "/" + originalFilename);
				map.put(fileName , originalFilename);
				map.put(ext,fileExt);
				map.put(originalfilename, myFile.getOriginalFilename());
				map.put(success, true);
			}else{
				originalFilename = String.valueOf(new DateTime().getMillis())+
						myFile.getOriginalFilename().substring( myFile.getOriginalFilename().lastIndexOf("."));
				org.apache.commons.io.FileUtils.copyInputStreamToFile(myFile.getInputStream(),
						new File(targetDir + fileDir, originalFilename));
				map.put(totalSize, com.batsoft.utils.FileUtils.getSize((double) myFile.getInputStream().available()));
				map.put(result, fileDir + "/" + originalFilename);
				map.put(fileName , originalFilename);
				map.put(originalfilename, myFile.getOriginalFilename());
				map.put(ext,fileExt);
				map.put(success, true);
			}


		}
		return map;
	}

	/**
	 * 上传文件-单图上传 OSS
	 * @param myFile
	 * @return
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static Map<String,Object> fileUploadOss(MultipartFile myFile,String rootPath,HttpServletRequest request) throws IOException,NullPointerException {


		Map<String,Object> map = Maps.newHashMap();
		map.put(success,false);
		// boolean errorFlag = true;
		// 获取内容类型
		String contentType = request.getContentType();
		int contentLength = request.getContentLength();
		// 文件保存目录路径
		// 文件保存目录URL
		String fileExt = myFile.getOriginalFilename().substring(myFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
		RedisService  redisService=(RedisService) SpringContextUtil.getBean("redisService");
		String fileType=redisService.get(Constants.CACHE_CONFIG_KEY+MyFile.ALLOWFILE);
		//Long maxsize=Long.valueOf(redisService.get(Constants.CACHE_CONFIG_KEY+MyFile.MAXSIZE));
		Long maxsize=10000000L;
		if(!StringUtils.isEmpty(fileType)){
			MyFile.fileType=fileType;
		}
		if(maxsize!=null) {
			MyFile.maxSize=(Long)maxsize;
		}

		if(myFile.isEmpty()){
			//上传图片为空
			map.put(result, "请选择文件后上传");

		}else if (!Arrays.<String> asList(MyFile.fileType.split(",")).contains(fileExt)) {
			map.put(result, "上传文件扩展名是不允许的扩展名。\n只允许" + MyFile.fileType + "格式。");
		} else if (contentType == null || !contentType.startsWith("multipart")) {
			System.out.println("请求不包含multipart/form-data流");
			map.put(result, "请求不包含multipart/form-data流");
		} else if (MyFile.maxSize < contentLength) {
			System.out.println("上传文件大小超出文件最大大小");
			map.put(result, "上传文件大小超出文件最大大小[" + com.batsoft.utils.FileUtils.convertFileSize(MyFile.maxSize) + "]");
		} else if (!ServletFileUpload.isMultipartContent(request)) {
			map.put(result, "请选择文件");
		} else{

			String filePath=rootPath+"/"+DateUtils.getDateStr(new Date(),DateUtils.DEFAULT_FORMAT_YYYYMMDD)+"/"+ IdGen.uuid()+"."+fileExt;
			String  path =AliOSSUtil.upload(myFile.getInputStream(),filePath);
				map.put(totalSize, com.batsoft.utils.FileUtils.getSize((double) myFile.getInputStream().available()));
				map.put(result, path);
				map.put(originalfilename, myFile.getOriginalFilename());
				map.put(ext,fileExt);
				map.put(success, true);
		}
		return map;
	}



}