package com.minbak.web.roomoptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomOptionsService {

    private final RoomOptionsMapper roomOptionsMapper;

    @Autowired
    public RoomOptionsService(RoomOptionsMapper roomOptionsMapper) {
        this.roomOptionsMapper = roomOptionsMapper;
    }
    public List<RoomOptionsDto> getRoomsByAmenities(List<String> amenities) {
        if (amenities == null || amenities.isEmpty()) {
            return roomOptionsMapper.getAllRooms();
        }
        return roomOptionsMapper.getRoomsByAmenities(amenities, amenities.size());
    }

}
