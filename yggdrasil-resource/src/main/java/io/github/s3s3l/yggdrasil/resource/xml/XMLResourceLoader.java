package io.github.s3s3l.yggdrasil.resource.xml;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.github.s3s3l.yggdrasil.resource.DefaultResourceLoader;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;

/**
 * 
 * <p>
 * </p>
 * ClassName: XMLResourceLoader <br>
 * date: Sep 20, 2019 11:24:47 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class XMLResourceLoader extends DefaultResourceLoader {
    public static final List<String> XML_FILE_EXTENSIONS = Arrays.asList("xml", "XML");

    private XMLResourceLoader() {
        this.parser = JacksonUtils.create(new XmlFactory());
    }

    private XMLResourceLoader(String profileKey) {
        this.parser = JacksonUtils.create(new XmlFactory());
        this.profileKey = profileKey;
    }

    public static XMLResourceLoader create() {
        return new XMLResourceLoader();
    }

    public static XMLResourceLoader create(String profileKey) {
        return new XMLResourceLoader(profileKey);
    }

    public static XMLResourceLoader create(Map<ConfigFeature, Boolean> features) {
        XMLResourceLoader xmlUtil = new XMLResourceLoader();
        xmlUtil.configure(features);
        return xmlUtil;
    }

    public static XMLResourceLoader create(String profileKey, Map<ConfigFeature, Boolean> features) {
        XMLResourceLoader xmlUtil = new XMLResourceLoader(profileKey);
        xmlUtil.configure(features);
        return xmlUtil;
    }

}
