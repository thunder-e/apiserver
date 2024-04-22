package org.clover.apiserver.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${org.clover.upload.path}")
    private String uploadPath;


    @PostConstruct //생성자 대신 초기화시킬때
    public void init() { //폴더를 만드는

        File tempFolder = new File(uploadPath);
        if(!tempFolder.exists()) {

            tempFolder.mkdir();

        }


        uploadPath = tempFolder.getAbsolutePath();
        log.info("---------------------------------------");
        log.info(uploadPath);

    }

    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {

        if(files == null || files.isEmpty()) {
            return null;//List.of() : 비어있는 리스트
        }

        List<String> uploadNames = new ArrayList<>();

        for (MultipartFile file : files) {
            //동일한 이름이 생기지 않도록 랜덤 uuid
            String saveName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();

            Path savePath = Paths.get(uploadPath, saveName);

            try {
                //원본파일 업로드
                Files.copy(file.getInputStream(), savePath);

                //이미지 파일이라면 썸네일
                String contentType = file.getContentType(); /* Mime type */
                if(contentType != null || contentType.startsWith("image")) {
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + saveName);
                    Thumbnails.of(savePath.toFile()).size(200,200).toFile(thumbnailPath.toFile());
                }

                uploadNames.add(saveName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        } //end for


        return uploadNames;

    }


    public ResponseEntity<Resource> getFiles(String fileName) {

        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);

        if(!resource.isReadable()) {
            resource = new FileSystemResource(uploadPath + File.separator + "default.jpg");
        }

        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    public void deleteFiles(List<String> fileNames) {
        if(fileNames == null || fileNames.isEmpty()) {
            return;
        }

        fileNames.forEach(fileName -> {

            //썸네일 삭제
            String thumbnailFileName = "s_" + fileName;

            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);

            try {
                Files.deleteIfExists(thumbnailPath);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }



        });

    }

}
