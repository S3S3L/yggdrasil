package io.github.s3s3l.yggdrasil.sample.trace.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.s3s3l.yggdrasil.otel.pool.OtelDelagateExecutorService;
import io.github.s3s3l.yggdrasil.promise.Promise;
import io.github.s3s3l.yggdrasil.sample.trace.utils.Dice;
import io.opentelemetry.api.OpenTelemetry;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
@RestController
@RequestMapping("test")
public class RollController {
    private static final Logger logger = LoggerFactory.getLogger(RollController.class);

    @Autowired
    OpenTelemetry openTelemetry;

    @Autowired
    OtelDelagateExecutorService executorService;

    @GetMapping("/rolldice")
    public List<Integer> index(@RequestParam("player") Optional<String> player,
            @RequestParam("rolls") Optional<Integer> rolls) {

        if (!rolls.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing rolls parameter", null);
        }

        List<Integer> result = new Dice(1, 6, executorService).rollTheDice(rolls.get());

        if (player.isPresent()) {
            logger.info("{} is rolling the dice: {}", player.get(), result);
        } else {
            logger.info("Anonymous player is rolling the dice: {}", result);
        }
        return result;
    }

    @GetMapping("/echo")
    public @ResponseBody String echo(@RequestParam("msg") String msg) {
        return msg;
    }

    @GetMapping("/error")
    public @ResponseBody String error() {

        log.error("error msg.");
        return "error";
    }

    @GetMapping("/warn")
    public @ResponseBody String warn() {

        log.error("warn msg.");
        return "warn";
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        List<Promise<Object>> promises = new LinkedList<>();
        while (true) {
            for (int i = 0; i <= 20; i++) {
                promises.add(Promise.async(() -> {
                    try {
                        doRequest("http://localhost:8080");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }, executorService));
                promises.add(Promise.async(() -> {
                    try {
                        doRequest("http://localhost:10080");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }, executorService));
            }
            Thread.sleep(1000);
        }

        // for (Promise<Object> promise : promises) {
        // promise.get();
        // }
    }

    private static void doRequest(String endPoint) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(1, TimeUnit.MINUTES)
                .build();
        Request request = new Request.Builder()
                .url(endPoint + "/social/follow/v1/relation/USER/5bhaETINQfm")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",
                        "Bearer v^1.1#i^1#p^1#r^0#f^0#I^3#t^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")
                .addHeader("X-EBAY-C-ENDUSERCTX",
                        "ip=184.51.206.110,userAgentAccept=text%2Fhtml%2Capplication%2Fxhtml%2Bxml%2Capplication%2Fxml%3Bq%3D0.9%2Cimage%2Fwebp%2Cimage%2Fapng%2C*%2F*%3Bq%3D0.8%2Capplication%2Fsigned-exchange%3Bv%3Db3,userAgentAcceptEncoding=gzip,userAgentAcceptCharset=null,userAgent=Mozilla%2F5.0+%28Macintosh%3B+Intel+Mac+OS+X+10_14_5%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F75.0.3770.142+Safari%2F537.36,referer=null,uri=%2Ffeed,physicalLocation=country%3DUS,contextualLocation=country%3DUS,origUserId=origUserName%3Daaron_test_user1%2CorigAcctId%3D1383390478,isPiggybacked=false,fullSiteExperience=true,expectSecureURL=true")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(String.format("Do Request to %s. Result: %s", endPoint, response.body()
                .string()));
    }

}
