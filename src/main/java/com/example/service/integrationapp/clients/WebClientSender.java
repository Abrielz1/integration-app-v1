package com.example.service.integrationapp.clients;

import com.example.service.integrationapp.model.EntityModelResponseDto;
import com.example.service.integrationapp.model.UpsertEntityRequestNewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebClientSender {

    private final WebClient webClient;

    public void uploadFile(MultipartFile file) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource())
               .filename(file.getName())
               .contentType(MediaType.APPLICATION_OCTET_STREAM);

        webClient.post()
                 .uri("/api/v1/file/upload")
                 .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                 .body(BodyInserters.fromMultipartData(builder.build()))
                 .retrieve()
                 .bodyToMono(String.class)
                 .block();
    }

    public Resource downloadFile(String filename) {

        return webClient.get()
                        .uri("/api/v1/file/download/{filename}", filename)
                        .accept(MediaType.APPLICATION_OCTET_STREAM)
                        .retrieve()
                        .bodyToMono(Resource.class)
                        .block();
    }

    public List<EntityModelResponseDto> getEntityList() {

        return webClient.get()
                        .uri("/api/v1/entity")
                        .retrieve()
                        .bodyToFlux(EntityModelResponseDto.class)
                        .collectList().block();
    }

    public EntityModelResponseDto getEntityByName(String name) {

        return webClient.get()
                        .uri("/api/v1/entity/{name}", name)
                        .retrieve()
                        .bodyToMono(EntityModelResponseDto.class)
                        .block();
    }

    public EntityModelResponseDto createEntity(UpsertEntityRequestNewDto request) {

        return webClient.post()
                        .uri("/api/v1/entity")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(EntityModelResponseDto.class)
                        .block();
    }

    public EntityModelResponseDto updateEntity(UUID id, UpsertEntityRequestNewDto request) {

        return webClient.put()
                        .uri("/api/v1/entity/{id}", id)
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(EntityModelResponseDto.class)
                        .block();
    }

    public void deleteEntityById(UUID id) {

        webClient.delete()
                 .uri("/api/v1/entity/{id}", id)
                 .retrieve()
                 .bodyToMono(Void.class)
                 .block();
    }
}
