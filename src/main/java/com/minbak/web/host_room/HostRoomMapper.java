package com.minbak.web.host_room;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HostRoomMapper {
    void insertHostRoom(HostRoomDTO hostRoomDTO);
    HostRoomDTO selectHostRoomById(int roomId);
}
