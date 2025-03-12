package com.minbak.web.host_pages;

import com.minbak.web.host_pages.dto.HostCategoriesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostCategoriesService {
    @Autowired
    private HostCategoriesMapper hostCategoriesMapper;

    // 전체 카테고리 조회
    public List<HostCategoriesDto> getAllCategories(){
        return hostCategoriesMapper.getAllCategories();
    }

    // 특정 숙소의 카테고리 조회
    public List<HostCategoriesDto> getCategoriesByRoomId(int roomId){
        return hostCategoriesMapper.getCategoriesByRoomId(roomId);
    }
}
