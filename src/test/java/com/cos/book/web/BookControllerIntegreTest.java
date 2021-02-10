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

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
/*
 * //통합 테스트 (모든 Bean들을 똑같이 IOC 올리고 테스트 하는 것)
 *@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
 * - 실제 톰켓을 올리는게 아니라 다른 톰켓으로 테스트
 *@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
 * - 실제 톰켓으로 테스트할 때 사용된다.
 *@AutoConfigureMockMvc
 * - MockMvc를 loc에 등록해준다.
 *@Transactional
 * - 각 각의 테스트함수가 종료될 때마다 트랜잭션을 rollback 해주는 어노테이션
*/
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)// 실제 톰켓을 올리는게 아니라 다른 톰켓으로 테스트
public class BookControllerIntegreTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
	}
	
	
	@Test
	public void save_테스트() throws Exception {
		//given(테스트를 하기 위한 준비)
		Book book = new Book(null, "스프링 따라하기","코스");
		String content = new ObjectMapper().writeValueAsString(book);
		
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
		books.add(new Book(null,"스프링부트 따라하기","코스"));
		books.add(new Book(null,"리엑트 따라하기","코스"));
		books.add(new Book(null,"JUNIT 따라하기","코스"));
		bookRepository.saveAll(books);
		
		//when
		ResultActions resultActions = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8)); //기대하는 값 
		
		//then = 내가 기대하는 값
		resultActions
		 .andExpect(status().isOk())
		 .andExpect(jsonPath("$", Matchers.hasSize(3))) //3개를 기대함 = 컬렉션 사이즈 
		 .andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
		 .andDo(MockMvcResultHandlers.print());
	}
	//세번째 테스트
	@Test
	public void findById_테스트() throws Exception{
		//given
		Long id = 2L;
		
		List<Book> books = new ArrayList<>();
		books.add(new Book(null,"스프링부트 따라하기","코스"));
		books.add(new Book(null,"리엑트 따라하기","코스"));
		books.add(new Book(null,"JUNIT 따라하기","코스"));
		bookRepository.saveAll(books);
		//when
		ResultActions resultActions = mockMvc.perform(get("/book/{id}",id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.title").value("리엑트 따라하기"))
		.andDo(MockMvcResultHandlers.print());
	}
	
	//네번째 테스트
	@Test
	public void update_테스트() throws Exception{
		//given
		Long id = 3L;
		List<Book> books = new ArrayList<>();
		books.add(new Book(null,"스프링부트 따라하기","코스"));
		books.add(new Book(null,"리엑트 따라하기","코스"));
		books.add(new Book(null,"JUNIT 따라하기","코스"));
		bookRepository.saveAll(books); //저장하기
		
		Book book = new Book(null,"C++ 따라하기","코스");
		String content = new ObjectMapper().writeValueAsString(book); //업데이트할 데이터 
		
		//when
		ResultActions resultActions = mockMvc.perform(put("/book/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8) //=application/json 요청을 json으로
				.content(content) //실제로 던질 데이터 
				.accept(MediaType.APPLICATION_JSON_UTF8)); // 응답을 json으로 받겠다.
		
		//then
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(3L))
		.andExpect(jsonPath("$.title").value("C++ 따라하기"))
		.andDo(MockMvcResultHandlers.print());
	}
	//다섯번째 테스트
	@Test
	public void delete_테스트() throws Exception{
		//given
		Long id = 3L;
		List<Book> books = new ArrayList<>();
		books.add(new Book(null,"스프링부트 따라하기","코스"));
		books.add(new Book(null,"리엑트 따라하기","코스"));
		books.add(new Book(null,"JUNIT 따라하기","코스"));
		bookRepository.saveAll(books);
		
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
