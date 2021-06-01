package com.example.springboot2.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局过滤器，用于处理一些需要在请求前处理的事物
 */
@Component
public class ServletFilter implements Filter {

    /**
     * 该方法用于初始化过滤器
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    /**
     * 该方法在容器移除 servlet 时执行，同样只执行一次
     */
    @Override
    public void destroy() {}

    /**
     * 该方法只执行在响应 WEB 请求的最开始
     * 这里添加了任意域名任意请求方式可跨域访问的功能
     * Access-Control-Allow-Origin   允许跨域访问的域名
     * Access-Control-Allow-Headers  允许任何请求头访问
     * Access-Control-Allow-Methods  允许跨域访问的请求方式
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        chain.doFilter(req, res);
    }
}
