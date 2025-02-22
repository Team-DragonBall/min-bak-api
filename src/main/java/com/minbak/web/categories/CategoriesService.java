package com.minbak.web.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class CategoriesService {

    private final CategoriesMapper categoriesMapper;

    @Autowired
    public CategoriesService(CategoriesMapper categoriesMapper) {
        this.categoriesMapper = categoriesMapper;
    }

    // 전체 카테고리 조회
    public List<CategoriesDto> getAllCategories() {
        return categoriesMapper.findAllCategories();
    }

    // 특정 카테고리 조회 (ID 기준)
    public CategoriesDto getCategoryById(int id) {
        return categoriesMapper.findCategoryById(id);
    }

    // 새 카테고리 생성 + 중복 검사
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int createCategory(CategoriesDto categoriesDto) {
        int count = categoriesMapper.countByName(categoriesDto.getName());
        if (count > 0) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
        }

        // 자동으로 category_order 값을 설정하여 삽입
        return categoriesMapper.createCategory(categoriesDto);
    }
    // 중복 확인 API 추가
    public CategoriesDto getCategoryByName(String name) {
        return categoriesMapper.findCategoryByName(name);
    }

    // 카테고리 수정
    public int updateCategory(CategoriesDto categoriesDto) {
        return categoriesMapper.updateCategory(categoriesDto);
    }

    // 카테고리 삭제
    @Transactional
    public int deleteCategory(int id) {
        return categoriesMapper.deleteCategory(id);
    }


    // ✅ 카테고리 순서 변경 (드래그 앤 드롭)
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public void changeCategoryOrder(int categoryId, int newOrder) {
        // 기존 카테고리 정보 가져오기
        CategoriesDto category = categoriesMapper.findCategoryById(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("해당 카테고리가 존재하지 않습니다.");
        }

        int oldOrder = category.getCategoryOrder();

        // 기존 순서를 기준으로 범위 설정
        int minOrder = Math.min(oldOrder, newOrder);
        int maxOrder = Math.max(oldOrder, newOrder);

        // 기존 카테고리들의 순서를 자동 정렬
        categoriesMapper.reorderCategories111(categoryId, newOrder, oldOrder, minOrder, maxOrder);
    }

}