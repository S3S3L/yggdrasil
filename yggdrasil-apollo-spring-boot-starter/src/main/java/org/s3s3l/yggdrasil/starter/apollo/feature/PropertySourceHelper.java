
package org.s3s3l.yggdrasil.starter.apollo.feature;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.s3s3l.yggdrasil.starter.apollo.ApolloConfiguration.Document;
import org.s3s3l.yggdrasil.starter.apollo.ApolloConfiguration.FieldDoc;
import org.s3s3l.yggdrasil.bean.exception.ResourceProcessException;
import org.s3s3l.yggdrasil.starter.apollo.DocumentProcessException;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonHelper;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.yaml.snakeyaml.Yaml;

import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigService;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * <p>
 * </p>
 * ClassName:PropertySourceHelper <br>
 * Date: Jun 5, 2019 10:12:04 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class PropertySourceHelper {
    private static final Yaml snakeYaml = new Yaml();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JacksonHelper json = JacksonUtils.create();
    private final JacksonHelper yml = JacksonUtils.create(new YAMLFactory());
    private final JacksonHelper xml = JacksonUtils.create(new XmlFactory());
    private final JacksonHelper prop = JacksonUtils.create(new JavaPropsFactory());

    private final ConfigurableEnvironment environment;
    private final Map<Class<? extends ConfigFileChangedProcessor>, ConfigFileChangedProcessor> processors = new ConcurrentHashMap<>();

    public PropertySourceHelper(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    /**
     * 
     * 处理文档
     * 
     * @param doc
     * @since JDK 1.8
     */
    public void processDocument(Document doc) {
        logger.info("Starting process document '{}'", doc.getName());
        switch (doc.getFormat()) {
            case YAML:
            case YML:
                processYamlDocument(doc);
                break;
            case JSON:
                processJsonDocument(doc);
                break;
            case XML:
                processXmlDocument(doc);
                break;
            case Properties:
                processPropDocument(doc);
                break;
            case TXT:
            default:
                throw new ResourceProcessException("doc for format '" +doc.getFormat() + "' is not support");
        }
        logger.info("Finished process document '{}'", doc.getName());
    }

    /**
     * 
     * 处理YAML格式文档
     * 
     * @param doc
     * @since JDK 1.8
     */
    private void processYamlDocument(Document doc) {
        processDocument(doc, content -> yml.valueToTree(snakeYaml.load(content)));
    }

    /**
     * 
     * 处理JSON格式文档
     * 
     * @param doc
     * @since JDK 1.8
     */
    private void processJsonDocument(Document doc) {
        processDocument(doc, json::toTreeNode);
    }

    /**
     * 
     * 处理XML格式文档
     * 
     * @param doc
     * @since JDK 1.8
     */
    private void processXmlDocument(Document doc) {
        processDocument(doc, xml::toTreeNode);
    }

    /**
     * 
     * 处理Properties格式文档
     * 
     * @param doc
     * @since JDK 1.8
     */
    private void processPropDocument(Document doc) {
        ConfigFile configFile = ConfigService.getConfigFile(doc.getName(), doc.getFormat());
        configFile.addChangeListener(event -> triggerProcessors(
                (c, e, n) -> processPropDocument(doc, event.getNewValue()), doc, event.getNewValue()));

        String configContent = configFile.getContent();
        processPropDocument(doc, configContent);
    }

    /**
     * 
     * 处理Properties格式文档
     * 
     * @param doc
     *            文档描述
     * @param configContent
     *            文档内容
     * @since JDK 1.8
     */
    private void processPropDocument(Document doc, String configContent) {
        JsonNode node = prop.toTreeNode(configContent);
        if (CollectionUtils.isEmpty(doc.getFieldDoc())) {
            processDocument(doc, prop::toTreeNode);
            return;
        }

        for (FieldDoc fd : doc.getFieldDoc()) {
            String key = fd.getKey();
            String content = getContent(node, key);
            String defaultContent = getContent(node, fd.getDefaultKey());
            if (StringUtils.isEmpty(key) || fd.getFormat() == null || StringUtils.isEmpty(content)) {
                logger.warn("Field doc '{}.{}' processing fail.", doc.getName(), key);
                continue;
            }

            TreeNode tree = null;
            switch (fd.getFormat()) {
                case JSON:
                    tree = getContentTree(defaultContent, content, json, Processers.DEFAULT);
                    break;
                case XML:
                    tree = getContentTree(defaultContent, content, xml, Processers.DEFAULT);
                    break;
                case YAML:
                    tree = getContentTree(defaultContent, content, yml, Processers.YAML);
                    break;
                default:
                    logger.warn("Field doc '{}.{}' format '{}' is not support.", doc.getName(), key, fd.getFormat());
                    break;
            }

            if (tree != null) {
                processFieldDoc(doc.getName(), key, tree);
            }
        }
    }

    private TreeNode
            getContentTree(String defaultContent, String content, JacksonHelper helper, TreeNodeProcesser processer) {
        if (StringUtils.isEmpty(defaultContent)) {
            return processer.process(content, helper);
        }
        return helper.update(processer.process(defaultContent, helper), processer.process(content, helper));
    }

    private void processFieldDoc(String docName, String key, TreeNode node) {
        String[] pres = key.split("\\.");
        ObjectNode root = JacksonUtils.defaultHelper.createObjectNode();
        ObjectNode current = root;
        for (int i = 0; i < pres.length - 1; i++) {
            String pre = pres[i];
            ObjectNode newNode = JacksonUtils.defaultHelper.createObjectNode();
            current.set(pre, newNode);
            current = newNode;
        }

        current.set(pres[pres.length - 1], (JsonNode) node);
        buildPropertySource(String.join(".", docName, key), yml.toStructuralBytes(root)).forEach(ps -> {
            MutablePropertySources propertySources = environment.getPropertySources();
            if (propertySources.contains(ps.getName())) {
                propertySources.replace(ps.getName(), ps);
            } else {
                propertySources.addLast(ps);
            }
        });
    }

    private void processDocument(Document doc, Function<String, TreeNode> func) {
        ConfigFile configFile = ConfigService.getConfigFile(doc.getName(), doc.getFormat());
        configFile.addChangeListener(event -> triggerProcessors((c, e, n) -> {
            List<PropertySource<?>> psList = buildPropertySource(doc.getName(),
                    yml.toStructuralBytes(func.apply(event.getNewValue())));
            psList.forEach(ps -> environment.getPropertySources()
                    .replace(doc.getName(), ps));
        }, doc, event.getNewValue()));

        String content = configFile.getContent();
        processDocument(doc, func, content);
    }

    private void processDocument(Document doc, Function<String, TreeNode> func, String content) {
        List<PropertySource<?>> psList = buildPropertySource(doc.getName(), yml.toStructuralBytes(func.apply(content)));
        switch (doc.getLocation()
                .getType()) {
            case AFTER:
                psList.forEach(ps -> environment.getPropertySources()
                        .addAfter(doc.getLocation()
                                .getTarget(), ps));
                break;
            case BEFORE:
                psList.forEach(ps -> environment.getPropertySources()
                        .addBefore(doc.getLocation()
                                .getTarget(), ps));
                break;
            case FIRST:
                psList.forEach(environment.getPropertySources()::addFirst);
                break;
            case LAST:
                psList.forEach(environment.getPropertySources()::addLast);
                break;
        }
    }

    private List<PropertySource<?>> buildPropertySource(String name, byte[] content) {
        try {
            return new YamlPropertySourceLoader().load(name,
                    new InputStreamResource(new ByteArrayInputStream(content)));
        } catch (IOException e) {
            throw new DocumentProcessException(e);
        }
    }

    private String getContent(final JsonNode node, String key) {
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        JsonNode currentNode = node;
        String[] fields = key.split("\\.");
        for (String field : fields) {
            if (!currentNode.has(field)) {
                return null;
            }
            currentNode = currentNode.get(field);
        }

        if (!currentNode.isValueNode()) {
            currentNode = currentNode.get("");
        }
        return currentNode.asText();
    }

    private void triggerProcessors(ConfigFileChangedProcessor firstProcessor, Document doc, String newValue) {
        if (firstProcessor != null) {
            firstProcessor.process(ProcessorContext.context, environment, newValue);
        }

        if (doc.getProcessors() == null) {
            return;
        }

        for (Class<? extends ConfigFileChangedProcessor> processorClazz : doc.getProcessors()) {
            ConfigFileChangedProcessor processor = this.processors.get(processorClazz);
            if (processor == null) {
                try {
                    processor = processorClazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new DocumentProcessException(e);
                }
                this.processors.put(processorClazz, processor);
            }

            processor.process(ProcessorContext.context, environment, newValue);
        }
    }

    @FunctionalInterface
    private interface TreeNodeProcesser {

        JsonNode process(String content, JacksonHelper helper);
    }

    private abstract static class Processers {
        public static final TreeNodeProcesser DEFAULT = (content, helper) -> helper.toTreeNode(content);
        public static final TreeNodeProcesser YAML = (content, helper) -> helper.valueToTree(snakeYaml.load(content));
    }
}
