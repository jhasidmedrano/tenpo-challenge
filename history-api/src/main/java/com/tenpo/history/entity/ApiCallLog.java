package com.tenpo.history.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_call_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiCallLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String service;
    private String method;
    private String endpoint;
    private String request;
    private String response;
    private int status;

    private LocalDateTime timestamp;
}