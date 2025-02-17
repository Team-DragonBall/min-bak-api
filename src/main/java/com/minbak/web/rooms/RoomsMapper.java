package com.minbak.web.rooms;

import com.minbak.web.dto.RoomsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RoomsMapper {

    void insertRoom (RoomsDto roomsDto);
    Optional<RoomsDto> selectRoomById(int id);
    List<RoomsDto> selectRoomsByPage(@Param("size") int size, @Param("offset") int offset);
    int countTotalRooms();
    int updateRoom(RoomsDto roomsDto);
    void deleteRoom(int id);
    List<RoomsDto> selectRoomsWithUser(@Param("size") int size, @Param("offset") int offset);
}
