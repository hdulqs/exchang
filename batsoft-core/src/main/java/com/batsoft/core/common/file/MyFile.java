package com.batsoft.core.common.file;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/16.
 */
public class MyFile implements Serializable {
    public  static final  String ALLOWFILE="allowFile";
    public  static final  String MAXSIZE="allowMaxSize";

    public static String fileType="ico,gif,GIF,jpg,JPG,jpeg,JPEG,png,PNG,bmp,BMP,swf,SWF,flv,FLV,swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,SWF,FLV,MP3,WAV,WMA,WMV,MID,AVI,MPG,RMVB,doc,docx,xls,xlsx,ppt,,txt,zip,rar,DOC,DOCX,XLS,XLSX,PPT,HTM,HTML,TXT,ZIP,RAR,svg";
    public static long maxSize=500000000;

    /**
     * extMap.put("images", "gif,GIF,jpg,JPG,jpeg,JPEG,png,PNG,bmp,BMP");
     *  extMap.put("flashs", "swf,SWF,flv,FLV");
     *  extMap.put("medias", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,SWF,FLV,MP3,WAV,WMA,WMV,MID,AVI,MPG,ASF,RM,RMVB");
     * extMap.put("files", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2,DOC,DOCX,XLS,XLSX,PPT,HTM,HTML,TXT,ZIP,RAR,GZ,BZ2");
     * extMap.put("sensitive", "txt,TXT");
     */
    /*static {
        // key文件夹名称
        // value该文件夹内可以上传文件的后缀名
        fileType.put(ALLOWFILE, "gif,GIF,jpg,JPG,jpeg,JPEG,png,PNG,bmp,BMP,swf,SWF,flv,FLV,swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,SWF,FLV,MP3,WAV,WMA,WMV,MID,AVI,MPG,ASF,RM,RMVB,doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2,DOC,DOCX,XLS,XLSX,PPT,HTM,HTML,TXT,ZIP,RAR,GZ,BZ2");

    }*/
}
