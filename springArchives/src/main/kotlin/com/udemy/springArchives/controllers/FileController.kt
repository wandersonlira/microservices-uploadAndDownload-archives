package com.udemy.springArchives.controllers

import com.udemy.springArchives.files.UploadFileResponseVO
import com.udemy.springArchives.services.FileStorageService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/file/v1")
class FileController (
    @Autowired private val service: FileStorageService
) {

    @PostMapping("/uploadFile")
    fun uploadFile(@RequestParam("file") file: MultipartFile): UploadFileResponseVO {
        val fileName = service.storeFile(file)
        val fileDownloadUri: String = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/file/v1/uploadFile/")
            .path(fileName)
            .toUriString()
        return UploadFileResponseVO(
            fileName, fileDownloadUri, file.contentType!!, file.size
        )
    }

    @PostMapping("/uploadMultipleFiles")
    fun uploadMultipleFiles(@RequestParam("files") files: ArrayList<MultipartFile>): List<UploadFileResponseVO> {
        return files.map { file -> uploadFile(file) }
    }

    @GetMapping("/downloadFile/{filename:.+}")
    fun downloadFile(@PathVariable filename: String,
                     request: HttpServletRequest): ResponseEntity<Resource> {
        val resource: Resource = service.loadFileAsResource(filename)
        var contentType: String = ""
        try {
            contentType = request.servletContext.getMimeType(resource.file.absolutePath)
        } catch (e: Exception) {
            throw Exception("Deu Merda!")
        }

        if (contentType.isBlank()) contentType = "application/octet-stream"

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
            .body(resource)
    }
}