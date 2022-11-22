package org.s3s3l.yggdrasil.spring.web;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.s3s3l.yggdrasil.utils.common.FreeMarkerHelper;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.View;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FreeMarkerView implements View {
    private String templatePath;
    private Object obj;
    private FreeMarkerHelper freeMarkerHelper;
    private String template;

    public void render(@Nullable Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/html");
        try (OutputStream os = response.getOutputStream()) {
            os.write(freeMarkerHelper.format(templatePath, template, obj).getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
        response.flushBuffer();
    }
}
