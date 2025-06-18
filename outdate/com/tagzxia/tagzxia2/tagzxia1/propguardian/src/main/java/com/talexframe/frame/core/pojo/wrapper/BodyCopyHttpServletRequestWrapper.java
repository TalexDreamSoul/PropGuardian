package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.wrapper;

import lombok.Getter;
import lombok.SneakyThrows;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <br /> {@link com.talexframe.frame.wrapper Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 20:39 <br /> Project: TalexFrame <br />
 */
public class BodyCopyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 输入流
     */
    private final byte[] bytes;
    @Getter
    private final String body;
    @Getter
    private final HttpServletRequest originRequest;

    public BodyCopyHttpServletRequestWrapper(HttpServletRequest request) {

        super(request);

        this.body = getBodyString(request);
        this.originRequest = request;

        bytes = this.body.getBytes(StandardCharsets.UTF_8);

    }

    /**
     * 获取请求Body
     *
     * @param request 原始请求
     */
    @SneakyThrows
    public String getBodyString(final ServletRequest request) {

        StringBuilder sb = new StringBuilder();

        InputStream inputStream = cloneInputStream(request.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String line;

        while ( Objects.nonNull(( line = reader.readLine() )) ) {

            sb.append(line);

        }

        return sb.toString();

    }

    /**
     * 输入流复制
     *
     * @param inputStream 输入流
     */
    @SneakyThrows
    private InputStream cloneInputStream(ServletInputStream inputStream) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;

        while ( ( len = inputStream.read(buffer) ) > -1 ) {

            byteArrayOutputStream.write(buffer, 0, len);

        }

        byteArrayOutputStream.flush();

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

    }

    @Override
    public BufferedReader getReader() {

        return new BufferedReader(new InputStreamReader(getInputStream()));

    }

    /**
     * 重写父方法，返回新地输入流
     */
    @Override
    public ServletInputStream getInputStream() {

        final ByteArrayInputStream copyStream = new ByteArrayInputStream(bytes);

        return new ServletInputStream() {

            @Override
            public int read() {

                return copyStream.read();
            }

            /**
             * 未读状态
             */
            @Override
            public boolean isFinished() {

                return false;
            }

            @Override
            public boolean isReady() {

                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

        };

    }

}
