package io.github.s3s3l.yggdrasil.bean;

import java.util.List;

/**
 * <p>
 * </p>
 * ClassName:TreeNode <br>
 * Date: Aug 21, 2018 5:42:32 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface TreeNode<T extends TreeNode<T>> {

    List<T> getChildren();

    void setChildren(List<T> children);
}
