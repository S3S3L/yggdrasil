package org.s3s3l.yggdrasil.spring.analyzer;

import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class DefaultStartupFailureAnalyzer implements FailureAnalyzer {

    @Override
    public FailureAnalysis analyze(Throwable failure) {
        log.error("FailureAnalysis", failure);
        return null;
    }

}
