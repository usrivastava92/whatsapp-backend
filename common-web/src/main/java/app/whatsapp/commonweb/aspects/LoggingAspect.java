package app.whatsapp.commonweb.aspects;

import app.whatsapp.common.constants.CommonConstants;
import app.whatsapp.common.models.BaseResponse;
import app.whatsapp.commonweb.annotations.log.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Around(value = "@annotation(app.whatsapp.commonweb.annotations.log.Log)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();

        boolean isControllerMethod = isControllerMethod(method);

        Log loggable = method.getAnnotation(Log.class);
        Set<Log.Context> loggableContexts = Set.of(loggable.contexts());

        String fullMethodName = new StringBuilder(proceedingJoinPoint.getSignature().getDeclaringType().getCanonicalName())
                .append(CommonConstants.SpecialChars.DOT)
                .append(method.getName()).toString();
        if (loggableContexts.contains(Log.Context.ARGS) || isControllerMethod) {
            Parameter[] parameters = method.getParameters();
            Map<String, String> loggableArgs = new HashMap<>();
            Object[] methodArgs = proceedingJoinPoint.getArgs();
            Map<Class, Object> argMap = new HashMap<>();
            for (Object arg : methodArgs) {
                argMap.put(arg.getClass(), arg);
            }
            for (Parameter parameter : parameters) {
                String parameterName = parameter.getName();
                Object value = argMap.get(parameter.getType());
                if (isLoggable(parameter) && value != null) {
                    loggableArgs.put(parameterName, value.toString());
                }
            }
            LOGGER.info("Method args for \"{}\" are : {}", fullMethodName, loggableArgs);
        }
        Object returnValue = proceedingJoinPoint.proceed();
        if (loggableContexts.contains(Log.Context.RETURN_VALUE) || (isControllerMethod && returnValue instanceof BaseResponse)) {
            LOGGER.info("Return value for \"{}\" is : {} ", fullMethodName, returnValue);
        }
        LOGGER.info("Time taken for \"{}\" execution is : {} milliseconds", fullMethodName, System.currentTimeMillis() - startTime);
        return returnValue;
    }

    private boolean isLoggable(Parameter parameter) {
        Log log = parameter.getAnnotation(Log.class);
        RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        return log != null || requestBody != null || pathVariable != null || requestParam != null;
    }

    private boolean isControllerMethod(Method method) {
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return getMapping != null || postMapping != null || putMapping != null || deleteMapping != null || patchMapping != null || requestMapping != null;
    }

}
