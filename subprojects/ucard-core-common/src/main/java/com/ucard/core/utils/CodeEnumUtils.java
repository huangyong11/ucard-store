package com.ucard.core.utils;


import com.ucard.core.model.CodeEnum;


public class CodeEnumUtils {
    public static <E extends Enum & CodeEnum> E parse(Class<E> enumClass, int code) {
        for (E codeEnum : enumClass.getEnumConstants()) {
            if (codeEnum.getCode() == code) {
                return codeEnum;
            }
        }
        throw new IllegalArgumentException("Unknown code enum, code=" + code + ", enumClass=" + enumClass);
    }
}
