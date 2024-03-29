package io.github.s3s3l.yggdrasil.orm.pagin;

import io.github.s3s3l.yggdrasil.orm.bind.annotation.Limit;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.Offset;
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
    private boolean pagin;

    @Offset
    private Long offset;
    @Limit
    private Long limit;

    public void prepare() {
        if (pageSize <= 0) {
            pageSize = 10;
        }

        if (pageIndex <= 0) {
            pageIndex = 1;
        }

        offset = Long.valueOf((pageIndex - 1) * pageSize);
        limit = Long.valueOf(pageSize);
    }
}
