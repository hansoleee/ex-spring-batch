package com.hansoleee.exspringbatch.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DeptTarget {

    @Id
    @Column(name = "dept_no")
    private Integer no;
    private String name;
    private String loc;
}
