package com.minbak.web.roomoptions;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface RoomOptionsMapper {

    // 사용자가 선택한 편의시설을 포함하는 숙소 리스트 조회
    List<RoomOptionsDto> getRoomsByAmenities(@Param("amenities") List<String> amenities, @Param("size") int size);

    //  모든 숙소 조회 (편의시설 필터 없이)
    List<RoomOptionsDto> getAllRooms();
}
