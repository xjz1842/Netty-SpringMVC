package com.netty.mvc.server.handler;

import com.netty.mvc.annotiaon.Controller;
import com.netty.mvc.annotiaon.RequestMapping;
import com.netty.mvc.server.WebHandler;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * @Author: zxj
 * @Date: 2019-09-21 14:36
 * @desc
 */
@Configuration
public class DefaultWebHandler implements WebHandler {


    public DefaultWebHandler(ApplicationContext applicationContext) {
        registerHandler(applicationContext);
    }

    /**
     * url -> Method对应
     */
    private Map<String, Method> handlerMap = new LinkedHashMap<>();

    /**
     * method—>controller的对应
     */
    private Map<Method, Object> controllerMap = new HashMap<>();

    private void registerHandler(ApplicationContext context) {

        Map<String, Object> annotationControllerClasses = context.getBeansWithAnnotation(Controller.class);

        Set<Class<?>> handlerTypes = new LinkedHashSet<>();
        Class<?> specificHandlerType = null;

        for (Object targetType : annotationControllerClasses.values()) {
            if (!Proxy.isProxyClass(targetType.getClass())) {
                specificHandlerType = ClassUtils.getUserClass(targetType);
                handlerTypes.add(specificHandlerType);
            }
            final Class<?> targetClass = (specificHandlerType != null ? specificHandlerType : targetType.getClass());

            ReflectionUtils.doWithMethods(specificHandlerType, method -> {
                String url = "";
                if (targetClass.isAnnotationPresent(RequestMapping.class)) {
                    url = targetClass.getAnnotation(RequestMapping.class).value();
                }
                Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
                if (specificMethod.isAnnotationPresent(RequestMapping.class)) {
                    url += specificMethod.getAnnotation(RequestMapping.class).value();
                }
                handlerMap.put(url, specificMethod);
                controllerMap.put(specificMethod, targetType);
            }, ReflectionUtils.USER_DECLARED_METHODS);
        }
    }

    @Override
    public Object handle(HttpRequest httpRequest) {
        Method method = handlerMap.get(httpRequest.uri());

        if(method != null){
            try {
                return method.invoke(controllerMap.get(method));
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }catch (InvocationTargetException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
