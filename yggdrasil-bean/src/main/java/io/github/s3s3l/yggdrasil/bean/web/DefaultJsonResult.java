package io.github.s3s3l.yggdrasil.bean.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultJsonResult<T> implements JsonResult<T> {

    @Builder.Default
    private int code = 0;
    private String msg;
    private T data;

    public boolean isSuccess() {
        return code == 0;
    }
}
