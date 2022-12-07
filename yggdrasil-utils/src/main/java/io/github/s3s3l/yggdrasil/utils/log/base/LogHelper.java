package io.github.s3s3l.yggdrasil.utils.log.base;

import java.io.PrintWriter;
import java.io.StringWriter;

import io.github.s3s3l.yggdrasil.utils.stuctural.StructuralHelper;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.s3s3l.yggdrasil.utils.log.ILogger;

/**
 * ClassName:LogUtilsBase <br>
 * Date: 2016年3月25日 下午3:08:15 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class LogHelper implements ILogger {
    public static final String BREAKLINE = ";; ";
    public static final String SEPARATOR = " >> ";
    public static final String SUB_SEPARATOR = " :: ";
    protected static final String DEFAULT_TRACE_LOGGER = "debug";
    protected static final String DEFAULT_DEBUG_LOGGER = "debug";
    protected static final String DEFAULT_INFO_LOGGER = "info";
    protected static final String DEFAULT_WARN_LOGGER = "warn";
    protected static final String DEFAULT_ERROR_LOGGER = "error";
    private static final StructuralHelper json = JacksonUtils.JSON;
    private final LogLevel logLevel;

    private Logger logger;

    private LogHelper(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public static LogHelper create(LogLevel logLevel) {
        return new LogHelper(logLevel);
    }

    public static Logger getLogger(Class<?> cls) {
        return LoggerFactory.getLogger(cls);
    }

    /**
     * 获取logger对象
     * 
     * @param loggerName
     *            logger的名字
     * @return Logger
     */
    private Logger getLogger(String loggerName) {
        if (logger == null) {
            logger = LoggerFactory.getLogger(loggerName);
        }
        return logger;
    }

    @Override
    public ILogger logger(String loggerName) {
        this.logger = getLogger(loggerName);
        return this;
    }

    private void logLocal(LogLevel level, String content) {
        switch (level) {
            case DEBUG:
                logger = getLogger(DEFAULT_DEBUG_LOGGER);
                logger.debug(content);
                break;
            case ERROR:
                logger = getLogger(DEFAULT_ERROR_LOGGER);
                logger.error(content);
                break;
            case INFO:
                logger = getLogger(DEFAULT_INFO_LOGGER);
                logger.info(content);
                break;
            case TRACE:
                logger = getLogger(DEFAULT_TRACE_LOGGER);
                logger.trace(content);
                break;
            case WARN:
                logger = getLogger(DEFAULT_WARN_LOGGER);
                logger.warn(content);
                break;
            default:
                break;
        }

        this.logger = null;
    }

    private void doLog(LogLevel level, String content) {
        if (logLevel.ordinal() > level.ordinal()) {
            return;
        }
        logLocal(level, content);
    }

    /**
     * 创建日志文本
     * 
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName argName : arg;
     */
    protected void log(LogLevel level, String argName, Object arg) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement stack = getStackTraceElement();
        sb.append(stack.getClassName())
                .append(SEPARATOR)
                .append(stack.getMethodName())
                .append(SEPARATOR)
                .append(argName)
                .append(SUB_SEPARATOR)
                .append(json.toStructuralString(arg))
                .append(BREAKLINE);
        String content = sb.toString();
        doLog(level, content);
    }

    /**
     * 创建日志文本
     * 
     * @param msg
     *            消息
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName Message : msg; argName : arg;
     */
    protected void log(LogLevel level, String msg, String argName, Object arg) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement stack = getStackTraceElement();
        sb.append(stack.getClassName())
                .append(SEPARATOR)
                .append(stack.getMethodName())
                .append(SEPARATOR)
                .append("Message")
                .append(SUB_SEPARATOR)
                .append(msg)
                .append(BREAKLINE)
                .append(argName)
                .append(SUB_SEPARATOR)
                .append(json.toStructuralString(arg))
                .append(BREAKLINE);
        String content = sb.toString();
        doLog(level, content);
    }

    /**
     * 创建日志文本
     * 
     * @param msg
     *            消息
     * @return ClassName MethodName msg;
     */
    protected void log(LogLevel level, String msg) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement stack = getStackTraceElement();
        sb.append(stack.getClassName())
                .append(SEPARATOR)
                .append(stack.getMethodName())
                .append(SEPARATOR)
                .append(msg)
                .append(BREAKLINE);
        String content = sb.toString();
        doLog(level, content);
    }

    /**
     * 创建日志文本
     * 
     * @param argNames
     *            参数名称
     * @param args
     *            参数内容11对应
     * @return ClassName MethodName argName1 : arg1; argName2 : arg2; ...
     */
    protected void log(LogLevel level, String[] argNames, Object... args) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement stack = getStackTraceElement();
        sb.append(stack.getClassName())
                .append(SEPARATOR)
                .append(stack.getMethodName())
                .append(SEPARATOR);
        for (int i = 0; i < argNames.length; i++) {
            if (args.length <= i) {
                break;
            }
            sb.append(argNames[i])
                    .append(SUB_SEPARATOR)
                    .append(json.toStructuralString(args[i]))
                    .append(BREAKLINE);
        }
        String content = sb.toString();
        doLog(level, content);
    }

    /**
     * 创建日志文本
     * 
     * @param msg
     *            消息
     * @param argNames
     *            参数名称
     * @param args
     *            参数内容11对应
     * @return ClassName MethodName Message : msg; argName1 : arg1 argName2 :
     *         arg2; ...
     */
    protected void log(LogLevel level, String msg, String[] argNames, Object... args) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement stack = getStackTraceElement();
        sb.append(stack.getClassName())
                .append(SEPARATOR)
                .append(stack.getMethodName())
                .append(SEPARATOR)
                .append("Message")
                .append(SUB_SEPARATOR)
                .append(msg)
                .append(BREAKLINE);
        for (int i = 0; i < argNames.length; i++) {
            if (args.length <= i) {
                break;
            }
            sb.append(argNames[i])
                    .append(SUB_SEPARATOR)
                    .append(json.toStructuralString(args[i]))
                    .append(BREAKLINE);
        }
        String content = sb.toString();
        doLog(level, content);
    }

    /**
     * 创建日志文本
     * 
     * @param e
     *            异常
     * @return ClassName MethodName 异常堆栈信息;
     */
    protected void log(LogLevel level, Throwable e) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement stack = getStackTraceElement();
        sb.append(stack.getClassName())
                .append(SEPARATOR)
                .append(stack.getMethodName())
                .append(SEPARATOR);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        sb.append(e.getClass()
                .getName())
                .append(SUB_SEPARATOR)
                .append(e.getMessage())
                .append(BREAKLINE)
                .append(sw.toString())
                .append(BREAKLINE);
        String content = sb.toString();
        doLog(level, content);
    }

    /**
     * 
     * 创建日志文本
     * 
     * @author kehw_zwei
     * @param e
     *            异常
     * @param tag
     *            标记
     * @return ClassName MethodName 异常堆栈信息;
     * @since JDK 1.8
     */
    protected void log(LogLevel level, Throwable e, String tag) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement stack = getStackTraceElement();
        sb.append(stack.getClassName())
                .append(SEPARATOR)
                .append(stack.getMethodName())
                .append(SEPARATOR)
                .append("tag")
                .append(SUB_SEPARATOR)
                .append(tag)
                .append(BREAKLINE);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        sb.append(e.getClass()
                .getName())
                .append(SUB_SEPARATOR)
                .append(e.getMessage())
                .append(BREAKLINE)
                .append(sw.toString())
                .append(BREAKLINE);
        String content = sb.toString();
        doLog(level, content);
    }

    private StackTraceElement getStackTraceElement() {
        return Thread.currentThread()
                .getStackTrace()[4];
    }

    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR;
    }

    @Override
    public void trace(String argName, Object arg) {
        log(LogLevel.TRACE, argName, arg);
    }

    @Override
    public void trace(String msg, String argName, Object arg) {
        log(LogLevel.TRACE, msg, argName, arg);
    }

    @Override
    public void trace(String msg) {
        log(LogLevel.TRACE, msg);
    }

    @Override
    public void trace(String[] argNames, Object... args) {
        log(LogLevel.TRACE, argNames, args);
    }

    @Override
    public void trace(String msg, String[] argNames, Object... args) {
        log(LogLevel.TRACE, msg, argNames, args);
    }

    @Override
    public void trace(Throwable e) {
        log(LogLevel.TRACE, e);
    }

    @Override
    public void trace(Throwable e, String tag) {
        log(LogLevel.TRACE, e, tag);
    }

    @Override
    public void debug(String argName, Object arg) {
        log(LogLevel.DEBUG, argName, arg);
    }

    @Override
    public void debug(String msg, String argName, Object arg) {
        log(LogLevel.DEBUG, msg, argName, arg);
    }

    @Override
    public void debug(String msg) {
        log(LogLevel.DEBUG, msg);
    }

    @Override
    public void debug(String[] argNames, Object... args) {
        log(LogLevel.DEBUG, argNames, args);
    }

    @Override
    public void debug(String msg, String[] argNames, Object... args) {
        log(LogLevel.DEBUG, msg, argNames, args);
    }

    @Override
    public void debug(Throwable e) {
        log(LogLevel.DEBUG, e);
    }

    @Override
    public void debug(Throwable e, String tag) {
        log(LogLevel.DEBUG, e, tag);
    }

    @Override
    public void info(String argName, Object arg) {
        log(LogLevel.INFO, argName, arg);
    }

    @Override
    public void info(String msg, String argName, Object arg) {
        log(LogLevel.INFO, msg, argName, arg);
    }

    @Override
    public void info(String msg) {
        log(LogLevel.INFO, msg);
    }

    @Override
    public void info(String[] argNames, Object... args) {
        log(LogLevel.INFO, argNames, args);
    }

    @Override
    public void info(String msg, String[] argNames, Object... args) {
        log(LogLevel.INFO, msg, argNames, args);
    }

    @Override
    public void info(Throwable e) {
        log(LogLevel.INFO, e);
    }

    @Override
    public void info(Throwable e, String tag) {
        log(LogLevel.INFO, e, tag);
    }

    @Override
    public void warn(String argName, Object arg) {
        log(LogLevel.WARN, argName, arg);
    }

    @Override
    public void warn(String msg, String argName, Object arg) {
        log(LogLevel.WARN, msg, argName, arg);
    }

    @Override
    public void warn(String msg) {
        log(LogLevel.WARN, msg);
    }

    @Override
    public void warn(String[] argNames, Object... args) {
        log(LogLevel.WARN, argNames, args);
    }

    @Override
    public void warn(String msg, String[] argNames, Object... args) {
        log(LogLevel.WARN, msg, argNames, args);
    }

    @Override
    public void warn(Throwable e) {
        log(LogLevel.WARN, e);
    }

    @Override
    public void warn(Throwable e, String tag) {
        log(LogLevel.WARN, e, tag);
    }

    @Override
    public void error(String argName, Object arg) {
        log(LogLevel.ERROR, argName, arg);
    }

    @Override
    public void error(String msg, String argName, Object arg) {
        log(LogLevel.ERROR, msg, argName, arg);
    }

    @Override
    public void error(String msg) {
        log(LogLevel.ERROR, msg);
    }

    @Override
    public void error(String[] argNames, Object... args) {
        log(LogLevel.ERROR, argNames, args);
    }

    @Override
    public void error(String msg, String[] argNames, Object... args) {
        log(LogLevel.ERROR, msg, argNames, args);
    }

    @Override
    public void error(Throwable e) {
        log(LogLevel.ERROR, e);
    }

    @Override
    public void error(Throwable e, String tag) {
        log(LogLevel.ERROR, e, tag);
    }

    @Override
    public LogLevel logLevel() {
        return this.logLevel;
    }

}
