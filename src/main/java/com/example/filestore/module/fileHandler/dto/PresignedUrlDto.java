package com.example.filestore.module.fileHandler.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PresignedUrlDto {
    private String presignedUrl;
    private Long fileId;
}
