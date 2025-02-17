package com.minbak.web.notice;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("noticeDto", new NoticeDto());
        return "notice/notice-create";
    }

    @PostMapping("/create")
    public String createNotice(@Valid NoticeDto noticeDto, BindingResult result) {
        if (result.hasErrors()) {
            return "notice/notice-create";// 오류 발생 시 다시 작성 페이지로 이동
        }
        noticeService.createNotice(noticeDto);
        return "redirect:/admin/notice/list";
    }


    // 페이징 기능 + 검색 기능이 추가된 공지사항 목록 조회
    @GetMapping("/list")
    public String getNoticeList(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "5") int pageSize,
                                @RequestParam(defaultValue = "") String searchType,
                                @RequestParam(defaultValue = "") String searchQuery,
                                Model model) {
        // 페이지네이션 처리
        NoticePageDto<NoticeDto> noticePageDto = noticeService.getNoticeList(page, pageSize);

        model.addAttribute("notices", noticePageDto.getObjects());  // 공지사항 목록
        model.addAttribute("currentPage", noticePageDto.getCurrentPage());  // 현재 페이지
        model.addAttribute("totalPages", noticePageDto.getTotalPages());  // 총 페이지 수
        model.addAttribute("startPage", noticePageDto.getStartPage());  // 네비게이션 시작 페이지
        model.addAttribute("endPage", noticePageDto.getEndPage());  // 네비게이션 끝 페이지
        model.addAttribute("hasPrev", noticePageDto.isHasPrev());  // 이전 페이지 여부
        model.addAttribute("hasNext", noticePageDto.isHasNext());  // 다음 페이지 여부

        // 검색 기준과 검색어도 모델에 추가
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchQuery", searchQuery);

        return "notice/notice-list";
    }

    @GetMapping("/detail/{id}")
    public String getNoticeDetail(@PathVariable("id") int noticeId, Model model) {
        NoticeDto notice = noticeService.getNoticeById(noticeId);
        model.addAttribute("notice", notice);
        return "notice/notice-detail"; // 상세 페이지
    }


    @GetMapping("/update/{noticeId}")
    public String showUpdateForm(@PathVariable Integer noticeId, Model model) {
        NoticeDto noticeDto = noticeService.getNoticeById(noticeId);
        System.out.println("불러온 공지사항: " + noticeDto); // 디버깅 코드. Dto를 잘 불러오는지 확인용.
        model.addAttribute("noticeDto", noticeDto);
        return "notice/notice-update";
    }

@PostMapping("/update")
public String updateNotice(@Valid @ModelAttribute NoticeDto noticeDto, BindingResult result) {
    System.out.println("수정할 공지사항 ID: " + noticeDto.getNoticeId());

    if (result.hasErrors()) {
        System.out.println("유효성 검사 실패: " + result.getAllErrors()); // 전체 오류 메시지 출력

        // 개별 필드별 오류 출력
        result.getFieldErrors().forEach(error -> {
            System.out.println("오류 필드: " + error.getField() + " / 메시지: " + error.getDefaultMessage());
        });

        return "notice/notice-update"; // 유효성 검사 실패 시 다시 update 페이지로 이동
    }

    noticeService.updateNotice(noticeDto);
    System.out.println("수정 완료 후 목록으로 이동");
    return "redirect:/admin/notice/list";
}


    @PostMapping("/delete/{id}")
    public String deleteNotice(@PathVariable("id") int noticeId) {
        noticeService.deleteNotice(noticeId);
        return "redirect:/admin/notice/list"; // 삭제 후 목록으로 이동
    }

}

