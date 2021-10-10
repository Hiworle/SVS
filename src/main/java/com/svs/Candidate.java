package com.svs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Candidate implements Serializable {
    public Candidate(int number) {
        this.number = number;
        name = new String[number];
        msg = new String[number]; // 应该规定每个msg的最大长度为500
    }

    Candidate() {
    }

    int number; // 候选人的数目
    String name[]; // 候选人的名字
    String msg[]; // 候选人的其他信息

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name[]) {
        this.name = name;
    }

    public void setMsg(String msg[]) {
        this.msg = msg;
    }

    // 重写toString
    public String toString() {
        return "{ number: " + this.number + ", name: " + Arrays.toString(this.name) + ", msg: " + Arrays.toString(msg)
                + " }";
    }

    // 返回键值对形式
    public Object toObj() {
        Map<String, Object> obj = new HashMap<String, Object>();

        obj.put("number", this.number);
        obj.put("name", this.name);
        obj.put("msg", this.msg);

        return obj;
    }
}
