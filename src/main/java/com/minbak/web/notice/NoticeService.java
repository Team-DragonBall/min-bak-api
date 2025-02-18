package com.minbak.web.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    public void createNotice(NoticeDto noticeDto) {
        noticeDto.setCreatedAt(LocalDateTime.now()); // 현재 시간 설정
        noticeMapper.insertNotice(noticeDto);
    }
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


    // 공지사항 고정하기
    public void pinNotice(int noticeId, int pinnedNum) {
        // pinnedNum이 0이면 고정 해제, 1~10 사이의 값을 입력받음
        if (pinnedNum < 1 || pinnedNum > 10) {
            throw new IllegalArgumentException("Pinned number must be between 1 and 10.");
        }

        // 고정할 공지사항을 업데이트
        Map<String, Object> params = Map.of("noticeId", noticeId, "pinnedNum", pinnedNum);
        noticeMapper.pinNotice(params);
    }

    // 공지사항 고정 해제하기
    public void unpinNotice(int noticeId) {
        // 고정 해제
        noticeMapper.unpinNotice(noticeId);
    }

    // 고정된 공지사항과 일반 공지사항 가져오기
    public List<NoticeDto> getNoticesWithPinned() {
        return noticeMapper.getNoticesWithPinned();
    }

}
