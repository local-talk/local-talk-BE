package com.localtalk.s3.config

import org.springframework.boot.test.context.TestComponent
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request

@TestComponent
class S3TestFixtures(
    val s3Client: S3Client
) {

    fun createBucket(bucketName: String) {
        try {
            val createBucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build()
            s3Client.createBucket(createBucketRequest)
        } catch (_: Exception) {
        }
    }

    fun cleanupBucket(bucketName: String) {
        try {
            val listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build()

            val response = s3Client.listObjectsV2(listRequest)
            response.contents().forEach { s3Object ->
                val deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Object.key())
                    .build()
                s3Client.deleteObject(deleteRequest)
            }
        } catch (_: Exception) {
        }
    }

    fun deleteBucket(bucketName: String) {
        try {
            cleanupBucket(bucketName)
            val deleteBucketRequest = DeleteBucketRequest.builder()
                .bucket(bucketName)
                .build()
            s3Client.deleteBucket(deleteBucketRequest)
        } catch (_: Exception) {
        }
    }

    fun setupTestBucket(bucketName: String = "localtalk-storage") {
        createBucket(bucketName)
    }

    fun teardownTestBucket(bucketName: String = "localtalk-storage") {
        deleteBucket(bucketName)
    }
}
