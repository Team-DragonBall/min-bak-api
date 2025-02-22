package com.minbak.web.roomoptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomOptionsController {

    private final RoomOptionsService roomOptionsService;

    @Autowired
    public RoomOptionsController(RoomOptionsService roomOptionsService) {
        this.roomOptionsService = roomOptionsService;
    }

    // 편의시설 필터 적용된 숙소 리스트를 Thymeleaf에서 렌더링
    @GetMapping
    public String getRoomsByAmenities(@RequestParam(required = false) List<String> amenities, Model model) {
        List<RoomOptionsDto> rooms = roomOptionsService.getRoomsByAmenities(amenities != null ? amenities : List.of());
        model.addAttribute("rooms", rooms); // 모델에 숙소 리스트 추가
        return "roomOptions/roomOption-list"; // Thymeleaf에서 사용할 HTML 파일명 (roomOption-list.html)
    }
}
