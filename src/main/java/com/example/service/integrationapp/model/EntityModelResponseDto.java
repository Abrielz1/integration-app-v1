package com.example.service.integrationapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityModelResponseDto { //newDto

    private UUID id;

    private String name;

    Instant date;
}
