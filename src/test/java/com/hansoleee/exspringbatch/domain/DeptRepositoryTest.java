package com.hansoleee.exspringbatch.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootTest
@Transactional
public class DeptRepositoryTest {

    @Autowired
    DeptRepository deptRepository;

    @Test
    @Rollback(value = false)
    void dept01() throws Exception {
        IntStream.range(1, 100).forEach(i -> deptRepository.save(new Dept(i, "name" + i, "loc" + i)));
    }
}
