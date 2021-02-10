package com.cos.book.domain;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY)
 //Relace.Any = 가짜 디비로 테스트 , Replace.NONE 실제 DB로 테스트
@DataJpaTest //Repository들을 다 IOC 등록해줌
public class BookRepositoryUnitTest {
	@Autowired
	private BookRepository bookRepository;
}
