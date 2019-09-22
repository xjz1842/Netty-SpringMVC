package com.netty.mvc;

import com.netty.mvc.server.WebHandler;
import com.netty.mvc.mvc.HandlerMapping;
import com.netty.mvc.mvc.adapter.HandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author: zxj
 * @Date: 2019-09-04 14:00
 * @desc 请求转发处理器
 */
@Configuration
public class DispatcherHandler implements WebHandler, ApplicationContextAware {


    private List<HandlerMapping> handlerMappings;

    private List<HandlerAdapter> handlerAdapters;

    /**
     * Create a new {@code DispatcherHandler} for the given {@link ApplicationContext}.
     * @param applicationContext the application context to find the handler beans in
     */
    public DispatcherHandler(ApplicationContext applicationContext) {
        initStrategies(applicationContext);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        initStrategies(applicationContext);
    }

    protected void initStrategies(ApplicationContext context) {

        Map<String, HandlerMapping> mappingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                context, HandlerMapping.class, true, false);

        ArrayList<HandlerMapping> mappings = new ArrayList<>(mappingBeans.values());
        AnnotationAwareOrderComparator.sort(mappings);
        this.handlerMappings = Collections.unmodifiableList(mappings);

        Map<String, HandlerAdapter> adapterBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                context, HandlerAdapter.class, true, false);

        this.handlerAdapters = new ArrayList<>(adapterBeans.values());
        AnnotationAwareOrderComparator.sort(this.handlerAdapters);

    }

    @Override
    public Object handle(HttpRequest httpRequest) {

        for(HandlerMapping handlerMapping : handlerMappings){
           Object handler = handlerMapping.getWebHandler(httpRequest);
           if(handler != null){
               for(HandlerAdapter adapter: handlerAdapters){
                    if(adapter.support(handler)){
                      return  adapter.handle(httpRequest,handler);
                    }
               }
           }
        }
        return null;
    }

}
