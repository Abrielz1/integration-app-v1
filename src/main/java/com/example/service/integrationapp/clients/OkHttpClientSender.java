package com.example.service.integrationapp.clients;

import com.example.service.integrationapp.model.EntityModelResponseDto;
import com.example.service.integrationapp.model.UpsertEntityRequestNewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import okhttp3.OkHttpClient;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OkHttpClientSender {

    private final OkHttpClient httpClient;

    private final ObjectMapper objectMapper;

    @Value("${app.integration.base-url}")
    private String baseurl;

    public String uploadFile(MultipartFile file) {

        MultipartBody.Builder builder;

        try {

            builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"), file.getBytes()));
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        Request request = new Request.Builder()
                .url(baseurl + "/api/v1/file/upload")
                .header("Content-Type", "multipart/form-data")
                .post(builder.build())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) {

                log.error("Error while uploading file!");

                return "Error!";
            }

            return new String(response.body().bytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource downloadFile(String filename) {

        Request request = new Request.Builder()
                .url(baseurl + "/api/v1/file/download" + filename)
                .header("Accept", "application/octet-stream")
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) {

                log.error("Error while uploading file!");

                return null;
            }

            return new ByteArrayResource(response.body().bytes());

        } catch (IOException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<EntityModelResponseDto> getEntityList() {

        Request request = new Request.Builder()
                .url(baseurl + "/api/v1/file/entity")
                .build();

        return processResponses(request, new TypeReference<>(){});
    }

    public EntityModelResponseDto getEntityByName(String name) {

        Request request = new Request.Builder()
                .url(baseurl + "/api/v1/entity" + name)
                .build();

        return processResponses(request, new TypeReference<>(){});
    }

    public EntityModelResponseDto createEntity(UpsertEntityRequestNewDto newDto) {

        MediaType JSON = MediaType.get("application/json;charset=utf-8");

        try {
            String requestBody = objectMapper.writeValueAsString(newDto);

            RequestBody body = RequestBody.create(requestBody, JSON);

            Request request = new Request.Builder()
                    .url(baseurl + "/api/v1/entity")
                    .post(body)
                    .build();

        return processResponses(request, new TypeReference<>(){});

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public EntityModelResponseDto updateEntity(UUID id, UpsertEntityRequestNewDto newDto) {

        MediaType JSON = MediaType.get("application/json;charset=utf-8");

        try {
            String requestBody = objectMapper.writeValueAsString(newDto);

            RequestBody body = RequestBody.create(requestBody, JSON);

            Request request = new Request.Builder()
                    .url(baseurl + "/api/v1/entity/" + id)
                    .put(body)
                    .build();

            return processResponses(request, new TypeReference<>(){});

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteEntityById(UUID id) {
        Request request = new Request.Builder()
                .url(baseurl + "/api/v1/entity/" + id)
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute())  {

            if (!response.isSuccessful()) {

                log.error("Unexpected Response code!" + response);
            }

        } catch (IOException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private <T> T processResponses(Request request, TypeReference<T> typeReference) {

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                log.error("Unexpected Response code!" + response);

                return null;
            }

            ResponseBody responseBody = response.body();

            if (responseBody == null) {
                throw new RuntimeException("ResponseBody is empty");
            }

            String stringBody = responseBody.string();

            return objectMapper.readValue(stringBody, typeReference);

        } catch (IOException e) {

                e.printStackTrace();
                throw new RuntimeException(e);
        }
    }
}