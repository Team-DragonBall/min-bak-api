package com.minbak.web;

import com.minbak.web.board.categories.BoardCategoriesMapper;
import com.minbak.web.board.comments.BoardCommentDto;
import com.minbak.web.board.comments.BoardCommentsMapper;
import com.minbak.web.board.posts.BoardPostDto;
import com.minbak.web.board.posts.BoardPostsMapper;
import com.minbak.web.review.ReviewDto;
import com.minbak.web.review.ReviewMapper;
import com.minbak.web.review.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MinbakApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	BoardCommentsMapper boardCommentsMapper;

	@Autowired
	ReviewMapper reviewMapper;

	@Autowired
	BoardPostsMapper boardPostsMapper;

	@Autowired
	BoardCategoriesMapper boardCategoriesMapper;

	@Test
	void inputBoardData(){

		for(int i = 2; i <= 10;i++){
			BoardCommentDto boardCommentDto = new BoardCommentDto();
			boardCommentDto.setPostId(i);
			boardCommentDto.setAuthor("작성자"+i);
			boardCommentDto.setContent("게시글에 공감합니다"+i+"번 내용입니다.");
			boardCommentsMapper.createComment(boardCommentDto);
		}

	}

	@Test
	void inputReview(){
		for (int i = 1;i <10;i++){
			ReviewDto reviewDto = new ReviewDto();
			reviewDto.setContent(i+"숙소가 깨끗해요.");
			reviewDto.setUserId(i);
			reviewDto.setBookId((i%3)+1);
			reviewDto.setScore(4);

			reviewMapper.createReview(reviewDto);
		}
	}
}
