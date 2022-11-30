package org.s3s3l.yggdrasil.orm.pagin;

import org.s3s3l.yggdrasil.orm.bind.annotation.Limit;
import org.s3s3l.yggdrasil.orm.bind.annotation.Offset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ConditionForPagination {
    private int pageSize;
    private int pageIndex;

    @Offset
    private long offset;
    @Limit
    private long limit;

    public void prepare() {
        if (pageSize <= 0) {
            pageSize = 10;
        }

        if (pageIndex <= 0) {
            pageIndex = 1;
        }

        offset = (pageIndex - 1) * pageSize;
        limit = pageSize;
    }
}
