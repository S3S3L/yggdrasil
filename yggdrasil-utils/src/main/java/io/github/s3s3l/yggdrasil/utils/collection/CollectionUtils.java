package io.github.s3s3l.yggdrasil.utils.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import io.github.s3s3l.yggdrasil.bean.TreeNode;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * <p>
 * </p>
 * ClassName:CollectionUtils <br>
 * Date: Sep 1, 2016 9:28:13 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class CollectionUtils {

    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(final Object[] arr) {
        return arr == null || arr.length <= 0;
    }

    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean isNotEmpty(final Object[] arr) {
        return !isEmpty(arr);
    }

    public static <T extends TreeNode<T>> List<T> flat(TreeNode<T> node) {
        List<T> children = node.getChildren();

        if (children == null || children.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> result = new LinkedList<>(children);

        for (T child : node.getChildren()) {
            result.addAll(flat(child));
        }

        return result;
    }

    @SafeVarargs
    public static <T> T getFirst(T... collection) {
        if (collection == null) {
            return null;
        }
        Optional<T> opt;
        try {
            opt = Arrays.stream(collection)
                    .findFirst();
        } catch (NullPointerException e) {
            return null;
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        return null;
    }

    public static <T> T getFirst(Collection<T> collection) {
        if (collection == null) {
            return null;
        }
        Optional<T> opt;
        try {
            opt = collection.stream()
                    .findFirst();
        } catch (NullPointerException e) {
            return null;
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        return null;
    }

    public static <T> Optional<T> getFirstOptional(Collection<T> collection) {
        if (collection == null) {
            return Optional.empty();
        }
        return collection.stream()
                .findFirst();
    }

    public static <T> T getFirst(Collection<T> collection, Predicate<? super T> predicate) {
        if (collection == null) {
            return null;
        }
        Optional<T> opt;
        try {
            opt = collection.stream()
                    .filter(predicate)
                    .findFirst();
        } catch (NullPointerException e) {
            return null;
        }
        if (opt.isPresent()) {
            return opt.get();
        }

        return null;

    }

    public static <T> Optional<T> getFirstOptional(Collection<T> collection, Predicate<? super T> predicate) {
        Verify.notNull(collection);
        Verify.notNull(predicate);
        return collection.stream()
                .filter(predicate)
                .findFirst();
    }

    public static <T> boolean equals(Collection<T> left, Collection<T> right) {
        if (isEmpty(left) && isEmpty(right)) {
            return true;
        }

        if (isEmpty(left) || isEmpty(right) || left.size() != right.size()) {
            return false;
        }

        Iterator<T> iLeft = left.iterator();
        Iterator<T> iRight = right.iterator();

        while (iLeft.hasNext()) {
            T l = iLeft.next();
            T r = iRight.next();

            if (!Objects.equals(l, r)) {
                return false;
            }
        }

        return true;
    }
}
