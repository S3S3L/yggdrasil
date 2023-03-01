package io.github.s3s3l.yggdrasil.register.core.register;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.register.core.event.Event;
import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.register.core.register.exception.RegisterException;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.Scanner;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RegisterFactoryManager {
    private final Map<String, RegisterFactory> facotries = new ConcurrentHashMap<>();
    private final Map<String, Register> registers = new ConcurrentHashMap<>();

    public void init(String... packages) {
        Scanner scanner = new ClassScanner();
        scanner.scan(packages)
                .stream()
                .filter(RegisterFactory.class::isAssignableFrom)
                .filter(type -> !type.isInterface() && !type.isEnum() && !Modifier.isAbstract(type.getModifiers()))
                .forEach(r -> {
                    try {
                        RegisterFactory rf = (RegisterFactory) r.getConstructor()
                                .newInstance();
                        facotries.put(rf.getScheme(), rf);
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        throw new RegisterException(e);
                    }
                });
    }

    /**
     * 获取注册中心实例
     * 
     * @param <N>
     *            节点信息类型
     * @param <D>
     *            数据信息类型
     * @param <ET>
     *            事件枚举类型
     * @param <E>
     *            事件类型
     * @param register
     * @return
     */
    public <N extends Node, D, ET extends Enum<?>, E extends Event<ET, D>> Register<N, D, ET, E> getRegister(
            String register) {
        Verify.hasText(register);

        return registers.computeIfAbsent(register, r -> {
            try {
                URI uri = new URI(register);
                String scheme = uri.getScheme();
                if (!facotries.containsKey(scheme)) {
                    throw new RegisterException("RegisterFactory not found for '" + scheme + "'");
                }

                return facotries.get(scheme)
                        .getRegister(uri);
            } catch (URISyntaxException e) {
                throw new RegisterException(e);
            }
        });

    }
}
