package com.der.codepratise.enums;

import lombok.Getter;

/**
 * @program: guava-code-pratise
 * @description: ${description}
 * @author: long
 * @create: 2019-12-20 09:48
 */
public enum DerTestEnum {

    LI_RUI(51240,"李睿"),
    LIU_WEI(891204,"刘威"),
    SHILONG(790016,"世龙"),
    ;

    @Getter
    private int code;

    @Getter
    private String name;

    DerTestEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
