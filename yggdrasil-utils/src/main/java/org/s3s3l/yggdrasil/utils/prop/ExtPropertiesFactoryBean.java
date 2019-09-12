package org.s3s3l.yggdrasil.utils.prop;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;


/**
 * ClassName:ExtPropertiesFactoryBean <br>
 * Date: 2016年3月16日 上午11:35:54 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ExtPropertiesFactoryBean extends PropertiesFactoryBean {

	private String prefixKey;

	private Resource[] fileLocations;

	private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

	private String fileEncoding;

	private static final String XML_FILE_EXTENSION = ".xml";

	private boolean ignoreResourceNotFound = false;

	public String getPrefixKey() {
		return prefixKey;
	}

	public void setPrefixKey(String prefixKey) {
		this.prefixKey = prefixKey;
	}

	public Resource[] getFileLocations() {
		return fileLocations;
	}

	public void setFileLocations(Resource[] fileLocations) {
		this.setLocations(fileLocations);
		this.fileLocations = fileLocations;
	}

	public PropertiesPersister getPropertiesPersister() {
		return propertiesPersister;
	}

	@Override
	public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
		this.propertiesPersister = propertiesPersister;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

    @Override
	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	public boolean isIgnoreResourceNotFound() {
		return ignoreResourceNotFound;
	}

    @Override
	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}

	public static String getXmlFileExtension() {
		return XML_FILE_EXTENSION;
	}

	@Override
	protected void loadProperties(Properties props) throws IOException {
		if (this.fileLocations != null) {
			for (Resource location : this.fileLocations) {
				if (logger.isInfoEnabled()) {
					logger.info("Loading properties file from " + location);
				}
				try {
					fillProperties(props, new EncodedResource(location, this.fileEncoding), this.propertiesPersister);
				} catch (IOException ex) {
					if (this.ignoreResourceNotFound) {
						if (logger.isWarnEnabled()) {
							logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
						}
					} else {
						throw ex;
					}
				}
			}
		}
	}

	private void fillProperties(Properties props, EncodedResource resource, PropertiesPersister persister)
			throws IOException {

		InputStream stream = null;
		Reader reader = null;
		try {
			String filename = resource.getResource().getFilename();
			if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
				stream = resource.getInputStream();
				persister.loadFromXml(props, stream);
			} else if (resource.requiresReader()) {
				reader = resource.getReader();
				persister.load(props, reader);
			} else {
				stream = resource.getInputStream();
				persister.load(props, stream);
			}

			final String prefix = props.getProperty(prefixKey);

			if (!StringUtils.isEmpty(prefix)) {

				List<String> keywords = props.keySet().stream().filter(r -> r.toString().startsWith(prefix))
						.map(Object::toString).collect(Collectors.toList());

				keywords.forEach(r -> {
					String prop = props.getProperty(r);
					props.remove(r);
					props.setProperty(r.replaceFirst(String.format("%s\\.", prefix), StringUtils.EMPTY_STRING), prop);
				});

			}
		} finally {
			if (stream != null) {
				stream.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}

}
