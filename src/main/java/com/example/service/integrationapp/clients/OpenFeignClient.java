package com.example.service.integrationapp.clients;

import com.example.service.integrationapp.model.EntityModelResponseDto;
import com.example.service.integrationapp.model.UpsertEntityRequestNewDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "openFeignClient", url = "${app.integration.base-url}")
public interface OpenFeignClient {

    @PostMapping(value = "/api/v1/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadFile(@RequestPart("file")MultipartFile file);

    @GetMapping(value = "/api/v1/file/download/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    Resource downloadFile(@PathVariable(name = "filename") String filename);

    @GetMapping(value = "/api/v1/entity")
    List<EntityModelResponseDto> getEntityList();

    @GetMapping(value = "/api/v1/entity/{name}")
    EntityModelResponseDto getEntityByName(@PathVariable(name = "name") String name);

    @PostMapping(value = "/api/v1/entity")
    EntityModelResponseDto createEntity(UpsertEntityRequestNewDto request);

    @PutMapping(value = "/api/v1/entity/{id}")
    EntityModelResponseDto updateEntity(@PathVariable(name = "id") UUID id, UpsertEntityRequestNewDto request);

    @DeleteMapping(value = "/api/v1/entity/{id}")
    void deleteEntityById(@PathVariable(name = "id")UUID id);
}
