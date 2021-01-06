package org.s3s3l.yggdrasil.doc.node;

import java.util.LinkedList;
import java.util.SortedSet;

import org.s3s3l.yggdrasil.doc.bean.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DocNode implements Order, Comparable<DocNode> {
    /**
     * 从父节点到根节点的路径 parent, parent`s parent, ..., root
     */
    private LinkedList<String> ancestors;
    /**
     * 主标题
     * 
     * @name
     */
    private String name;
    /**
     * 作为副标题
     * 
     * @literal
     */
    private String literal;
    /**
     * 正文内容 javadoc正文
     */
    private String comment;
    /**
     * 作者
     * 
     * @author
     */
    private String author;
    /**
     * 过期描述
     * 
     * @deprecated
     */
    private String deprecated;
    /**
     * 版本号
     * 
     * @version
     */
    private String version;
    /**
     * 子节点
     */
    private SortedSet<DocNode> children;
    /**
     * 是否是Api详情节点
     */
    @Builder.Default
    private boolean isApi = false;

    /**
     * 排序位
     */
    @Builder.Default
    private int order = 0;

    @Override
    public int compareTo(DocNode o) {
        return this.order - o.order;
    }

}
