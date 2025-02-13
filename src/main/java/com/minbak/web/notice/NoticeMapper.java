package com.minbak.web.notice;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface NoticeMapper {
    int insertNotice(NoticeDto noticeDto);

    // 공지사항 목록 조회 (페이징 적용)
    List<NoticeDto> getNoticeList(@Param("limit") int limit, @Param("offset") int offset);
    // 전체 공지사항 개수 조회 (총 페이지 계산용)
    int getNoticeCount();
    // 공지사항 목록 상세 조회
    NoticeDto getNoticeById(int noticeId);


    void updateNotice(NoticeDto noticeDto);
    void deleteNotice(@Param("noticeId") int noticeId);

    // 공지사항 검색
    List<NoticeDto> searchNotices(
            @Param("searchType") String searchType,
            @Param("searchQuery") String searchQuery,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
