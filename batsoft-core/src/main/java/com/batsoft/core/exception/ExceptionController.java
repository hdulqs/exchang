/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月12日 下午8:53:44
 */
package com.batsoft.core.exception;

import com.batsoft.core.dto.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p> 异常处理器</p>
 * @author:         Bat Admin
 * @Date :          2016年12月12日 下午8:53:44 
 */
/**
* 重写BasicErrorController,主要负责系统的异常页面的处理以及错误信息的显示
* @see org.springframework.boot.autoconfigure.web.BasicErrorController
* @see org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration
*
* @author Bat Admin
* @version 2016/5/31 11:22
* @since JDK 7.0+
*/

@RestController
@RequestMapping(value = "/")
@EnableConfigurationProperties({ServerProperties.class})
@Slf4j
public class ExceptionController implements ErrorController {

   private ErrorAttributes errorAttributes;

   @Autowired
   private ServerProperties serverProperties;


   /**
    * 初始化ExceptionController
    * @param errorAttributes
    */
   @Autowired
   public ExceptionController(ErrorAttributes errorAttributes) {
       Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
       this.errorAttributes = errorAttributes;
   }

   /**
    * 定义403的ModelAndView
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(produces = "text/html",value = "403")
   public ModelAndView error403(HttpServletRequest request,
                                 HttpServletResponse response) {
       response.setStatus(getStatus(request).value());

       Map<String, Object> model = getErrorAttributes(request,
               isIncludeStackTrace(request, MediaType.TEXT_HTML));

       log.info("403==:"+model.toString());

       return new ModelAndView("redirect:/403.html");
   }

   @RequestMapping(produces = "text/html",value = "403.html")
   public ModelAndView errorHtml403(HttpServletRequest request,
                                 HttpServletResponse response) {
       response.setStatus(getStatus(request).value());
       Map<String, Object> model = getErrorAttributes(request,
               isIncludeStackTrace(request, MediaType.TEXT_HTML));
       model.put("data", "403");
       return new ModelAndView( "403", model);
   }


   /**
    * 定义404的ModelAndView
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(produces = "text/html",value = "404")
   public ModelAndView error404(HttpServletRequest request,
                                 HttpServletResponse response) {
       response.setStatus(getStatus(request).value());

       return new ModelAndView("redirect:/404.html");
   }

   @RequestMapping(produces = "text/html",value = "404.html")
   public ModelAndView errorHtml404(HttpServletRequest request,
                                 HttpServletResponse response) {
       response.setStatus(getStatus(request).value());
       Map<String, Object> model = getErrorAttributes(request,
               isIncludeStackTrace(request, MediaType.TEXT_HTML));
       model.put("data", "mydata");
       return new ModelAndView( "404", model);
   }

   /**
    * 定义404的JSON数据
    * @param request
    * @return
    */
   @RequestMapping(value = "404")
   @ResponseBody
   public ErrorInfo<String> error404(HttpServletRequest request) {
       Map<String, Object> body = getErrorAttributes(request,
               isIncludeStackTrace(request, MediaType.APPLICATION_JSON));
       HttpStatus status = getStatus(request);
       ErrorInfo<String> r = new ErrorInfo<>();
       r.setMessage(body.toString());
       r.setCode(Integer.valueOf(status.toString()));
       r.setData("Some Data");
       r.setUrl(request.getRequestURL().toString());
       return r;
   }

   /**
    * 定义500的ModelAndView
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(produces = "text/html",value = "500")
   public ModelAndView error500(HttpServletRequest request,
                                 HttpServletResponse response) {
       response.setStatus(getStatus(request).value());

       Map<String, Object> model = getErrorAttributes(request,
               isIncludeStackTrace(request, MediaType.TEXT_HTML));

       log.info("500==:"+model.toString());

       return new ModelAndView("redirect:/500.html");
   }
   @RequestMapping(produces = "text/html",value = "500.html")
   public ModelAndView errorHtml500(HttpServletRequest request,
                                 HttpServletResponse response) {
       response.setStatus(getStatus(request).value());
       Map<String, Object> model = getErrorAttributes(request,
               isIncludeStackTrace(request, MediaType.TEXT_HTML));
       return new ModelAndView("500", model);
   }


   /**
    * 定义500的错误JSON信息
    * @param request
    * @return
    */
   @RequestMapping(value = "500")
   @ResponseBody
   public ErrorInfo<String> error500(HttpServletRequest request) {
       Map<String, Object> body = getErrorAttributes(request,
               isIncludeStackTrace(request, MediaType.APPLICATION_JSON));
       HttpStatus status = getStatus(request);
      
       ErrorInfo<String> r = new ErrorInfo<>();
       r.setMessage(body.toString());
       r.setCode(Integer.valueOf(status.toString()));
       r.setData("Some Data");
       r.setUrl(request.getRequestURL().toString());
       return r;
       
   }

    @RequestMapping(value = "/401", produces = "text/html")
    public ModelAndView error401(HttpServletResponse response, HttpServletRequest request) {
        response.setStatus(getStatus(request).value());
        Map<String, Object> model = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.TEXT_HTML));


        log.info("401==:"+model.toString());

        return new ModelAndView("redirect:/login");
    }
   
   /**
    *
    * <p> 在执行@RequestMapping之前遇到的异常</p>
    * @author:         Bat Admin
    * @param:    @param response
    * @param:    @param request
    * @param:    @return
    * @return: ModelAndView
    * @Date :          2016年12月12日 下午10:53:07
    * @throws:
    */

   @RequestMapping(value = "/error", produces = "text/html")
   public ModelAndView errorHtml(HttpServletResponse response, HttpServletRequest request) {
	   response.setStatus(getStatus(request).value());
	   Map<String, Object> model = getErrorAttributes(request,
               isIncludeStackTrace(request, MediaType.TEXT_HTML));


       log.info("error==:"+model.toString());

       return new ModelAndView("redirect:/error.html");
   }
   @RequestMapping(produces = "text/html",value = "error.html")
   public ModelAndView error(HttpServletRequest request,
                                 HttpServletResponse response) {
       response.setStatus(getStatus(request).value());
       Map<String, Object> model = getErrorAttributes(request,
               isIncludeStackTrace(request, MediaType.TEXT_HTML));
       return new ModelAndView("error", model);
   }
   /**
    * 定义eror的JSON数据
    * @param request
    * @return
    */
   @RequestMapping(value = "/error")
   @ResponseBody
   public ErrorInfo<String> error(HttpServletResponse response, HttpServletRequest request) {
	   Map<String, Object> body = getErrorAttributes(request,
               isIncludeStackTrace(request, MediaType.APPLICATION_JSON));
       HttpStatus status = getStatus(request);
      
       ErrorInfo<String> r = new ErrorInfo<>();
       r.setMessage(body.toString());
       r.setCode(Integer.valueOf(status.toString()));
       r.setData("Some Data");
       r.setUrl(request.getRequestURL().toString());
       return r;
   }
   

   /**
    * Determine if the stacktrace attribute should be included.
    * @param request the source request
    * @param produces the media type produced (or {@code MediaType.ALL})
    * @return if the stacktrace attribute should be included
    */
   protected boolean isIncludeStackTrace(HttpServletRequest request,
                                         MediaType produces) {
       ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();
       if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
           return true;
       }
       if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
           return getTraceParameter(request);
       }
       return false;
   }


   /**
    * 获取错误的信息
    * @param request
    * @param includeStackTrace
    * @return
    */
   private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                  boolean includeStackTrace) {
       RequestAttributes requestAttributes = new ServletRequestAttributes(request);
       return this.errorAttributes.getErrorAttributes(requestAttributes,
               includeStackTrace);
   }

   /**
    * 是否包含trace
    * @param request
    * @return
    */
   private boolean getTraceParameter(HttpServletRequest request) {
       String parameter = request.getParameter("trace");
       if (parameter == null) {
           return false;
       }
       return !"false".equals(parameter.toLowerCase());
   }

   /**
    * 获取错误编码
    * @param request
    * @return
    */
   private HttpStatus getStatus(HttpServletRequest request) {
       Integer statusCode = (Integer) request
               .getAttribute("javax.servlet.error.status_code");
       if (statusCode == null) {
           return HttpStatus.INTERNAL_SERVER_ERROR;
       }
       try {
           return HttpStatus.valueOf(statusCode);
       }
       catch (Exception ex) {
           return HttpStatus.INTERNAL_SERVER_ERROR;
       }
   }
   
   
   
  

   @Override
   public String getErrorPath() {
       return "/error";
   }

 

}
