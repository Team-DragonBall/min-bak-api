package com.minbak.web.roomoptions;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RoomOptionsDto {
    private int roomId;
    private String name;
    private String content;
    private String address;
    private int price;
    private List<String> amenities; // 편의시설 목록 추가
}