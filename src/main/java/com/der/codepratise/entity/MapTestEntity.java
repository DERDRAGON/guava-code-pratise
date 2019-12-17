package com.der.codepratise.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author K0790016
 **/
@Getter
@Setter
@ToString
public class MapTestEntity implements Comparable<MapTestEntity> {

    private Integer id;

    private String name;

    private Boolean sex;

    private String description;

    public MapTestEntity() {
    }

    public MapTestEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo(MapTestEntity map) {
        return this.id - map.getId();
    }
}
