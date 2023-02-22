package io.github.s3s3l.yggdrasil.utils.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

import io.github.s3s3l.yggdrasil.bean.combine.CombinableInteger;
import io.github.s3s3l.yggdrasil.bean.combine.CombinableInteger.IntegerCombineType;
import io.github.s3s3l.yggdrasil.utils.concurrent.CommonTaskExecutor;
import io.github.s3s3l.yggdrasil.utils.concurrent.TaskExecutor;

/**
 * <p>
 * </p>
 * ClassName:ExecutorTest <br>
 * Date: Mar 22, 2017 4:03:18 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ExecutorTest {
    private TaskExecutor executor = CommonTaskExecutor.create(() -> Executors.newFixedThreadPool(10));
    private List<Integer> conditions;

    @BeforeEach
    public void setConditions() {
        conditions = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            conditions.add(i);
        }
    }

    @Test
    public void callableExecute() {
        List<Integer> result = executor.execute(param -> param, Lists.partition(conditions, 1000));
        Assertions.assertEquals(result.size(), conditions.size());
        int count = executor.execute(param -> param.size(), Lists.partition(conditions, 1000),
                new CombinableInteger(0, IntegerCombineType.ADD));
        Assertions.assertEquals(conditions.size(), count);
    }
}
