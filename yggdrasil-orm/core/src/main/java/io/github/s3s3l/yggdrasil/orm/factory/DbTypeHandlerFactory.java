package io.github.s3s3l.yggdrasil.orm.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.exception.DataBindExpressException;
import io.github.s3s3l.yggdrasil.orm.exception.RemoteMetaManagerGenerateException;
import io.github.s3s3l.yggdrasil.orm.meta.type.DbTypeHandler;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;

public class DbTypeHandlerFactory {

    public static DbTypeHandlerFactory DEFAULT = new DbTypeHandlerFactory();

    static {
        DEFAULT.initDefault();
    }

    private final Map<DatabaseType, DbTypeHandler> DBTYPE_HANDLERS = new ConcurrentHashMap<>();

    private boolean inited = false;

    public synchronized void initDefault() {
        if (inited) {
            return;
        }
        new ClassScanner().scan("io.github.s3s3l.yggdrasil.orm")
                .stream()
                .filter(c -> DbTypeHandler.class.isAssignableFrom(c) && !c.isInterface() && !c.isEnum()
                        && !Modifier.isAbstract(c.getModifiers()))
                .map(c -> {
                    try {
                        return (DbTypeHandler) c.getConstructor()
                                .newInstance();
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        throw new DataBindExpressException(e);
                    }
                })
                .forEach(this::register);
        inited = true;
    }

    public void register(DbTypeHandler dbTypeHandler) {
        DBTYPE_HANDLERS.put(dbTypeHandler.databaseType(), dbTypeHandler);
    }

    public DbTypeHandler getInstance(DatabaseType databaseType) {
        return DBTYPE_HANDLERS.computeIfAbsent(databaseType, k -> {
            throw new RemoteMetaManagerGenerateException(
                    "DbTypeHandler not found for database type: " + databaseType);
        });
    }
    
}
