package io.github.s3s3l.yggdrasil.template;

import java.nio.file.Path;

import io.github.s3s3l.yggdrasil.bean.Sortable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateMeta implements Sortable {
    /**
     * The larger value takes precedence.<br />
     * 数值较大的优先级较高
     */
    @Builder.Default
    private long priority = 0;
    /**
     * mark if the content of the template has been loaded.<br />
     * 标识模板内容是否已经加载
     */
    @Builder.Default
    private boolean loaded = false;

    private String name;
    private Path path;
    private String content;
}
