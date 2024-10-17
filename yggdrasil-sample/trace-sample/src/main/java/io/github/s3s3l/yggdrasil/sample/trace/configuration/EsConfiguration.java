package io.github.s3s3l.yggdrasil.sample.trace.configuration;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
public class EsConfiguration {

    @Bean
    RestClient restClient(EsClientConfig config) {
        return RestClient
                .builder(HttpHost.create(config.getEndpoint()))
                .setDefaultHeaders(new Header[] {
                        new BasicHeader("Authorization", "ApiKey " + config.getApiKey())
                })
                .build();
    }

    @Bean
    ElasticsearchClient esClient(RestClient restClient) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return new ElasticsearchClient(new RestClientTransport(restClient, new JacksonJsonpMapper(objectMapper)));
    }
}
