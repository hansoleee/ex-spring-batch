package com.hansoleee.exspringbatch.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
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
public class Dept {

    @Id
    @Column(name = "dept_no")
    private Integer no;
    private String name;
    private String loc;
}
