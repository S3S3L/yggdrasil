/** 
 * Project Name:utils 
 * File Name:ResourceLoader.java 
 * Package Name:com.s3s3l.utils.resource 
 * Date:2016年6月27日下午6:36:42 
 * Copyright (c) 2016, kehw.zwei@gmail.com All Rights Reserved. 
 * 
*/

package io.github.s3s3l.yggdrasil.resource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * 
 * <p>
 * </p>
 * ClassName: ResourceLoader <br>
 * date: Sep 20, 2019 11:25:31 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface ResourceLoader {

    /**
     * 
     * load configuration resource
     * 
     * @param <T>
     *                 type
     * @param resource
     *                 configuration resource
     * @param type
     *                 target type
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(String resource, Class<T> type) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resource
     * 
     * @param <T>
     *                 type
     * @param resource
     *                 configuration resource
     * @param type
     *                 target type
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(File resource, Class<T> type) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                  type
     * @param type
     *                  target type
     * @param resources
     *                  configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(Class<T> type, File... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                       type
     * @param type
     *                       target type
     * @param checkFileExist
     *                       if true, will throw a FileNotFoundException when a
     *                       resource
     *                       file not found; if false, will not check file and
     *                       continue
     *                       loading configuration.
     * @param resources
     *                       configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(Class<T> type, boolean checkFileExist, File... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                  type
     * @param type
     *                  target type
     * @param resources
     *                  configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(Class<T> type, String... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resource
     * 
     * @param <T>
     *                 type
     * @param resource
     *                 configuration resource
     * @param type
     *                 target type
     * @param profile
     *                 configuration profile
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(String resource, Class<T> type, String profile) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resource
     * 
     * @param <T>
     *                 type
     * @param resource
     *                 configuration resource
     * @param type
     *                 target type
     * @param profile
     *                 configuration profile
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(File resource, Class<T> type, String profile) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                  type
     * @param type
     *                  target type
     * @param profile
     *                  configuration profile
     * @param resources
     *                  configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(Class<T> type, String profile, File... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                       type
     * @param type
     *                       target type
     * @param checkFileExist
     *                       if true, will throw a FileNotFoundException when a
     *                       resource
     *                       file not found; if false, will not check file and
     *                       continue
     *                       loading configuration.
     * @param profile
     *                       configuration profile
     * @param resources
     *                       configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(Class<T> type, boolean checkFileExist, String profile, File... resources)
            throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                  type
     * @param type
     *                  target type
     * @param profile
     *                  configuration profile
     * @param resources
     *                  configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(Class<T> type, String profile, String... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resource
     * 
     * @param <T>
     *                 type
     * @param object
     *                 target object
     * @param resource
     *                 configuration resource
     * @param type
     *                 target type
     * @return instance of type
     * @throws IOException
     *                                {@link IOException}
     * @throws InstantiationException
     *                                {@link InstantiationException}
     * @throws IllegalAccessException
     *                                {@link IllegalAccessException}
     * @since JDK 1.8
     */
    <T> T loadConfiguration(T object, String resource, Class<T> type) throws IOException,
            InstantiationException,
            IllegalAccessException;

    /**
     * 
     * load configuration resource
     * 
     * @param <T>
     *                 type
     * @param object
     *                 target object
     * @param resource
     *                 configuration resource
     * @param type
     *                 target type
     * @return instance of type
     * @throws IOException
     *                                {@link IOException}
     * @throws InstantiationException
     *                                {@link InstantiationException}
     * @throws IllegalAccessException
     *                                {@link IllegalAccessException}
     * @since JDK 1.8
     */
    <T> T loadConfiguration(T object, File resource, Class<T> type) throws IOException,
            InstantiationException,
            IllegalAccessException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                  type
     * @param object
     *                  target object
     * @param type
     *                  target type
     * @param resources
     *                  configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(T object, Class<T> type, File... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                       type
     * @param object
     *                       target object
     * @param type
     *                       target type
     * @param checkFileExist
     *                       if true, will throw a FileNotFoundException when a
     *                       resource
     *                       file not found; if false, will not check file and
     *                       continue
     *                       loading configuration.
     * @param resources
     *                       configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(T object, Class<T> type, boolean checkFileExist, File... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                  type
     * @param object
     *                  target object
     * @param type
     *                  target type
     * @param resources
     *                  configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(T object, Class<T> type, String... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resource
     * 
     * @param <T>
     *                 type
     * @param object
     *                 target object
     * @param resource
     *                 configuration resource
     * @param type
     *                 target type
     * @param profile
     *                 configuration profile
     * @return instance of type
     * @throws IOException
     *                                {@link IOException}
     * @throws InstantiationException
     *                                {@link InstantiationException}
     * @throws IllegalAccessException
     *                                {@link IllegalAccessException}
     * @since JDK 1.8
     */
    <T> T loadConfiguration(T object, String resource, Class<T> type, String profile) throws IOException,
            InstantiationException,
            IllegalAccessException;

    /**
     * 
     * load configuration resource
     * 
     * @param <T>
     *                 type
     * @param object
     *                 target object
     * @param resource
     *                 configuration resource
     * @param type
     *                 target type
     * @param profile
     *                 configuration profile
     * @return instance of type
     * @throws IOException
     *                                {@link IOException}
     * @throws InstantiationException
     *                                {@link InstantiationException}
     * @throws IllegalAccessException
     *                                {@link IllegalAccessException}
     * @since JDK 1.8
     */
    <T> T loadConfiguration(T object, File resource, Class<T> type, String profile) throws IOException,
            InstantiationException,
            IllegalAccessException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                  type
     * @param object
     *                  target object
     * @param type
     *                  target type
     * @param profile
     *                  configuration profile
     * @param resources
     *                  configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(T object, Class<T> type, String profile, File... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                       type
     * @param object
     *                       target object
     * @param type
     *                       target type
     * @param checkFileExist
     *                       if true, will throw a FileNotFoundException when a
     *                       resource
     *                       file not found; if false, will not check file and
     *                       continue
     *                       loading configuration.
     * @param profile
     *                       configuration profile
     * @param resources
     *                       configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(T object, Class<T> type, boolean checkFileExist, String profile, File... resources)
            throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;

    /**
     * 
     * load configuration resources
     * 
     * @param <T>
     *                  type
     * @param object
     *                  target object
     * @param type
     *                  target type
     * @param profile
     *                  configuration profile
     * @param resources
     *                  configuration resources
     * @return instance of type
     * @throws IOException
     *                                   {@link IOException}
     * @throws InstantiationException
     *                                   {@link InstantiationException}
     * @throws IllegalAccessException
     *                                   {@link IllegalAccessException}
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @since JDK 1.8
     */
    <T> T loadConfiguration(T object, Class<T> type, String profile, String... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException;
}
