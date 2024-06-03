package io.github.hefrankeleyn.utils;

import com.google.common.collect.Lists;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @Date 2024/6/3
 * @Author lifei
 */
public interface FieldUtils {

    /**
     * 查询类的注解类型
     * @param clazz
     * @param annotationClass
     * @return
     */
    static List<Field> findAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return findFields(clazz, (field) -> field.isAnnotationPresent(annotationClass));
    }

    static List<Field> findFields(Class<?> clazz, Function<Field, Boolean> function) {
        List<Field> result = Lists.newArrayList();
        while (Objects.nonNull(clazz)) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (function.apply(declaredField)) {
                    result.add(declaredField);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return result;
    }
}
