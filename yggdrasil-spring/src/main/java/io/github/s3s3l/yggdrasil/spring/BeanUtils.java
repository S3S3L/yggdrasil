
package io.github.s3s3l.yggdrasil.spring;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * <p>
 * </p>
 * ClassName:BeanUtils <br>
 * Date: Sep 20, 2018 11:52:13 AM <br>
 *
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class BeanUtils {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.exit(0);
        String orgHeader = "sec-ch-ua-model=%22%22,sec-ch-ua-platform-version=%2211.7.0%22,sec-ch-ua-full-version=%22109.0.5414.119%22,clientId=urn%3Aebay-marketplace-consumerid%3A706fa774-6d7b-4dab-9b42-44fa0196a550,ip=10.226.208.174,userAgentAccept=text%2Fhtml%2Capplication%2Fxhtml%2Bxml%2Capplication%2Fxml%3Bq%3D0.9%2Cimage%2Favif%2Cimage%2Fwebp%2Cimage%2Fapng%2C*%2F*%3Bq%3D0.8%2Capplication%2Fsigned-exchange%3Bv%3Db3%3Bq%3D0.9,userAgentAcceptEncoding=gzip%2C%20deflate%2C%20br,userAgent=Mozilla%2F5.0%20(Macintosh%3B%20Intel%20Mac%20OS%20X%2010_15_7)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F109.0.0.0%20Safari%2F537.36,uri=%2F,applicationURL=https%3A%2F%2Fwww.qa.ebay.com%2F%3F_showdiag%3D1,isPiggybacked=false,expectSecureURL=true,physicalLocation=country%3DUS%2Czip%3D95125,contextualLocation=country%3DUS,origClientId=urn%3Aebay-marketplace-consumerid%3A706fa774-6d7b-4dab-9b42-44fa0196a550,guessedUserId=2751589316,deviceId=8546342004,deviceIdType=ID,origUserId=origUserName%3Debaybuyer2023_1%2CorigAcctId%3D2751589316%2Crecognized%3Dfalse";
        String header = "sec-ch-ua-model=%22%22,sec-ch-ua-platform-version=%2211.7.0%22,sec-ch-ua-full-version=%22109.0.5414.119%22,clientId=urn%3Aebay-marketplace-consumerid%3A706fa774-6d7b-4dab-9b42-44fa0196a550,ip=10.226.208.174,userAgentAccept=text%2Fhtml%2Capplication%2Fxhtml%2Bxml%2Capplication%2Fxml%3Bq%3D0.9%2Cimage%2Favif%2Cimage%2Fwebp%2Cimage%2Fapng%2C*%2F*%3Bq%3D0.8%2Capplication%2Fsigned-exchange%3Bv%3Db3%3Bq%3D0.9,userAgentAcceptEncoding=gzip%2C%20deflate%2C%20br,userAgent=Mozilla%2F5.0%20(Macintosh%3B%20Intel%20Mac%20OS%20X%2010_15_7)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F109.0.0.0%20Safari%2F537.36,uri=%2F,applicationURL=https%3A%2F%2Fwww.qa.ebay.com%2F%3F_showdiag%3D1,isPiggybacked=false,expectSecureURL=true,physicalLocation=country%3DUS%2Czip%3D95125,contextualLocation=country%3DUS,origClientId=urn%3Aebay-marketplace-consumerid%3A706fa774-6d7b-4dab-9b42-44fa0196a550,guessedUserId=2751589316,deviceId=8546342004,deviceIdType=ID,origUserId=origUserName%3D%2CorigAcctId%3D2751589316%2Crecognized%3Dfalse";
        String origUserName = "ebaybuyer2023_1";
        Map<String, String> map = toMap(header);

        System.out.println("=======================");

        String origUserId = map.get("origUserId");

        origUserId = URLDecoder.decode(origUserId, StandardCharsets.UTF_8);

        Map<String, String> origUserIdMap = toMap(origUserId);

        origUserIdMap.put("origUserName", origUserName);
        String origUserIdStr = toString(origUserIdMap);
        System.out.println(origUserIdStr);

        System.out.println("=======================");

        origUserIdStr = URLEncoder.encode(origUserIdStr, StandardCharsets.UTF_8);

        map.put("origUserId", origUserIdStr);
        String newHeader = toString(map);
        System.out.println(newHeader);

        System.out.println(orgHeader.equals(newHeader));
    }

    private static String toString(Map<String, String> map) {
        return map.entrySet().stream()
                .map(r -> {
                    String value = r.getValue();
                    System.out.println(r.getKey() + " : " + value);
                    return String.join("=", r.getKey(),
                            value);
                })
                .collect(Collectors.joining(","));
    }

    private static Map<String, String> toMap(String header) throws UnsupportedEncodingException {
        Map<String, String> map = new LinkedHashMap<>();
        String[] headers = header.split(",");
        for (String h : headers) {
            String[] kv = h.split("=");
            if (kv.length == 1) {
                System.out.println(kv[0] + " : null");
                map.put(kv[0], null);
                continue;
            }
            String value = kv[1];
            System.out.println(kv[0] + " : " + value);
            map.put(kv[0], value);
        }

        return map;
    }

    /**
     * 构建BeanDefinition
     * 
     * @param beanType
     *                 类型
     * @return
     */
    public static BeanDefinition buildBeanDefinition(Class<?> beanType) {
        return buildBeanDefinition(null, null, null, null, beanType);
    }

    /**
     * 构建BeanDefinition
     * 
     * @param props
     *                 属性值(fieldName -> object)
     * @param beanType
     *                 类型
     * @return
     */
    public static BeanDefinition buildBeanDefinition(Map<String, Object> props, Class<?> beanType) {
        return buildBeanDefinition(props, null, null, null, beanType);
    }

    /**
     * 构建BeanDefinition
     * 
     * @param props
     *                 属性值(fieldName -> object)
     * @param refs
     *                 属性值(fieldName -> beanName)
     * @param beanType
     *                 类型
     * @return
     */
    public static BeanDefinition buildBeanDefinition(Map<String, Object> props, Map<String, String> refs,
            Class<?> beanType) {
        return buildBeanDefinition(props, refs, null, null, beanType);
    }

    /**
     * 构建BeanDefinition
     * 
     * @param props
     *                         属性值(fieldName -> object)
     * @param refs
     *                         属性值(fieldName -> beanName)
     * @param constructArgsRef
     *                         构造方法参数(beanName)
     * @param beanType
     *                         类型
     * @return
     */
    public static BeanDefinition buildBeanDefinition(Map<String, Object> props,
            Map<String, String> refs,
            String[] constructArgsRef,
            Class<?> beanType) {
        return buildBeanDefinition(props, refs, constructArgsRef, null, beanType);
    }

    /**
     * 构建BeanDefinition
     * 
     * @param constructArgs
     *                      构造方法参数(object)
     * @param beanType
     *                      类型
     * @return
     */
    public static BeanDefinition buildBeanDefinition(Object[] constructArgs, Class<?> beanType) {
        return buildBeanDefinition(null, null, null, constructArgs, beanType);
    }

    /**
     * 构建BeanDefinition
     * 
     * @param props
     *                         属性值(fieldName -> object)
     * @param refs
     *                         属性值(fieldName -> beanName)
     * @param constructArgsRef
     *                         构造方法参数(beanName)
     * @param constructArgs
     *                         构造方法参数(object)
     * @param beanType
     *                         类型
     * @return
     */
    public static BeanDefinition buildBeanDefinition(Map<String, Object> props,
            Map<String, String> refs,
            String[] constructArgsRef,
            Object[] constructArgs,
            Class<?> beanType) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanType);
        if (props != null) {
            props.entrySet()
                    .forEach(entry -> builder.addPropertyValue(entry.getKey(), entry.getValue()));
        }
        if (refs != null) {
            refs.entrySet()
                    .forEach(entry -> builder.addPropertyReference(entry.getKey(), entry.getValue()));
        }
        if (constructArgsRef != null) {
            Arrays.stream(constructArgsRef)
                    .forEach(builder::addConstructorArgReference);
        }
        if (constructArgs != null) {
            Arrays.stream(constructArgs)
                    .forEach(builder::addConstructorArgValue);
        }

        builder.setLazyInit(true);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        return builder.getBeanDefinition();
    }

    /**
     * 构建BeanDefinition
     * 
     * @param beanType
     *                        类型
     * @param factoryBeanName
     *                        工厂beanName
     * @param factoryMethod
     *                        工厂方法名
     * @param args
     *                        工厂方法入参(object)
     * @param refs
     *                        工厂方法入参(beanName)
     * @return
     */
    public static BeanDefinition buildBeanDefinitionForFactoryMethod(Class<?> beanType,
            String factoryBeanName,
            String factoryMethod,
            Object[] args,
            String... refs) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanType);
        builder.setFactoryMethodOnBean(factoryMethod, factoryBeanName);
        if (args != null) {
            for (Object arg : args) {
                builder.addConstructorArgValue(arg);
            }
        }
        for (String ref : refs) {
            builder.addConstructorArgReference(ref);
        }
        builder.setLazyInit(true);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        return builder.getBeanDefinition();
    }
}
