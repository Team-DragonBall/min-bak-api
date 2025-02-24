package com.minbak.web.notice;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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



    // 공지사항 고정하기
    void pinNotice(Map<String, Object> params);

    // 공지사항 고정 해제하기
    void unpinNotice(int noticeId);

    // 고정된 공지사항과 일반 공지사항을 합쳐서 가져오기
    List<NoticeDto> getNoticesWithPinned();
}

