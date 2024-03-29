package io.github.s3s3l.yggdrasil.spring.env;

import java.util.List;
import java.util.Map;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.springframework.core.env.EnumerablePropertySource;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

/**
 * <p>
 * </p>
 * ClassName:TreePropertySource <br>
 * Date: Jan 15, 2019 3:08:44 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class TreePropertySource extends EnumerablePropertySource<TreeNode> {
    private final Map<String, TreeNode> configs;
    private String[] propertyNames;

    public TreePropertySource(String name, TreeNode source) {
        super(name, source);
        configs = JacksonUtils.flatValues(source);
    }

    public TreePropertySource(String name, String prefix, TreeNode source) {
        super(name, source);
        String[] pres = prefix.split("\\.");
        ObjectNode root = JacksonUtils.JSON.createObjectNode();
        ObjectNode current = root;
        for (int i = 0; i < pres.length - 1; i++) {
            String pre = pres[i];
            ObjectNode node = JacksonUtils.JSON.createObjectNode();
            current.set(pre, node);
            current = node;
        }

        current.set(pres[pres.length - 1], (JsonNode) source);
        configs = JacksonUtils.flatValues(prefix, root);
    }

    @Override
    public String[] getPropertyNames() {
        if (propertyNames == null) {
            propertyNames = configs.keySet()
                    .toArray(new String[configs.keySet()
                            .size()]);
        }
        return propertyNames;
    }

    @Override
    public Object getProperty(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        TreeNode node = configs.get(name);

        return node == null ? null : getValue(node);
    }

    private Object getValue(TreeNode node) {
        if (node == null) {
            return null;
        }
        if (node.isValueNode()) {
            return JacksonUtils.getValue((ValueNode) node);
        } else if (node.isObject()) {
            return JacksonUtils.JSON.treeToValue(node, Object.class);
            // return node.toString();
        } else if (node.isArray()) {
            return JacksonUtils.JSON.convert(node, new TypeReference<List<Object>>() {
            });
            // return node.toString();
        }

        return null;
    }

}
