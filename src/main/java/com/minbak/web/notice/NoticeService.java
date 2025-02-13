package com.minbak.web.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    public void createNotice(NoticeDto noticeDto) {
        noticeDto.setCreatedAt(LocalDateTime.now()); // 현재 시간 설정
        noticeMapper.insertNotice(noticeDto);
    }

    // 페이지네이션 구현, 전체 목록 보기
    public List<NoticeDto> getNoticeList(int page, int limit) {
        int offset = (page - 1) * limit; // offset 계산
        return noticeMapper.getNoticeList(limit, offset);
    }
    // 총 페이지 수 계산
    public int getTotalPages(int limit) {
        int totalNotices = noticeMapper.getNoticeCount();
        return (int) Math.ceil((double) totalNotices / limit);
    }

    //상세 보기
    public NoticeDto getNoticeById(int noticeId) {
        return noticeMapper.getNoticeById(noticeId);
    }
    //수정
    public void updateNotice(NoticeDto noticeDto) {
        noticeMapper.updateNotice(noticeDto);
    }
    //삭제
    public void deleteNotice(int noticeId) {
        noticeMapper.deleteNotice(noticeId);
    }


    // 공지사항 검색
    public List<NoticeDto> searchNotices(String searchType, String searchQuery, int limit, int offset) {
        return noticeMapper.searchNotices(searchType, searchQuery, limit, offset);
    }
}
