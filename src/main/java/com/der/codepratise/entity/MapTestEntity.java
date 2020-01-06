package com.der.codepratise.entity;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapTestEntity that = (MapTestEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sex, that.sex) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        if (id < 2) {
            return Hashing.murmur3_128().newHasher().putInt(id).putBoolean(sex).putString(name, Charsets.UTF_8).putString(description, Charsets.UTF_8).hash().hashCode();
        }
        return Objects.hash(id, name, sex, description);
    }
}
