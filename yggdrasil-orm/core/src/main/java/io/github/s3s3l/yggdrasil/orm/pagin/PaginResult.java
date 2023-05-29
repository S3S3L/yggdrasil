package io.github.s3s3l.yggdrasil.orm.pagin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaginResult<T> {
    /**
     * 返回值
     */
    private T data;
    /**
     * 总记录数
     */
    private long recordsCount;
    /**
     * 总页数
     */
    private long pageCount;
}
