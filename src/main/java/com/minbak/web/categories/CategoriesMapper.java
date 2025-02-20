package com.minbak.web.categories;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoriesMapper {

        // 전체 카테고리 조회
        List<CategoriesDto> findAllCategories();

        // 특정 카테고리 조회 (ID 기준)
        CategoriesDto findCategoryById(@Param("categoryId") int categoryId);

        // 새 카테고리 추가
        int createCategory(CategoriesDto categoriesDto);

        // 카테고리 수정
        int updateCategory(CategoriesDto categoriesDto);

        // 카테고리 삭제
        int deleteCategory(int id);

        // 카테고리 순서 업데이트 (개별적으로 변경)
        @Update("UPDATE categories SET category_order = #{categoryOrder} WHERE category_id = #{categoryId}")
        void updateCategoryOrder11(@Param("categoryId") int categoryId, @Param("categoryOrder") int categoryOrder);

        // 드래그 앤 드롭을 위한 순서 변경 (기존 순서 조정)
        void reorderCategories111(@Param("categoryId") int categoryId,
                               @Param("newOrder") int newOrder,
                               @Param("oldOrder") int oldOrder,
                               @Param("minOrder") int minOrder,
                               @Param("maxOrder") int maxOrder);
}
