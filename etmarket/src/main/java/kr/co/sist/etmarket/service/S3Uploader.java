package kr.co.sist.etmarket.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

@Slf4j
@Service
public class  S3Uploader {
    private final AmazonS3 amazonS3;
    private final String bucket;

    // 생성자 생성
    public S3Uploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    // 파일 업로드 메서드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // 파일 이름에서 공백을 제거한 새로운 파일 이름 생성
        String originalFileName = multipartFile.getOriginalFilename();

        // UUID를 파일명에 추가
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        // 업로드할 파일 경로 생성
        String fileName = dirName + "/" + uniqueFileName;
        log.info("fileName: " + fileName);

        // MultipartFile을 File 객체로 변환
        File uploadFile = convert(multipartFile);

        // S3에 파일 업로드
        String uploadImageUrl = putS3(uploadFile, fileName);

        // 로컬에 생성된 임시파일 제거
        removeNewFile(uploadFile);

        // 업로드된 파일의 URL 반환
        return uploadImageUrl;
    }

    // MultipartFile을 File 객체로 변환하는 메서드
    private File convert(MultipartFile file) throws IOException {
        // 파일 이름 생성
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        // 파일 변환
        File convertFile = new File(uniqueFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                log.error("파일 변환 중 오류 발생: {}", e.getMessage());
                throw e;
            }
            return convertFile;
        }
        throw new IllegalArgumentException(String.format("파일 변환에 실패했습니다. %s", originalFileName));
    }

    // S3에 파일 업로드
    private String putS3(File uploadFile, String fileName) {
        // 업로드 요청 생성, PublicRead 권한 부여
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // 업로드된 파일의 URL 반환
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    // 로컬에 생성된 임시 파일 삭제
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    // S3에서 파일 삭제
    public void deleteFile(String fileName) {
        try {
            // URL 디코딩을 통해 원래의 파일 이름을 가져옵니다.
            String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("Deleting file from S3: " + decodedFileName);

            // S3에서 파일 삭제
            amazonS3.deleteObject(bucket, decodedFileName);
        } catch (UnsupportedEncodingException e) {
            log.error("Error while decoding the file name: {}", e.getMessage());
        }
    }

    // 기존 파일을 삭제하고 새 파일을 업로드
    public String updateFile(MultipartFile newFile, String oldFileName, String dirName) throws IOException {
        // 기존 파일 삭제
        log.info("S3 oldFileName: " + oldFileName);
        deleteFile(oldFileName);

        // 새 파일 업로드
        return upload(newFile, dirName);
    }
}
