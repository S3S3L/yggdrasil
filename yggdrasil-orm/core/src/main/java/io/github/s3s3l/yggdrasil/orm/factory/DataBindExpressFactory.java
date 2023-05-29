package io.github.s3s3l.yggdrasil.orm.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import io.github.s3s3l.yggdrasil.orm.bind.express.ExpressBuilderType;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.exception.DataBindExpressException;
import io.github.s3s3l.yggdrasil.orm.exception.DataBindExpressNotFoundException;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataBindExpressFactory {

    private final Map<ExpressBuilderType, Map<DatabaseType, DataBindExpress>> DATA_BIND_EXPRESSES = new ConcurrentHashMap<>();

    private boolean inited = false;

    public DataBindExpressFactory(MetaManager metaManager, DbTypeHandlerFactory dbTypeHandlerFactory) {
        initDefault(metaManager, dbTypeHandlerFactory);
    }

    public synchronized void initDefault(MetaManager metaManager, DbTypeHandlerFactory dbTypeHandlerFactory) {
        if (inited) {
            return;
        }
        new ClassScanner().scan("io.github.s3s3l.yggdrasil.orm")
                .stream()
                .filter(c -> DataBindExpress.class.isAssignableFrom(c) && !c.isInterface() && !c.isEnum()
                        && !Modifier.isAbstract(c.getModifiers()))
                .map(c -> {
                    try {
                        return (DataBindExpress) c.getConstructor(MetaManager.class, 
                                DbTypeHandlerFactory.class)
                                .newInstance(metaManager, dbTypeHandlerFactory);
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        throw new DataBindExpressException(e);
                    }
                })
                .forEach(this::register);
        inited = true;
    }

    public synchronized void register(DataBindExpress dataBindExpress) {
        DATA_BIND_EXPRESSES.computeIfAbsent(dataBindExpress.expressBuilderType(), k -> new ConcurrentHashMap<>())
                .put(dataBindExpress.databaseType(), dataBindExpress);
    }

    public DataBindExpress getInstance(DatabaseType databaseType, ExpressBuilderType expressBuilderType) {
        return DATA_BIND_EXPRESSES.computeIfAbsent(expressBuilderType, k -> {
            throw new DataBindExpressNotFoundException(
                    "DataBindExpress not found for express builder type: " + expressBuilderType);
        })
                .computeIfAbsent(databaseType, k -> {
                    throw new DataBindExpressNotFoundException(
                            "DataBindExpress not found for database type: " + databaseType);
                });
    }
}
