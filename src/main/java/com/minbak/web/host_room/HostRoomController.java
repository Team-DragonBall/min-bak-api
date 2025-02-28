package com.minbak.web.host_room;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("user/host/room") // 변경된 부분
@RequiredArgsConstructor
public class HostRoomController {

    private final HostRoomService hostRoomService; // 서비스 주입

    @GetMapping("/form")
    public String showHostRoomForm(Model model) {
        model.addAttribute("hostRoom", new HostRoomDTO());
        return "host-room/host_room_form";
    }

    @PostMapping("/submit")
    public String submitHostRoomForm(@ModelAttribute HostRoomDTO hostRoomDTO, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            // 사용자가 로그인 페이지로 이동하기 전에 현재 URL을 저장
            session.setAttribute("redirectAfterLogin", "/user/host/room/form"); // 변경된 부분
            return "redirect:/admin/login";
        }

        hostRoomDTO.setUserId(userId);
        hostRoomService.insertHostRoom(hostRoomDTO);

        return "redirect:/user/host/room/success"; // 변경된 부분
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "host-room/host_room_success"; // 성공 메시지 페이지
    }
}
