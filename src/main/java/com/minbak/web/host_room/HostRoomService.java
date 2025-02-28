package com.minbak.web.host_room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // @Autowired 대신 사용
public class HostRoomService {

    private final HostRoomMapper hostRoomMapper; // 생성자 자동 생성

    public void insertHostRoom(HostRoomDTO hostRoomDTO) {
        hostRoomMapper.insertHostRoom(hostRoomDTO);
    }
    // 숙소 ID로 특정 숙소 정보 조회
    public HostRoomDTO getHostRoomById(int roomId) {
        return hostRoomMapper.selectHostRoomById(roomId);
    }

}
