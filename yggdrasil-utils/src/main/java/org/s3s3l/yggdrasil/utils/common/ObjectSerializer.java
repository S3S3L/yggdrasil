package org.s3s3l.yggdrasil.utils.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.s3s3l.yggdrasil.bean.exception.ObjectSerializeException;

/**
 * 
 * <p>
 * </p>
 * ClassName: ObjectSerializer <br>
 * date: Sep 20, 2019 4:46:07 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class ObjectSerializer {

    public static byte[] serialize(Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new ObjectSerializeException(e);
        }
    }

    public static Object desrialize(byte[] bytes) {
        try (ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new ObjectSerializeException(e);
        }
    }
}
