package io.github.s3s3l.yggdrasil.orm.exec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateConfig {
    /**
     * 是否无视表是否存在，如果为true，当表已存在的时候会抛出异常
     */
    @Builder.Default
    private boolean force = false;
    /**
     * 建表前是否先执行drop操作
     */
    @Builder.Default
    private boolean dropFirst = false;
    /**
     * 是否检查字段类型，如果类型不匹配则抛出异常
     */
    @Builder.Default
    private boolean columnTypeCheck = true;
    /**
     * 是否自动添加不存在的字段
     */
    @Builder.Default
    private boolean autoAddColumn = true;
    /**
     * 是否自动删除未定义的字段
     */
    @Builder.Default
    private boolean autoDropColumn = false;
    /**
     * 是否自动变更字段属性
     */
    @Builder.Default
    private boolean autoAlterColumn = false;
}
