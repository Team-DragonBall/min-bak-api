package com.minbak.web.dto;

import lombok.Getter;

@Getter
public class ReviewDto {
    Integer reviewId;
    Integer userId;
    Integer bookId;
    //String content;
    Integer score;

    public ReviewDto(Integer reviewId, Integer userId, Integer score) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.score = score;
    }
}


/**
 * -- minbak_db.review definition
 *
 * CREATE TABLE `review` (
 *   `review_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '리뷰 고유 ID',
 *   `user_id` int(11) NOT NULL COMMENT '작성한 회원 ID',
 *   `book_id` int(11) NOT NULL COMMENT '리뷰 대상 예약 ID',
 *   `content` varchar(300) NOT NULL COMMENT '리뷰 내용',
 *   `score` int(11) NOT NULL COMMENT '리뷰 점수',
 *   PRIMARY KEY (`review_id`),
 *   KEY `user_id` (`user_id`),
 *   KEY `book_id` (`book_id`),
 *   CONSTRAINT `review_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
 *   CONSTRAINT `review_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 */