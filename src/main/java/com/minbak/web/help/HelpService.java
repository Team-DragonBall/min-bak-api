package com.minbak.web.help;

import com.minbak.web.common.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class HelpService {

    @Autowired
    private HelpMapper helpMapper;

    public void createHelp(HelpDto helpDto) {
        helpDto.setCreatedAt(LocalDateTime.now()); // 현재 시간 설정
        helpMapper.insertHelp(helpDto);
    }

    public PageDto<HelpDto> getHelpList(int currentPage, int pageSize, String searchType, String searchQuery) {
        int totalHelps;

        if (searchQuery.isEmpty()) {
            // 검색 조건이 없으면 전체 고객 서비스 개수 조회
            totalHelps = helpMapper.getHelpCount();
        } else {
            // 검색 조건이 있으면 검색된 고객 서비스 개수 조회
            totalHelps = helpMapper.getSearchHelpCount(searchType, searchQuery);
        }

        // 현재 페이지에 해당하는 고객 서비스 목록 가져오기
        int offset = (currentPage - 1) * pageSize;
        List<HelpDto> helps;

        if (searchQuery.isEmpty()) {
            // 검색 조건이 없으면 전체 고객 서비스 목록 가져오기
            helps = helpMapper.getHelpList(pageSize, offset);
        } else {
            // 검색 조건이 있을 때는 검색된 고객 서비스 목록 가져오기
            helps = helpMapper.searchHelps(pageSize, offset, searchType, searchQuery);
        }
        System.out.println("Total helps: " + totalHelps);
        System.out.println("Fetched helps size: " + helps.size());

        return new PageDto<>(currentPage, pageSize, totalHelps, helps);

    }

    // 상세 보기
    public HelpDto getHelpById(int noticeId) {
        return helpMapper.getHelpById(noticeId);
    }

    // 수정
    public void updateHelp(HelpDto helpDto) {
        helpMapper.updateHelp(helpDto);
    }

    // 삭제
    public void deleteHelp(int noticeId) {
        helpMapper.deleteHelp(noticeId);
    }

    // 고객 서비스 고정하기
    public void pinHelp(int helpId, int pinnedNum) {
        // pinnedNum이 0이면 고정 해제, 1~10 사이의 값을 입력받음
        if (pinnedNum < 1 || pinnedNum > 10) {
            throw new IllegalArgumentException("Pinned number must be between 1 and 10.");
        }

        // 고정할 고객 서비스 항목 업데이트
        Map<String, Object> params = Map.of("helpId", helpId, "pinnedNum", pinnedNum);
        helpMapper.pinHelp(params);
    }

    // 고객 서비스 고정 해제하기
    public void unpinHelp(int helpId) {
        // 고정 해제
        helpMapper.unpinHelp(helpId);
    }

    // 고정된 고객 서비스와 일반 고객 서비스 가져오기
    public List<HelpDto> getHelpsWithPinned() {
        return helpMapper.getHelpsWithPinned();
    }
}
