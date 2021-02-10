package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

//단위테스트 Controller, Filter, ControllerAdvice)

@Slf4j
@WebMvcTest //메모리에 Controller, Filter, ControllerAdvice가 띄워진다
public class BookControllerUnitTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BookService bookservice;
	
	//첫번째 테스트
	//BDDMockito 패턴 = given,when,then
	@Test
	public void save_테스트() throws Exception {
		//given(테스트를 하기 위한 준비)
		Book book = new Book(null, "스프링 따라하기","코스");
		String content = new ObjectMapper().writeValueAsString(book);
		when(bookservice.저장하기(book)).thenReturn(new Book(1L,"스프링 따라하기","코스")); //스텁
		
		//when (테스트 실행)
		ResultActions resultActions = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8) //=application/json 요청을 json으로
				.content(content) //실제로 던질 데이터 
				.accept(MediaType.APPLICATION_JSON_UTF8)); // 응답을 json으로 받겠다.
		
		//then (검증)
		resultActions
		 .andExpect(status().isCreated())
		 .andExpect(jsonPath("$.title").value("스프링 따라하기"))
		 .andDo(MockMvcResultHandlers.print());
	}
	
	//두번째 테스트
	@Test
	public void findAll_테스트() throws Exception {
		//given
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L,"스프링부트 따라하기","코스"));
		books.add(new Book(2L,"리엑트 따라하기","코스"));
		when(bookservice.모두가져오기()).thenReturn(books);
		
		//when
		ResultActions resultActions = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8)); //기대하는 값 
		
		//then = 내가 기대하는 값
		resultActions
		 .andExpect(status().isOk())
		 .andExpect(jsonPath("$", Matchers.hasSize(2))) //2개를 기대함 = 컬렉션 사이즈 
		 .andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
		 .andDo(MockMvcResultHandlers.print());
	}
	
	//세번째 테스트
	@Test
	public void findById_테스트() throws Exception{
		//given
		Long id = 1L;
		when(bookservice.한건가져오기(id)).thenReturn(new Book(1L,"자바 공부하기","쌀"));
		
		//when
		ResultActions resultActions = mockMvc.perform(get("/book/{id}",id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.title").value("자바 공부하기"))
		.andDo(MockMvcResultHandlers.print());
	}
	//네번째 테스트
	@Test
	public void update_테스트() throws Exception{
		//given
		Long id = 1L;
		Book book = new Book(null, "C++ 따라하기","코스"); //기존 값
		String content = new ObjectMapper().writeValueAsString(book);
		
		when(bookservice.수정하기(id, book)).thenReturn(new Book(1L,"C++ 따라하기","코스")); //업데이트 될 값
		
		//when
		ResultActions resultActions = mockMvc.perform(put("/book/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8) //=application/json 요청을 json으로
				.content(content) //실제로 던질 데이터 
				.accept(MediaType.APPLICATION_JSON_UTF8)); // 응답을 json으로 받겠다.
		
		//then
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.title").value("C++ 따라하기"))
		.andDo(MockMvcResultHandlers.print());
	}
	//다섯번째 테스트
		@Test
		public void delete_테스트() throws Exception{
			//given
			Long id = 1L;
			
			when(bookservice.삭제하기(id)).thenReturn("ok"); 
			
			//when
			ResultActions resultActions = mockMvc.perform(delete("/book/{id}",id)
					.contentType(MediaType.TEXT_PLAIN)); //=application/json 요청을 json으로
			
			//then
			resultActions
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
			
			MvcResult requestResult = resultActions.andReturn();
			String result = requestResult.getResponse().getContentAsString(); //상태코드가 맞는지
			
			assertEquals("ok", result);
		}
}
