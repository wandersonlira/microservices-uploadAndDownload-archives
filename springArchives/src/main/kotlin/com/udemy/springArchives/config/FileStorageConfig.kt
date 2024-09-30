package com.udemy.springArchives.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "file")
class FileStorageConfig (
    val uploadDir: String
)