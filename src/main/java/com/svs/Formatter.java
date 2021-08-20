package com.svs;

/**
 * 用来获取格式化处理文本
 */
public class Formater {

    /**
     * 创建
     * 
     * @param voterNumber 投票人数目
     * @param candidate   候选人信息
     * @param voteMsg[]   投票信息
     * @return 返回创建用格式文本
     */
    public static String toCreate(String voterNumber, Candidate candidate, String voteMsg[]) {
        String result = "0\n" + voterNumber + "\n" + candidate.number + "\n";// 创建模式
        for (int i = 0; i < candidate.number; i++) {
            result = result.concat(candidate.name[i] + "\n" + candidate.msg[i] + "\n");
        }
        for (String str : voteMsg) {
            result = result.concat(str + "\n");
        }
        return result;
    }

    /**
     * 加入
     * 
     * @param voteMsg 投票信息
     * @return 返回加入用格式文本
     */
    public static String toJoin(String voteMsg[]) {
        String result = "1\n";
        for (String str : voteMsg) {
            result = result.concat(str + "\n");
        }
        return result;
    }
}
