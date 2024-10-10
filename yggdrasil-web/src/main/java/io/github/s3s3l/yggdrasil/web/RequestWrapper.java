package io.github.s3s3l.yggdrasil.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * 实现http请求流的重复读写
 * 
 * @author kehw
 *
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest original;
    private byte[] reqBytes;
    private boolean firstTime = true;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        original = request;
    }

    @Override
    public BufferedReader getReader() throws IOException {

        if (firstTime) {
            firstTime();
        }

        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(reqBytes));
        return new BufferedReader(isr);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (firstTime) {
            firstTime();
        }

        return new ServletInputStream() {
            private int i;

            @Override
            public int read() throws IOException {
                byte b;
                if (reqBytes.length > i) {
                    b = reqBytes[i++];
                } else {
                    b = -1;
                }
                return b;
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener arg0) {
                // do nothing
            }
        };
    }

    private void firstTime() throws IOException {
        firstTime = false;
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = original.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reqBytes = buffer.toString()
                .getBytes(StandardCharsets.UTF_8);
    }
}
