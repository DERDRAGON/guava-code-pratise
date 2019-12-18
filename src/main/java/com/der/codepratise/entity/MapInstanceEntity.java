package com.der.codepratise.entity;

import lombok.Data;

/**
 * @author K0790016
 **/
@Data
public class MapInstanceEntity extends MapTestEntity {

    private String favouriteMoive;

    public MapInstanceEntity() {
    }

    public MapInstanceEntity(Integer id, String name, String favouriteMoive) {
        super(id, name);
        this.favouriteMoive = favouriteMoive;
    }
}
