package com.tagzxia3.propguardian.src.main.java.com.talexframe.frame.core.modules.network.interceptor;// package com.talexframe.frame.core.modules.network.interceptor;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.talexframe.frame.core.pojo.wrapper.ResultData;
// import lombok.SneakyThrows;
// import org.springframework.core.MethodParameter;
// import org.springframework.http.MediaType;
// import org.springframework.http.converter.HttpMessageConverter;
// import org.springframework.http.server.ServerHttpRequest;
// import org.springframework.http.server.ServerHttpResponse;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
// import java.util.Objects;
//
// /**
//  * <br /> {@link com.talexframe.frame.interceptor Package }
//  *
//  * @author TalexDreamSoul
//  * @date 2022/1/16 10:33 <br /> Project: TalexFrame <br />
//  */
// @RestControllerAdvice
// public class ResponseAdviceInterceptor implements ResponseBodyAdvice<Object> {
//
//     private final ObjectMapper objectMapper;
//
//     public ResponseAdviceInterceptor(ObjectMapper objectMapper) {this.objectMapper = objectMapper;}
//
//     @Override
//     public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//
//         WrapperResponse wr = converterType.getAnnotation(WrapperResponse.class);
//
//         if ( wr != null && !wr.value() ) {
//             return false;
//         }
//
//         wr = Objects.requireNonNull(returnType.getMethod()).getAnnotation(WrapperResponse.class);
//
//         return wr == null || wr.value();
//
//     }
//
//     @SneakyThrows
//     @Override
//     public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//
//         if ( body instanceof ResultData ) {
//             return body;
//         }
//
//         if ( body instanceof String ) {
//
//             return objectMapper.writeValueAsString(ResultData.SUCCESS(body));
//
//         }
//
//         return ResultData.SUCCESS(body);
//
//     }
//
// }
