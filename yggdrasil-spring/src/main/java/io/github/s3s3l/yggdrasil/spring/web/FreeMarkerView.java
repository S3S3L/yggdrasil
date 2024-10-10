package io.github.s3s3l.yggdrasil.spring.web;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.View;

import io.github.s3s3l.yggdrasil.utils.common.FreeMarkerHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    public void render(@Nullable Map<String, ?> model, @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response)
            throws Exception {
        response.setContentType("text/html");
        try (OutputStream os = response.getOutputStream()) {
            os.write(freeMarkerHelper.format(templatePath, template, obj).getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
        response.flushBuffer();
    }
}
