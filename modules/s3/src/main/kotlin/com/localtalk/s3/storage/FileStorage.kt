package com.localtalk.s3.storage

import org.springframework.web.multipart.MultipartFile

interface FileStorage {

    /**
     * 파일을 지정된 디렉토리와 파일명으로 업로드합니다.
     *
     * @param directory 업로드할 디렉토리 경로
     * @param fileName 저장할 파일명
     * @param file 업로드할 파일
     * @return 업로드된 파일의 URL
     */
    fun uploadFile(directory: String, fileName: String, file: MultipartFile): String

    /**
     * 파일을 삭제합니다.
     *
     * @param directory 삭제할 파일이 있는 디렉토리 경로
     * @param fileName 삭제할 파일명
     * @return 삭제 성공 여부
     */
    fun deleteFile(directory: String, fileName: String): Boolean
}
