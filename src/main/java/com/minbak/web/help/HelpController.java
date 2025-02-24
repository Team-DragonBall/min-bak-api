package com.minbak.web.help;

import com.minbak.web.common.dto.PageDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/help")
public class HelpController {

    @Autowired
    private HelpService helpService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("helpDto", new HelpDto());
        return "help/help-create";
    }

    @PostMapping("/create")
    public String createHelp(@Valid HelpDto helpDto, BindingResult result) {
        if (result.hasErrors()) {
            return "help/help-create"; // 오류 발생 시 다시 작성 페이지로 이동
        }
        helpService.createHelp(helpDto);
        return "redirect:/admin/help";
    }

    @GetMapping
    public String getHelpList(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "5") int pageSize,
                              @RequestParam(defaultValue = "") String searchType,
                              @RequestParam(defaultValue = "") String searchQuery,
                              Model model) {
        // 고정된 고객 서비스와 일반 목록을 합쳐서 가져오기
        List<HelpDto> helpsWithPinned = helpService.getHelpsWithPinned();

        // 페이지네이션 처리
        PageDto<HelpDto> pageDto = helpService.getHelpList(page, pageSize, searchType, searchQuery);

        model.addAttribute("pageDto", pageDto);
        model.addAttribute("helpsWithPinned", helpsWithPinned);  // 고정된 목록 포함
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchQuery", searchQuery);

        return "help/help-list";
    }

    @GetMapping("/detail/{id}")
    public String getHelpDetail(@PathVariable("id") int helpId, Model model) {
        HelpDto help = helpService.getHelpById(helpId);
        model.addAttribute("help", help);
        return "help/help-detail";
    }

    @GetMapping("/update/{noticeId}")
    public String showUpdateForm(@PathVariable Integer noticeId, Model model) {
        HelpDto helpDto = helpService.getHelpById(noticeId);
        model.addAttribute("helpDto", helpDto);
        return "help/help-update";
    }

    @PostMapping("/update")
    public String updateHelp(@Valid @ModelAttribute HelpDto helpDto, BindingResult result) {
        if (result.hasErrors()) {
            return "help/help-update";
        }
        helpService.updateHelp(helpDto);
        return "redirect:/admin/help";
    }

    @PostMapping("/delete/{noticeId}")
    public String deleteHelp(@PathVariable("noticeId") int noticeId) {
        helpService.deleteHelp(noticeId);
        return "redirect:/admin/help";
    }

    @GetMapping("/pin")
    public String pinHelp(@RequestParam("helpId") int helpId, @RequestParam("pinnedNum") int pinnedNum) {
        if (pinnedNum < 0 || pinnedNum > 10) {
            throw new IllegalArgumentException("Pinned number must be between 0 and 10.");
        }

        if (pinnedNum == 0) {
            helpService.unpinHelp(helpId);
        } else {
            helpService.pinHelp(helpId, pinnedNum);
        }

        return "redirect:/admin/help";
    }

    @RequestMapping("/unpin")
    public String unpinHelp(@RequestParam("helpId") int helpId) {
        helpService.unpinHelp(helpId);
        return "redirect:/admin/help";
    }
}
