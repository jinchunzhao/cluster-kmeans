package com.jy.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Objects;

/**
 * List集合工具类
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-02-03 10:49
 */
public class ListUtil {

    /**
     * 集合深度copy
     * 注意事项（实体类需要实现Serializable）
     *
     * @param sourceList 源集合
     * @param <E>        泛型
     * @return 新集合
     */
    public static  <E> List<E> deepCopy(List<E> sourceList) {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        List<E> dest = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            out = new ObjectOutputStream(byteOut);
            out.writeObject(sourceList);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            in = new ObjectInputStream(byteIn);
            dest = (List<E>) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException("集合深度copy，出现异常", e);
        } finally {
            try {
                if (Objects.nonNull(out)) {
                    out.close();
                }
                if (Objects.nonNull(in)) {
                    in.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("集合深度copy，关闭流，出现异常", e);
            }
        }
        return dest;
    }

}
