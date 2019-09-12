package org.s3s3l.yggdrasil.utils.log;

import org.s3s3l.yggdrasil.utils.log.base.LogHelper.LogLevel;

/**
 * ClassName:ILogger <br>
 * Date: 2016年4月25日 下午4:38:43 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface ILogger {
    
    ILogger logger(String loggerName);
    
    LogLevel logLevel();

    /**
     * 记录trace级别日志
     * 
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName argName : arg;
     */
    void trace(String argName, Object arg);

    /**
     * 记录trace级别日志
     * 
     * @param msg
     *            消息
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName Message : msg; argName : arg;
     */
    void trace(String msg, String argName, Object arg);

    /**
     * 记录trace级别日志
     * 
     * @param msg
     *            消息
     * @return ClassName MethodName msg;
     */
    void trace(String msg);

    /**
     * 记录trace级别日志
     * 
     * @param argNames
     *            参数名称
     * @param args
     *            参数内容11对应
     * @return ClassName MethodName argName1 : arg1; argName2 : arg2; ...
     */
    void trace(String[] argNames, Object... args);

    /**
     * 记录trace级别日志
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
    void trace(String msg, String[] argNames, Object... args);

    /**
     * 记录trace级别日志
     * 
     * @param e
     *            异常
     * @return ClassName MethodName 异常堆栈信息;
     */
    void trace(Throwable e);

    /**
     * 
     * 记录trace级别日志
     * 
     * @author kehw_zwei
     * @param e
     *            异常
     * @param tag
     *            标记
     * @return ClassName MethodName 异常堆栈信息;
     * @since JDK 1.8
     */
    void trace(Throwable e, String tag);
    
    /**
     * 记录debug级别日志
     * 
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName argName : arg;
     */
    void debug(String argName, Object arg);

    /**
     * 记录debug级别日志
     * 
     * @param msg
     *            消息
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName Message : msg; argName : arg;
     */
    void debug(String msg, String argName, Object arg);

    /**
     * 记录debug级别日志
     * 
     * @param msg
     *            消息
     * @return ClassName MethodName msg;
     */
    void debug(String msg);

    /**
     * 记录debug级别日志
     * 
     * @param argNames
     *            参数名称
     * @param args
     *            参数内容11对应
     * @return ClassName MethodName argName1 : arg1; argName2 : arg2; ...
     */
    void debug(String[] argNames, Object... args);

    /**
     * 记录debug级别日志
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
    void debug(String msg, String[] argNames, Object... args);

    /**
     * 记录debug级别日志
     * 
     * @param e
     *            异常
     * @return ClassName MethodName 异常堆栈信息;
     */
    void debug(Throwable e);

    /**
     * 
     * 记录debug级别日志
     * 
     * @author kehw_zwei
     * @param e
     *            异常
     * @param tag
     *            标记
     * @return ClassName MethodName 异常堆栈信息;
     * @since JDK 1.8
     */
    void debug(Throwable e, String tag);
    
    /**
     * 记录info级别日志
     * 
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName argName : arg;
     */
    void info(String argName, Object arg);

    /**
     * 记录info级别日志
     * 
     * @param msg
     *            消息
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName Message : msg; argName : arg;
     */
    void info(String msg, String argName, Object arg);

    /**
     * 记录info级别日志
     * 
     * @param msg
     *            消息
     * @return ClassName MethodName msg;
     */
    void info(String msg);

    /**
     * 记录info级别日志
     * 
     * @param argNames
     *            参数名称
     * @param args
     *            参数内容11对应
     * @return ClassName MethodName argName1 : arg1; argName2 : arg2; ...
     */
    void info(String[] argNames, Object... args);

    /**
     * 记录info级别日志
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
    void info(String msg, String[] argNames, Object... args);

    /**
     * 记录info级别日志
     * 
     * @param e
     *            异常
     * @return ClassName MethodName 异常堆栈信息;
     */
    void info(Throwable e);

    /**
     * 
     * 记录info级别日志
     * 
     * @author kehw_zwei
     * @param e
     *            异常
     * @param tag
     *            标记
     * @return ClassName MethodName 异常堆栈信息;
     * @since JDK 1.8
     */
    void info(Throwable e, String tag);
    
    /**
     * 记录warn级别日志
     * 
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName argName : arg;
     */
    void warn(String argName, Object arg);

    /**
     * 记录warn级别日志
     * 
     * @param msg
     *            消息
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName Message : msg; argName : arg;
     */
    void warn(String msg, String argName, Object arg);

    /**
     * 记录warn级别日志
     * 
     * @param msg
     *            消息
     * @return ClassName MethodName msg;
     */
    void warn(String msg);

    /**
     * 记录warn级别日志
     * 
     * @param argNames
     *            参数名称
     * @param args
     *            参数内容11对应
     * @return ClassName MethodName argName1 : arg1; argName2 : arg2; ...
     */
    void warn(String[] argNames, Object... args);

    /**
     * 记录warn级别日志
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
    void warn(String msg, String[] argNames, Object... args);

    /**
     * 记录warn级别日志
     * 
     * @param e
     *            异常
     * @return ClassName MethodName 异常堆栈信息;
     */
    void warn(Throwable e);

    /**
     * 
     * 记录warn级别日志
     * 
     * @author kehw_zwei
     * @param e
     *            异常
     * @param tag
     *            标记
     * @return ClassName MethodName 异常堆栈信息;
     * @since JDK 1.8
     */
    void warn(Throwable e, String tag);
    
    /**
     * 记录error级别日志
     * 
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName argName : arg;
     */
    void error(String argName, Object arg);

    /**
     * 记录error级别日志
     * 
     * @param msg
     *            消息
     * @param argName
     *            参数名称
     * @param arg
     *            参数内容
     * @return ClassName MethodName Message : msg; argName : arg;
     */
    void error(String msg, String argName, Object arg);

    /**
     * 记录error级别日志
     * 
     * @param msg
     *            消息
     * @return ClassName MethodName msg;
     */
    void error(String msg);

    /**
     * 记录error级别日志
     * 
     * @param argNames
     *            参数名称
     * @param args
     *            参数内容11对应
     * @return ClassName MethodName argName1 : arg1; argName2 : arg2; ...
     */
    void error(String[] argNames, Object... args);

    /**
     * 记录error级别日志
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
    void error(String msg, String[] argNames, Object... args);

    /**
     * 记录error级别日志
     * 
     * @param e
     *            异常
     * @return ClassName MethodName 异常堆栈信息;
     */
    void error(Throwable e);

    /**
     * 
     * 记录error级别日志
     * 
     * @author kehw_zwei
     * @param e
     *            异常
     * @param tag
     *            标记
     * @return ClassName MethodName 异常堆栈信息;
     * @since JDK 1.8
     */
    void error(Throwable e, String tag);
}
