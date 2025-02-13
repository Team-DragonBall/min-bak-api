package com.minbak.web.notice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeDto {
    private Integer noticeId; // 공지사항 ID

    @NotNull(message = "작성자 ID는 필수입니다.")
    private Integer userId; // 작성자 ID

    @NotBlank(message = "제목을 입력해주세요.")
    private String title; // 공지사항 제목

    @NotBlank(message = "내용을 입력해주세요.")
    private String content; // 공지사항 내용

    private LocalDateTime createdAt; // 생성 시간
}
