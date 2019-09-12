package org.s3s3l.yggdrasil.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.s3s3l.yggdrasil.bean.combine.CombinableInteger;
import org.s3s3l.yggdrasil.bean.combine.CombinableInteger.IntegerCombineType;
import org.s3s3l.yggdrasil.utils.concurrent.CommonTaskExecutor;
import org.s3s3l.yggdrasil.utils.concurrent.TaskExecutor;

import com.google.common.collect.Lists;

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

    @Before
    public void setConditions() {
        conditions = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            conditions.add(i);
        }
    }

    @Test
    public void callableExecute() {
        List<Integer> result = executor.execute(param -> param, Lists.partition(conditions, 1000));
        Assert.assertEquals(result.size(), conditions.size());
        int count = executor.execute(param -> param.size(), Lists.partition(conditions, 1000),
                new CombinableInteger(0, IntegerCombineType.ADD));
        Assert.assertEquals(conditions.size(), count);
    }
}
