package com.minbak.web.rooms;

public class RoomException extends RuntimeException {

    // 생성자 1: 예외 메시지만 받을 경우
    public RoomException(String message) {
        super(message);  // 부모 클래스의 생성자를 호출하여 메시지를 전달
    }

    // 생성자 2: 예외 메시지와 원인(원본 예외)을 받을 경우
    public RoomException(String message, Throwable cause) {
        super(message, cause);  // 부모 클래스의 생성자를 호출하여 메시지와 원인을 전달
    }
}
