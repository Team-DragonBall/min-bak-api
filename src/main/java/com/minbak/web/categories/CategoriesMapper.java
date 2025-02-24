package com.minbak.web.categories;

import org.apache.ibatis.annotations.*;

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
        // 카테고리 순서 업데이트
        @Update("UPDATE categories SET category_order = #{categoryOrder} WHERE category_id = #{categoryId}")
        void updateCategoryOrder(@Param("categoryId") int categoryId, @Param("categoryOrder") int categoryOrder);

        // 카테고리 아이콘 URL 추가/수정
        void updateCategoryIcon(@Param("categoryId") int categoryId, @Param("categoryiconUrl") String categoryiconUrl);


        // 카테고리 순서 업데이트 (개별적으로 변경)
        @Update("UPDATE categories SET category_order = #{categoryOrder} WHERE category_id = #{categoryId}")
        void updateCategoryOrder11(@Param("categoryId") int categoryId, @Param("categoryOrder") int categoryOrder);

        // 드래그 앤 드롭을 위한 순서 변경 (기존 순서 조정)
        void reorderCategories111(@Param("categoryId") int categoryId,
                               @Param("newOrder") int newOrder,
                               @Param("oldOrder") int oldOrder,
                               @Param("minOrder") int minOrder,
                               @Param("maxOrder") int maxOrder);

        //새로운 카테고리 이름이 중복되는지 확인용
        @Select("SELECT COUNT(*) FROM categories WHERE name = #{name}")
        int countByName(@Param("name") String name);
        // 중복 확인 API 추가
        @Select("SELECT * FROM categories WHERE name = #{name} LIMIT 1")
        CategoriesDto findCategoryByName(@Param("name") String name);
}
