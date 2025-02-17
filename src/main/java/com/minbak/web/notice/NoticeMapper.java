package com.minbak.web.notice;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
@Mapper
public interface NoticeMapper {
    int insertNotice(NoticeDto noticeDto);

    // 전체 공지사항 개수 조회
    int getNoticeCount();

    // 공지사항 목록 조회 (페이징 적용)
    List<NoticeDto> getNoticeList(@Param("limit") int limit, @Param("offset") int offset);

    // 검색된 공지사항 개수 조회
    int getSearchNoticeCount(@Param("searchType") String searchType, @Param("searchQuery") String searchQuery);

    // 검색된 공지사항 목록 조회
    List<NoticeDto> searchNotices(@Param("pageSize") int pageSize,
                                  @Param("offset") int offset,
                                  @Param("searchType") String searchType,
                                  @Param("searchQuery") String searchQuery);

    // 공지사항 상세 조회
    NoticeDto getNoticeById(int noticeId);

    void updateNotice(NoticeDto noticeDto);
    void deleteNotice(@Param("noticeId") int noticeId);
}

