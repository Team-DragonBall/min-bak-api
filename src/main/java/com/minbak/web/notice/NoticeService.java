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

    //페이지네이션 구현, 전체 목록보기
    public NoticePageDto<NoticeDto> getNoticeList(int currentPage, int pageSize){
        //천체 공지사항 개수 조회
        int totalNotices = noticeMapper.getNoticeCount();

        //현재 페이지에 해당하는 공지사항 목록 가져오기
        int offset = (currentPage - 1 ) * pageSize; // offset 계산하는 것 = offset은 DB에서 데이터를 가져올 때, 몇 번째 항목부터 가져올지 결정하는 값.
        List<NoticeDto> notices = noticeMapper.getNoticeList(pageSize, offset);
        // NoticePageDto 생성
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


}
