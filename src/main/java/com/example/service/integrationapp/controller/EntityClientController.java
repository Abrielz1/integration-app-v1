package com.example.service.integrationapp.controller;

import com.example.service.integrationapp.clients.OpenFeignClient;
import com.example.service.integrationapp.model.EntityModelResponseDto;
import com.example.service.integrationapp.model.UpsertEntityRequestNewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/client/entity")
@RequiredArgsConstructor
public class EntityClientController {

    private final OpenFeignClient clientSender;

    @GetMapping
    public ResponseEntity<List<EntityModelResponseDto>> getEntityList() {

        return ResponseEntity.ok(clientSender.getEntityList());
    }

    @GetMapping("/{name}")
    public ResponseEntity<EntityModelResponseDto> getEntityByName(@PathVariable String name) {

        return ResponseEntity.ok(clientSender.getEntityByName(name));
    }


    @PostMapping
    public ResponseEntity<EntityModelResponseDto> createEntity(@RequestBody UpsertEntityRequestNewDto newDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(clientSender.createEntity(newDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModelResponseDto> updateEntity(@PathVariable UUID id,
                                                               @RequestBody UpsertEntityRequestNewDto newDto) {

        return ResponseEntity.ok(clientSender.updateEntity(id, newDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModelResponseDto> deleteEntityById(@PathVariable UUID id) {

        clientSender.deleteEntityById(id);

        return ResponseEntity.noContent().build();
    }
}


