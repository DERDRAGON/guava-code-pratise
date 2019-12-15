package com.der.codepratise.BasicUtilities;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

/**
 * 前置条件
 * @author K0790016
 **/
@Slf4j
public class PreconditionsClient {

    public static void main(String[] args) {

        int value = 3;

        try {
            Preconditions.checkArgument(3>4);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            Preconditions.checkState(value > 4, "该值小于等于4");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            Preconditions.checkNotNull(null, "参数为空");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            Preconditions.checkArgument(value < 0, "参数期望为负数，实际参数为 %s ", value);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            Preconditions.checkElementIndex(3, 5);
            Preconditions.checkElementIndex(4, 2);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            Preconditions.checkPositionIndexes(4, 6, 8);
            Preconditions.checkPositionIndexes(4, 6, 5);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
