package io.github.hefrankeleyn.utils.http.impl;

import static com.google.common.base.Preconditions.*;
import io.github.hefrankeleyn.utils.http.HttpInvoker;
import okhttp3.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Date 2024/5/30
 * @Author lifei
 */
public class OkHttpInvoker implements HttpInvoker {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client;

    public OkHttpInvoker(Long timeoutMS) {
        int coreNum = Runtime.getRuntime().availableProcessors();
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(coreNum, 5000, TimeUnit.SECONDS))
                .readTimeout(timeoutMS, TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMS, TimeUnit.MILLISECONDS)
                .connectTimeout(timeoutMS, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public String get(String url) {
        Request request = new Request.Builder().url(url).get().build();
        try (Response response = client.newCall(request).execute()){
            checkState(response.isSuccessful(), "Unexpected code " + response);
            ResponseBody body = response.body();
            if (Objects.nonNull(body)) {
                return body.string();
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public String post(String url, String requestBody) {
        Request request = new Request.Builder().url(url).post(RequestBody.create(requestBody, MEDIA_TYPE_JSON)).build();
        try (Response response = client.newCall(request).execute()){
            checkState(response.isSuccessful(), "Unexpected code " + response);
            ResponseBody body = response.body();
            if (Objects.nonNull(body)) {
                return body.string();
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
