package com.der.codepratise.entity;

import com.google.common.collect.ComparisonChain;
import lombok.Data;

/**
 * @author K0790016
 **/
@Data
public class Book implements Comparable {

    private Integer id;

    private String name;

    private String author;

    public Book() {
    }

    public Book(Integer id) {
        this.id = id;
    }

    @Override
    public int compareTo(Object o) {
        Book other = (Book) o;
        return ComparisonChain.start().compare(id, other.getId()).compare(name, other.getName()).result();
    }
}
