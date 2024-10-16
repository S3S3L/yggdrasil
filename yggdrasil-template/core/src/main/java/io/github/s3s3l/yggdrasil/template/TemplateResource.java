package io.github.s3s3l.yggdrasil.template;

import io.github.s3s3l.yggdrasil.bean.Sortable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class TemplateResource implements Comparable<TemplateResource>, Sortable {
    /**
     * The larger value takes precedence.<br />
     * 数值较大的优先级较高
     */
    @Builder.Default
    private int priority = 0;
    /**
     * The path of the template resource.<br />
     * 模板资源的路径
     */
    private String path;

    @Override
    public int compareTo(TemplateResource o) {
        if (o == null) {
            return 1;
        }
        return o.priority - this.priority;
    }
}
