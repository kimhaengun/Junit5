package com.cos.book.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
/*
 * BookService 함수를 실행하기 위해서는 BookReposiory가 메모리에 띄워져야 하기 때문에
 * BoardRepository => 가짜 객체로 만들 수 있다. 
 * Mock = 가짜로 객체를 띄운다.
*/

import com.cos.book.domain.BookRepository;
@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {
	
	@InjectMocks // BookService 객체가 만들어 질 때 BookServiceUnitTest 파일에
	             // @Mock로 등록된 모든 애들을 주입받는다.
	private BookService bookService;
	
	@Mock
	private BookRepository bookRepository;
}
