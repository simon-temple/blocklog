package io.simont.blocklog.utils;

import io.simont.blocklog.bytes.Bytes512;
import io.simont.blocklog.bytes.Bytes64;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

public class Classes {

    private Classes() {
        //Utility classes have no public constructor
    }

    public static Stream<Field> supportedFixedLengthFields(Class<?> klass) {
        return Arrays.stream(klass.getDeclaredFields())
                .filter(x -> x.getType().isPrimitive()
                        || x.getType().getName().equals(Bytes64.class.getName())
                        || x.getType().getName().equals(Bytes512.class.getName()));
    }
}
