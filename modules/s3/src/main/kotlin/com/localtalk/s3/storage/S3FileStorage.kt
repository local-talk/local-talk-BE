package com.localtalk.s3.storage

import com.localtalk.s3.config.S3Properties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class S3FileStorage(
    private val s3Client: S3Client,
    private val s3Properties: S3Properties,
) : FileStorage {

    override fun uploadFile(directory: String, fileName: String, file: MultipartFile): String {
        val key = "$directory/$fileName"

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(s3Properties.defaultBucket)
            .key(key)
            .contentType(file.contentType)
            .build()

        s3Client.putObject(
            putObjectRequest,
            RequestBody.fromInputStream(file.inputStream, file.size),
        )

        return "https://${s3Properties.defaultBucket}.s3.${s3Properties.region}.amazonaws.com/$key"
    }

    override fun deleteFile(directory: String, fileName: String): Boolean {
        val key = "$directory/$fileName"

        val deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(s3Properties.defaultBucket)
            .key(key)
            .build()
        
        return runCatching { s3Client.deleteObject(deleteObjectRequest) }
            .isSuccess
    }
}
