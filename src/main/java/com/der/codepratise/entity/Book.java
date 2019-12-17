package com.der.codepratise.entity;

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
        return id - other.getId();
    }
}
