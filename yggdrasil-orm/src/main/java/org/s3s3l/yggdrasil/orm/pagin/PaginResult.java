package org.s3s3l.yggdrasil.orm.pagin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaginResult<T> {
    private T data;
    private long recordsCount;
    private long pageCount;
}
