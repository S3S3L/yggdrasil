package io.github.s3s3l.yggdrasil.sample.trace;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Base64;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import io.github.s3s3l.yggdrasil.otel.data.es.trace.LogData;
import io.github.s3s3l.yggdrasil.sample.trace.es.query.LogQuery;
import io.github.s3s3l.yggdrasil.template.TemplateManager;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

@SpringBootApplication(scanBasePackages = { "io.github.s3s3l.yggdrasil.sample.trace" })
public class Application {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        // testEs(ctx.getBean(TemplateManager.class));
    }

    static void testEs(TemplateManager templateManager) throws IOException {
        // URL and API key
        String serverUrl = "https://localhost:9200";
        String apiKey = "NHVwQmRKSUJQTktEV0NpLWNKY0Q6MlVNU2hiQ0ZSdEMxTW0wYW9VNS1Fdw==";

        // Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
                .setDefaultHeaders(new Header[] {
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper(objectMapper));

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        String query = templateManager.compile(LogQuery.builder()
                .start(ZonedDateTime.parse("2024-10-10T06:53:27.441Z"))
                .end(ZonedDateTime.parse("2024-10-10T19:13:40.125Z"))
                .body("request log")
                .build(),
                LogQuery.class);

        String base64Query = Base64.getEncoder().encodeToString(query.getBytes());
        // Use the client...
        esClient.search(builder -> builder.index("otel*")
                .query(QueryBuilders.wrapper(w -> w.query(
                        base64Query))),
                LogData.class).hits().hits().forEach(hit -> {
                    System.out.println(JacksonUtils.JSON.toStructuralString(hit.source()));
                });

        // Rest Request
        // Request request = new Request("POST", "/otel_log_index/_async_search");
        // request.setJsonEntity(templateManager.compile(LogQuery.builder()
        // .start(ZonedDateTime.parse("2024-10-10T06:53:27.441Z"))
        // .end(ZonedDateTime.parse("2024-10-10T19:13:40.125Z"))
        // .body("request log")
        // .build(),
        // LogQuery.class));
        // Response res = restClient.performRequest(request);
        // ByteArrayOutputStream out = new ByteArrayOutputStream();
        // res.getEntity().writeTo(out);
        // var response = JacksonUtils.JSON.toObject(out.toByteArray(),
        // new TypeReference<RestResponse<LogData>>() {

        // });

        // System.out.println(JacksonUtils.JSON.toStructuralString(response.getResponse().getHits()));

        // Close the transport, freeing the underlying thread
        // transport.close();
    }
}
