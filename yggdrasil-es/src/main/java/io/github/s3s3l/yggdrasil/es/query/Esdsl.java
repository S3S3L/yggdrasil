package io.github.s3s3l.yggdrasil.es.query;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 * ClassName:Esdsl <br>
 * Date: Jan 4, 2019 11:53:34 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class Esdsl implements QueryBlock {
    private List<QueryBlock> queryBlocks;

    public Esdsl add(QueryBlock queryBlock) {
        getQueryBlocks().add(queryBlock);
        return this;
    }

    private List<QueryBlock> getQueryBlocks() {
        if (queryBlocks == null) {
            queryBlocks = new LinkedList<>();
        }

        return queryBlocks;
    }

    @Override
    public String toQueryString() {
        return String.format("{%s}", queryBlocks.stream()
                .map(QueryBlock::toQueryString)
                .collect(Collectors.joining(",")));
    }

}
