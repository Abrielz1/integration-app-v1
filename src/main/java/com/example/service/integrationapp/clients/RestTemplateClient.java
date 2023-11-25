package com.example.service.integrationapp.clients;

import com.example.service.integrationapp.model.EntityModelResponseDto;
import com.example.service.integrationapp.model.UpsertEntityRequestNewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestTemplateClient {

    private final RestTemplate restTemplate;

    @Value("${app.integration.base-url}")
    private String baseurl;

    public void uploadFile(MultipartFile file) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.patchForObject(baseurl + "/api/v1/file/upload", requestEntity, String.class);
    }

    public Resource downloadFile(String filename) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

        ResponseEntity<Resource> response = restTemplate.exchange(
                baseurl + "/api/v1/file/download/{filename}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Resource.class,
                filename
        );

        return response.getBody();
    }

    public List<EntityModelResponseDto> getEntityList() {

        ResponseEntity<List<EntityModelResponseDto>> response = restTemplate.exchange(
                baseurl + "/api/v1/file/entity",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }

    public EntityModelResponseDto getEntityByName(String name) {

        ResponseEntity<EntityModelResponseDto> response = restTemplate.exchange(
                baseurl + "/api/v1/file/entity/{name}",
                HttpMethod.GET,
                null,
                EntityModelResponseDto.class,
                name
        );

        return response.getBody();
    }

    public EntityModelResponseDto createEntity(UpsertEntityRequestNewDto request) {

        HttpEntity<UpsertEntityRequestNewDto> responseEntity = new HttpEntity<>(request);

        ResponseEntity<EntityModelResponseDto> response = restTemplate.exchange(
                baseurl + "/api/v1/file/entity",
                HttpMethod.POST,
                responseEntity,
                EntityModelResponseDto.class
        );

        return response.getBody();
    }

    public EntityModelResponseDto updateEntity(UUID id, UpsertEntityRequestNewDto request) {

        HttpEntity<UpsertEntityRequestNewDto> requestEntity = new HttpEntity<>(request);

        ResponseEntity<EntityModelResponseDto> response = restTemplate.exchange(
                baseurl + "/api/v1/file/entity/{id}",
                HttpMethod.PUT,
                requestEntity,
                EntityModelResponseDto.class,
                id
        );

        return response.getBody();
    }

    public void deleteEntityById(UUID id) {

        restTemplate.exchange(
                baseurl + "/api/v1/file/entity/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                id
        );
    }
}
