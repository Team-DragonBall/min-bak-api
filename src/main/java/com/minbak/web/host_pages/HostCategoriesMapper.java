package com.minbak.web.host_pages;

import com.minbak.web.host_pages.dto.HostCategoriesDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HostCategoriesMapper {
    // 전체 카테고리 목록 가져오기
    List<HostCategoriesDto> getAllCategories();

    // 특정 숙소의 카테고리 가져오기
    List<HostCategoriesDto> getCategoriesByRoomId(@Param("roomId") int roomId);
}
