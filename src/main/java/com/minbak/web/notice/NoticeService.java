package com.minbak.web.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    // 공지사항 생성
    public void createNotice(NoticeDto noticeDto) {
        noticeDto.setCreatedAt(LocalDateTime.now()); // 현재 시간 설정
        noticeMapper.insertNotice(noticeDto);
    }

    // 공지사항 목록 조회
    public NoticePageDto<NoticeDto> getNoticeList(int currentPage, int pageSize, String searchType, String searchQuery) {
        int totalNotices;

        if (searchQuery.isEmpty()) {
            // 검색 조건이 없으면 전체 공지사항 개수 조회
            totalNotices = noticeMapper.getNoticeCount();
        } else {
            // 검색 조건이 있으면 검색된 공지사항 개수 조회
            totalNotices = noticeMapper.getSearchNoticeCount(searchType, searchQuery);
        }

        // 현재 페이지에 해당하는 공지사항 목록 가져오기
        int offset = (currentPage - 1) * pageSize;
        List<NoticeDto> notices;

        if (searchQuery.isEmpty()) {
            // 검색 조건이 없으면 전체 공지사항 목록 가져오기
            notices = noticeMapper.getNoticeList(pageSize, offset);
        } else {
            // 검색 조건이 있을 때는 검색된 공지사항 목록 가져오기
            notices = noticeMapper.searchNotices(pageSize, offset, searchType, searchQuery);
        }

        return new NoticePageDto<>(currentPage, pageSize, totalNotices, notices);
    }

    // 공지사항 상세 조회
    public NoticeDto getNoticeById(int noticeId) {
        return noticeMapper.getNoticeById(noticeId);
    }

    // 공지사항 수정
    public void updateNotice(NoticeDto noticeDto) {
        noticeMapper.updateNotice(noticeDto);
    }

    // 공지사항 삭제
    public void deleteNotice(int noticeId) {
        noticeMapper.deleteNotice(noticeId);
    }

    // 공지사항 고정 또는 해제
    public void pinNotice(int noticeId, int isPinned) {
        noticeMapper.pinNotice(noticeId, isPinned);
    }

    // 고정된 공지사항 순서 변경 (선택 사항)
    public void updatePinnedOrder(int noticeId, int newOrder) {
        noticeMapper.updatePinnedOrder(noticeId, newOrder);
    }
}
