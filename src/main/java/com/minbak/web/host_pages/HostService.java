package com.minbak.web.host_pages;

import com.minbak.web.file_upload.FileMapper;
import com.minbak.web.file_upload.ImageFileDto;
import com.minbak.web.host_pages.dto.HostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class HostService{

    @Autowired
    private CreateHostMapper createHostMapper;
    @Autowired
    private FileMapper fileMapper;
    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @Transactional
    public void insertRoom(HostDto hostDto) {
        // 🏡 1. 숙소 정보 `rooms` 테이블에 저장
        createHostMapper.insertRoom(hostDto);
    }

    public ImageFileDto saveFile(String uniqueFilename, String originalFilename ,int fileSize, int roomId, String type) throws IOException {

        // 파일 정보 DB 저장
        ImageFileDto imageFile = new ImageFileDto();
        //웹에서 사진을 다운받을 경로
        imageFile.setFileUrl("/uploads/" + uniqueFilename);
        //기존 파일 이름
        imageFile.setFileName(originalFilename);
        //파일 사이즈 인트로변환
        imageFile.setFileSize(fileSize);
        //rooms의 사진이라는 뜻
        imageFile.setEntityType(type); // type 타입으로 저장
        //해당 room의Id
        imageFile.setEntityId(roomId);
        // TODO 유저 아이디 추가
        fileMapper.insertImageFile(imageFile);

        return imageFile;
    }

    public void updateRoomImages(String fileUrl, int roomId) {
        // 이미지를 업데이트하는 쿼리 호출
        createHostMapper.updateRoomImages("/uploads/"+ fileUrl, roomId);
    }
}
