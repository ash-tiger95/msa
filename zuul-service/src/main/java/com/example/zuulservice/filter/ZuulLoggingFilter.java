package com.example.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j // lombok에서 logger 객체 제공, (== Logger logger = LoggerFactory.getLogger(ZuulLoggingFilter.class); )
@Component // 스프링 객체(빈)으로 등록하는 용도 (Controller, Service 등 뭔지 모를 때)
public class ZuulLoggingFilter extends ZuulFilter { // Log를 출력해주는 필터
    // Logger logger = LoggerFactory.getLogger(ZuulLoggingFilter.class);

    @Override
    public Object run() throws ZuulException { // 실제 어떤 동작하는지, 사용자 요청이 들어올 때 먼저 실행
        log.info("************printing logs: ");

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest(); // request 정보, 어떤 요청인지 알 수 있다.
        log.info("************ " + request.getRequestURI());
        return null;
    }

    @Override
    public String filterType() { // 이 필터가 사전인지 사후인지 결정
        return "pre";
    }

    @Override
    public int filterOrder() { // 필터 순서
        return 1;
    }

    @Override
    public boolean shouldFilter() { // 필터 사용 유무
        return true;
    }
}
