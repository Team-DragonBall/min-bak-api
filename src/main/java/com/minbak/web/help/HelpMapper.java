package com.minbak.web.help;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface HelpMapper {
    int insertHelp(HelpDto helpDto);

    // 전체 공지사항 개수 조회
    int getHelpCount();

    // 공지사항 목록 조회 (페이징 적용)
    List<HelpDto> getHelpList(@Param("limit") int limit, @Param("offset") int offset);

    // 검색된 공지사항 개수 조회
    int getSearchHelpCount(@Param("searchType") String searchType, @Param("searchQuery") String searchQuery);

    // 검색된 공지사항 목록 조회
    List<HelpDto> searchHelps(@Param("pageSize") int pageSize,
                              @Param("offset") int offset,
                              @Param("searchType") String searchType,
                              @Param("searchQuery") String searchQuery);

    // 공지사항 상세 조회
    HelpDto getHelpById(int noticeId);


    void updateHelp(HelpDto noticeDto);
    void deleteHelp(@Param("noticeId") int noticeId);



    // 공지사항 고정하기
    void pinHelp(Map<String, Object> params);

    // 공지사항 고정 해제하기
    void unpinHelp(int noticeId);

    // 고정된 공지사항과 일반 공지사항을 합쳐서 가져오기
    List<HelpDto> getHelpsWithPinned();
}

