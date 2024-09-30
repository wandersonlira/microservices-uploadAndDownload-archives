package com.udemy.springArchives.services

import com.udemy.springArchives.config.FileStorageConfig
import com.udemy.springArchives.exceptions.MyFileStorageException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


@Service
class FileStorageService (
    @Autowired fileStorageConfig: FileStorageConfig
) {
    private lateinit var fileStorageLocation: Path

    init {
        val path: Path = Paths.get(fileStorageConfig.uploadDir).toAbsolutePath().normalize()
        this.fileStorageLocation = path
        try {
            if (!Files.isDirectory(this.fileStorageLocation))
                Files.createDirectory(this.fileStorageLocation)
        } catch(e: Exception) {
            throw MyFileStorageException("Could not create the directory where the upload files will be stored", e)
        }
    }

    fun storeFile(file: MultipartFile): String {
        val fileName: String = StringUtils.cleanPath(file.originalFilename!!)
        try {
//            FileName..txt
            if (fileName.contains("..")) {
                println("Formato incorreto $fileName")
            }
            val targetLocation: Path = this.fileStorageLocation.resolve(fileName)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            return fileName
        }catch (e: Exception) {
            throw MyFileStorageException(
                "Could not store file $fileName . Please try again!", e
            )
        }
    }

    fun loadFileAsResource(fileName: String): Resource {
        try {
            val filePath: Path = this.fileStorageLocation.resolve(fileName).normalize()
            val resource: Resource = UrlResource(filePath.toUri())
            if (resource.exists()) return resource else throw Exception("File not found")
        } catch (e: Exception) {
            throw MyFileStorageException("File not found", e)
        }
    }
}