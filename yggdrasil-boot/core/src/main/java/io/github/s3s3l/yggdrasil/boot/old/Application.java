package io.github.s3s3l.yggdrasil.boot.old;

import io.github.s3s3l.yggdrasil.boot.old.component.context.ComponentConfigurationContext;
import io.github.s3s3l.yggdrasil.boot.old.exceptions.ApplicationInitalizingException;
import io.github.s3s3l.yggdrasil.boot.old.exceptions.ApplicationRunTimeException;

public interface Application {

	void beforeAll() throws ApplicationInitalizingException;

	void prepare() throws ApplicationInitalizingException;

	ComponentConfigurationContext run() throws ApplicationRunTimeException, ApplicationInitalizingException;

	void stop() throws ApplicationRunTimeException;

	void restart() throws ApplicationRunTimeException, ApplicationInitalizingException;

	void destory() throws ApplicationRunTimeException;

	boolean isRunning();

	boolean isStarted();
}
