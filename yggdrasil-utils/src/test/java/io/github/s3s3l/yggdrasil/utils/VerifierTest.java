package io.github.s3s3l.yggdrasil.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.bean.exception.VerifyException;
import io.github.s3s3l.yggdrasil.bean.verify.Examine;
import io.github.s3s3l.yggdrasil.bean.verify.Expectation;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.verify.CommonVerifier;

/**
 * <p>
 * </p>
 * ClassName:VerifierTest <br>
 * Date: Apr 13, 2017 1:53:57 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class VerifierTest {

    @Test
    public void verifierTest() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        verifier.verify(condition, ConditionModel.class);

    }

    @Test
    public void lengthLimitStringNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setLengthLimitString(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void lengthLimitStringSmall() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setLengthLimitString("1");
        verifier.verify(condition, ConditionModel.class);

    }

    @Test
    public void lengthLimitStringLarge() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setLengthLimitString("123");
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void lengthLimitCollectionNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setLengthLimitCollection(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void lengthLimitCollectionSmall() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setLengthLimitCollection(Arrays.asList(1));
        verifier.verify(condition, ConditionModel.class);

    }

    @Test
    public void lengthLimitCollectionLarge() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setLengthLimitCollection(Arrays.asList(1, 2, 3));
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void lengthLimitArrayNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setLengthLimitArray(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void lengthLimitArraySmall() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setLengthLimitArray(new Integer[] { 1 });
        verifier.verify(condition, ConditionModel.class);

    }

    @Test
    public void lengthLimitArrayLarge() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setLengthLimitArray(new Integer[] { 1, 2, 3 });
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void fixedLengthStringNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setFixedLengthString(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void fixedLengthStringSmall() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setFixedLengthString("1");
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void fixedLengthStringLarge() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setFixedLengthString("123");
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void fixedLengthCollectionNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setFixedLengthCollection(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void fixedLengthCollectionSmall() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setFixedLengthCollection(Arrays.asList(1));
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void fixedLengthCollectionLarge() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setFixedLengthCollection(Arrays.asList(1, 2, 3));
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void fixedLengthArrayNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setFixedLengthArray(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void fixedLengthArraySmall() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setFixedLengthArray(new Integer[] { 1 });
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void fixedLengthArrayLarge() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setFixedLengthArray(new Integer[] { 1, 2, 3 });
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void notEmptyCollectionNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setNotEmptyCollection(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void notEmptyCollection() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setNotEmptyCollection(new ArrayList<>());
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void notEmptyArrayNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setNotEmptyArray(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void notEmptyArray() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setNotEmptyArray(new Integer[] {});
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void emptyCollectionNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setEmptyCollection(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void emptyCollection() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setEmptyCollection(Arrays.asList(1));
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void emptyArrayNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setEmptyArray(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void emptyArray() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setEmptyArray(new Integer[] { 1 });
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void hasLengthNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setHasLength(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void hasLength() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setHasLength(StringUtils.EMPTY_STRING);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void nullObject() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setNullObject(new Object());
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void notNullObject() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setNotNullObject(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void typeNull() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setType(null);
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void type() {
        CommonVerifier verifier = new CommonVerifier();

        ConditionModel condition = new ConditionModel();
        condition.setType(Integer.valueOf(1));
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, ConditionModel.class));

    }

    @Test
    public void interfaceCheck() {
        CommonVerifier verifier = new CommonVerifier();

        InterfaceCheck condition = new InterfaceCheck();
        Assertions.assertThrows(VerifyException.class, () -> verifier.verify(condition, InterfaceCheck.class));

    }

    @Examine
    public static class InterfaceCheck {
        @Examine(value = Expectation.IS_INTERFACE)
        private ArrayList<?> arrayList;

        public ArrayList<?> getArrayList() {
            return arrayList;
        }

        public void setArrayList(ArrayList<?> arrayList) {
            this.arrayList = arrayList;
        }
    }

    @Examine
    public static class ConditionModel {

        @Examine(value = Expectation.LENGTH_LIMIT, length = 2)
        private String lengthLimitString = "12";
        @Examine(value = Expectation.LENGTH_LIMIT, length = 2)
        private List<Integer> lengthLimitCollection = Arrays.asList(1, 2);
        @Examine(value = Expectation.LENGTH_LIMIT, length = 2)
        private Integer[] lengthLimitArray = new Integer[] { 1, 2 };
        @Examine(value = Expectation.FIXED_LENGTH, length = 2)
        private String fixedLengthString = "12";
        @Examine(value = Expectation.FIXED_LENGTH, length = 2)
        private List<Integer> fixedLengthCollection = Arrays.asList(1, 2);
        @Examine(value = Expectation.FIXED_LENGTH, length = 2)
        private Integer[] fixedLengthArray = new Integer[] { 1, 2 };
        @Examine(Expectation.NOT_EMPTY)
        private List<Integer> notEmptyCollection = Arrays.asList(1, 2);
        @Examine(Expectation.NOT_EMPTY)
        private Integer[] notEmptyArray = new Integer[] { 1, 2 };
        @Examine(Expectation.EMPTY)
        private List<Integer> emptyCollection = new ArrayList<>();
        @Examine(Expectation.EMPTY)
        private Integer[] emptyArray = new Integer[] {};
        @Examine(Expectation.HAS_LENGTH)
        private String hasLength = "12";
        @Examine(Expectation.NULL)
        private Object nullObject = null;
        @Examine(Expectation.NOT_NULL)
        private Object notNullObject = new Object();
        @Examine(value = Expectation.TYPE, type = String.class)
        private Object type = new String("string");
        @Examine(value = Expectation.IS_INTERFACE)
        private List<?> list;

        public String getLengthLimitString() {
            return lengthLimitString;
        }

        public void setLengthLimitString(String lengthLimitString) {
            this.lengthLimitString = lengthLimitString;
        }

        public List<Integer> getLengthLimitCollection() {
            return lengthLimitCollection;
        }

        public void setLengthLimitCollection(List<Integer> lengthLimitCollection) {
            this.lengthLimitCollection = lengthLimitCollection;
        }

        public Integer[] getLengthLimitArray() {
            return lengthLimitArray;
        }

        public void setLengthLimitArray(Integer[] lengthLimitArray) {
            this.lengthLimitArray = lengthLimitArray;
        }

        public String getFixedLengthString() {
            return fixedLengthString;
        }

        public void setFixedLengthString(String fixedLengthString) {
            this.fixedLengthString = fixedLengthString;
        }

        public List<Integer> getFixedLengthCollection() {
            return fixedLengthCollection;
        }

        public void setFixedLengthCollection(List<Integer> fixedLengthCollection) {
            this.fixedLengthCollection = fixedLengthCollection;
        }

        public Integer[] getFixedLengthArray() {
            return fixedLengthArray;
        }

        public void setFixedLengthArray(Integer[] fixedLengthArray) {
            this.fixedLengthArray = fixedLengthArray;
        }

        public List<Integer> getNotEmptyCollection() {
            return notEmptyCollection;
        }

        public void setNotEmptyCollection(List<Integer> notEmptyCollection) {
            this.notEmptyCollection = notEmptyCollection;
        }

        public Integer[] getNotEmptyArray() {
            return notEmptyArray;
        }

        public void setNotEmptyArray(Integer[] notEmptyArray) {
            this.notEmptyArray = notEmptyArray;
        }

        public List<Integer> getEmptyCollection() {
            return emptyCollection;
        }

        public void setEmptyCollection(List<Integer> emptyCollection) {
            this.emptyCollection = emptyCollection;
        }

        public Integer[] getEmptyArray() {
            return emptyArray;
        }

        public void setEmptyArray(Integer[] emptyArray) {
            this.emptyArray = emptyArray;
        }

        public String getHasLength() {
            return hasLength;
        }

        public void setHasLength(String hasLength) {
            this.hasLength = hasLength;
        }

        public Object getNullObject() {
            return nullObject;
        }

        public void setNullObject(Object nullObject) {
            this.nullObject = nullObject;
        }

        public Object getNotNullObject() {
            return notNullObject;
        }

        public void setNotNullObject(Object notNullObject) {
            this.notNullObject = notNullObject;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public List<?> getList() {
            return list;
        }

        public void setList(List<?> list) {
            this.list = list;
        }

    }
}
