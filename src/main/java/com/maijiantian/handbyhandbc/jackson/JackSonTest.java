package com.maijiantian.handbyhandbc.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class JackSonTest {
    public static void main(String[] args) throws Exception {

        User user = new User("zhangsan", "123");
        User user2 = new User("lisi", "123");
        ArrayList<User> list = new ArrayList<>();
        list.add(user);
        list.add(user2);

        ObjectMapper objectMapper = new ObjectMapper();
        // 把对象转换成json格式
        String s = objectMapper.writeValueAsString(list);
        // 反序列化普通对象
        //User user1 = objectMapper.readValue(s, User.class);
        //System.out.println(user1.toString());

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, User.class);
        ArrayList<User> list2 =    objectMapper.readValue(s, javaType);

        for (User user1 : list2) {
            System.out.println(user1.toString());

        }

    }
}
