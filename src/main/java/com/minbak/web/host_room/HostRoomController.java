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
@RequestMapping("/host/room")
@RequiredArgsConstructor
public class HostRoomController {

    private final HostRoomService hostRoomService; // 서비스 주입

    @GetMapping("/form")
    public String showHostRoomForm(Model model) {
        model.addAttribute("hostRoom", new HostRoomDTO());
        return "host/host_room_form";
    }

    @PostMapping("/submit")
    public String submitHostRoomForm(@ModelAttribute HostRoomDTO hostRoomDTO, HttpSession session) {
        // 현재 로그인한 사용자 ID 가져오기 (예시)
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/admin/login"; // 로그인 안 되어 있으면 로그인 페이지로 리디렉트
        }

        hostRoomDTO.setUserId(userId); // ✅ userId 설정
        hostRoomService.insertHostRoom(hostRoomDTO); // ✅ 저장 실행

        return "redirect:/host/room/success";
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "host/host_room_success"; // 성공 메시지 페이지
    }
}
