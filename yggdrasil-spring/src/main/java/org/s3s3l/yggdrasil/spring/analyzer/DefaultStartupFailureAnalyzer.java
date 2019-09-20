package org.s3s3l.yggdrasil.spring.analyzer;

import org.s3s3l.yggdrasil.utils.log.ILogger;
import org.s3s3l.yggdrasil.utils.log.base.LogHelper;
import org.s3s3l.yggdrasil.utils.log.base.LogHelper.LogLevel;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

/**
 * <p>
 * </p>
 * ClassName:DefaultStartupFailureAnalyzer <br>
 * Date: Mar 30, 2017 5:10:10 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultStartupFailureAnalyzer implements FailureAnalyzer {

    private ILogger logger = LogHelper.create(LogLevel.ERROR);

    @Override
    public FailureAnalysis analyze(Throwable failure) {
        logger.error(failure);
        return null;
    }

}
