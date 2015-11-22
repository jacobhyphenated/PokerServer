///*
// * File take from https://github.com/bhagyas/spring-jsonp-support
// * Copyright Bhagya Nirmaan Silva
// */
//package com.hyphenated.card.servlet.filter;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.Map;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//public class JsonpCallbackFilter implements Filter {
//
//    private static Log log = LogFactory.getLog(JsonpCallbackFilter.class);
//
//    public void init(FilterConfig fConfig) throws ServletException {}
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        @SuppressWarnings("unchecked")
//        Map<String, String[]> parms = httpRequest.getParameterMap();
//
//        if(parms.containsKey("callback")) {
//            if(log.isDebugEnabled())
//                log.debug("Wrapping response with JSONP callback '" + parms.get("callback")[0] + "'");
//
//            OutputStream out = httpResponse.getOutputStream();
//
//            GenericResponseWrapper wrapper = new GenericResponseWrapper(httpResponse);
//
//            chain.doFilter(request, wrapper);
//
//            out.write(new String(parms.get("callback")[0] + "(").getBytes());
//            //Handle error case. If the callback is used, the Exception Handling is skipped
//            if(wrapper.getData() == null || wrapper.getData().length == 0){
//            	out.write(new String("{\"error\":\"There was a server error completing your request\"}").getBytes());
//            	wrapper.setStatus(400);
//            }else{
//            	out.write(wrapper.getData());
//            }
//            out.write(new String(");").getBytes());
//
//            wrapper.setContentType("text/javascript;charset=UTF-8");
//            out.close();
//        } else {
//            chain.doFilter(request, response);
//        }
//    }
//
//    public void destroy() {}
//}