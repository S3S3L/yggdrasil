package io.github.s3s3l.yggdrasil.boot.old;

import io.github.s3s3l.yggdrasil.boot.old.component.ComponentScanner;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.ScanPackages;
import io.github.s3s3l.yggdrasil.boot.old.component.context.ComponentConfigurationContext;
import io.github.s3s3l.yggdrasil.boot.old.enumerations.ApplicationLifecycle;
import io.github.s3s3l.yggdrasil.boot.old.exceptions.ApplicationInitalizingException;
import io.github.s3s3l.yggdrasil.boot.old.exceptions.ApplicationRunTimeException;
import javafx.application.Platform;

public class DefaultFXApplication implements Application {
    private Class<? extends javafx.application.Application> appClass;
    private final String[] primaryPackages = { "io.github.s3s3l.yggdrasil.boot.old.component" };
    private String[] componentPackages;
    private ApplicationLifecycle lifecycle = ApplicationLifecycle.CREATED;

    private ComponentConfigurationContext context;

    public DefaultFXApplication(Class<? extends javafx.application.Application> appClass)
            throws ApplicationInitalizingException {
        if (appClass.isAnnotationPresent(ScanPackages.class)) {
            ScanPackages scanPackages = appClass.getAnnotation(ScanPackages.class);
            this.componentPackages = scanPackages.value();
        } else {
            this.componentPackages = new String[] { appClass.getPackage()
                    .getName() };
        }
        this.appClass = appClass;
        prepare();
    }

    @Override
    public void beforeAll() throws ApplicationInitalizingException {
        switch (this.lifecycle) {
            case CREATED:
                this.lifecycle = ApplicationLifecycle.INITIALIZING;
                break;
            case INITIALIZING:
            case INITIALIZED:
            case PREPARING:
            case PREPARED:
            case STARTING:
            case STARTED:
            case STOPPING:
            case STOPPED:
            case DESTROYING:
            case DESTROYED:
            case TERMINATED:
            default:
                return;
        }
        try {
            // scan primary components
            this.context = ComponentScanner.scan(this.primaryPackages);

            // scan user components
            ComponentScanner.scan(this.context, this.componentPackages);

            // register componentConfigurationContext
            this.context.addAtom("defaultComponentConfigurationContext", this.context);
        } catch (Exception e) {
            this.lifecycle = ApplicationLifecycle.TERMINATED;
            throw new ApplicationInitalizingException(e);
        }
        this.lifecycle = ApplicationLifecycle.INITIALIZED;
    }

    @Override
    public void prepare() throws ApplicationInitalizingException {
        switch (this.lifecycle) {
            case CREATED:
                beforeAll();
            case INITIALIZED:
                this.lifecycle = ApplicationLifecycle.PREPARING;
                break;
            case INITIALIZING:
            case PREPARING:
            case PREPARED:
            case STARTING:
            case STARTED:
            case STOPPING:
            case STOPPED:
            case DESTROYING:
            case DESTROYED:
            case TERMINATED:
            default:
                return;
        }
        try {
            // TODO prepare application
        } catch (Exception e) {
            this.lifecycle = ApplicationLifecycle.TERMINATED;
            throw new ApplicationInitalizingException(e);
        }

        this.lifecycle = ApplicationLifecycle.PREPARED;
    }

    @Override
    public ComponentConfigurationContext run() throws ApplicationRunTimeException, ApplicationInitalizingException {
        switch (this.lifecycle) {
            case CREATED:
                beforeAll();
            case INITIALIZED:
                prepare();
            case PREPARED:
                this.lifecycle = ApplicationLifecycle.STARTING;
                break;
            case INITIALIZING:
            case PREPARING:
            case STARTING:
            case STARTED:
            case STOPPING:
            case STOPPED:
            case DESTROYING:
            case DESTROYED:
            case TERMINATED:
            default:
                return this.context;
        }
        try {
            new Thread(() -> {
                javafx.application.Application.launch(appClass);
            }).start();
        } catch (Exception e) {
            this.lifecycle = ApplicationLifecycle.TERMINATED;
            throw e;
        }
        this.lifecycle = ApplicationLifecycle.STARTED;
        return this.context;
    }

    @Override
    public void stop() throws ApplicationRunTimeException {
        switch (this.lifecycle) {
            case STARTED:
                this.lifecycle = ApplicationLifecycle.STOPPING;
                break;
            case CREATED:
            case INITIALIZING:
            case INITIALIZED:
            case PREPARING:
            case PREPARED:
            case STARTING:
            case STOPPING:
            case STOPPED:
            case DESTROYING:
            case DESTROYED:
            case TERMINATED:
            default:
                return;
        }
        try {
            Platform.exit();
        } catch (Exception e) {
            this.lifecycle = ApplicationLifecycle.TERMINATED;
            throw e;
        }
        this.lifecycle = ApplicationLifecycle.STOPPED;
    }

    @Override
    public void restart() throws ApplicationRunTimeException, ApplicationInitalizingException {
        switch (this.lifecycle) {
            case STARTED:
                break;
            case CREATED:
            case INITIALIZING:
            case INITIALIZED:
            case PREPARING:
            case PREPARED:
            case STARTING:
            case STOPPING:
            case STOPPED:
            case DESTROYING:
            case DESTROYED:
            case TERMINATED:
            default:
                return;
        }
        stop();
        run();
    }

    @Override
    public void destory() throws ApplicationRunTimeException {
        switch (this.lifecycle) {
            case STARTED:
                stop();
            case CREATED:
            case INITIALIZED:
            case PREPARED:
            case STOPPED:
                this.lifecycle = ApplicationLifecycle.DESTROYING;
                break;
            case INITIALIZING:
            case PREPARING:
            case STARTING:
            case STOPPING:
            case DESTROYING:
            case DESTROYED:
            case TERMINATED:
            default:
                return;
        }
        try {
            System.exit(0);
        } catch (Exception e) {
            this.lifecycle = ApplicationLifecycle.TERMINATED;
            throw e;
        }
        this.lifecycle = ApplicationLifecycle.DESTROYED;
    }

    @Override
    public boolean isRunning() {
        switch (this.lifecycle) {
            case CREATED:
            case INITIALIZING:
            case INITIALIZED:
            case PREPARING:
            case PREPARED:
            case STARTING:
            case STARTED:
            case STOPPING:
                return true;
            case STOPPED:
            case DESTROYING:
            case DESTROYED:
            case TERMINATED:
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean isStarted() {
        switch (this.lifecycle) {
            case STARTED:
                return true;
            case CREATED:
            case INITIALIZING:
            case INITIALIZED:
            case PREPARING:
            case PREPARED:
            case STARTING:
            case STOPPING:
            case STOPPED:
            case DESTROYING:
            case DESTROYED:
            case TERMINATED:
            default:
                break;
        }
        return false;
    }

}
