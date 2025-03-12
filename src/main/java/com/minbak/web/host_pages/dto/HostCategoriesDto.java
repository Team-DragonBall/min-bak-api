package com.minbak.web.host_pages.dto;

import lombok.Data;

@Data
public class HostCategoriesDto {
    private Integer categoryId;  // 카테고리 ID (categories 테이블의 category_id)
    private String name;         // 카테고리 이름
}
