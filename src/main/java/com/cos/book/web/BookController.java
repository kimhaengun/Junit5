package com.cos.book.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
//생성자를 만들어줌 final을 걸면 위어노테이션이 생성자를 자동으로 만들어줌.
@RestController
public class BookController {
	
	private final BookService bookService;
	
	@CrossOrigin
	@PostMapping("/book")
	public ResponseEntity<?> save(@RequestBody Book book){
		//@requestBody를 걸면 json으로 받아옴.
		return new ResponseEntity<>(bookService.저장하기(book),HttpStatus.CREATED); //200응답해줌
	}
	
	@CrossOrigin
	@GetMapping("/book")
	public ResponseEntity<?> findAll(){
		return new ResponseEntity<>(bookService.모두가져오기(),HttpStatus.OK); //200응답해줌
	}
	
	@CrossOrigin
	@GetMapping("/book/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) throws IllegalAccessException{
		return new ResponseEntity<>(bookService.한건가져오기(id),HttpStatus.OK); //200응답해줌
	}
	
	@CrossOrigin
	@PutMapping("/book/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Book book) throws IllegalAccessException{
		return new ResponseEntity<>(bookService.수정하기(id, book),HttpStatus.OK);
	}
	
	@CrossOrigin
	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id){
		return new ResponseEntity<>(bookService.삭제하기(id),HttpStatus.OK);
	}
}
