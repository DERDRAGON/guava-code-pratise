package com.der.codepratise.entity;

import com.google.common.collect.ImmutableSet;

import java.util.Collections;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author K0790016
 **/
public class Foo {
    private static final ImmutableSet<String> RESERVED_CODES =
            ImmutableSet.of("AZ", "CQ", "ZX");

    private final ImmutableSet<String> codes;

    public Foo(Iterable<String> codes) {
        this.codes = ImmutableSet.copyOf(codes);
        checkArgument(Collections.disjoint(this.codes, RESERVED_CODES));
    }
}