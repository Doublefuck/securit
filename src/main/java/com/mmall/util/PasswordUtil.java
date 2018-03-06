package com.mmall.util;


import java.util.Date;
import java.util.Random;

/**
 * 密码工具类
 * Created by Administrator on 2018/3/1 0001.
 */
public class PasswordUtil {

    //密码字典：字母和数字
    private static final String[] word = {
            "a","b","c","d","e","f","g","h",
            "j","k","m","n","p","q","r","s",
            "t","u","v","w","x","y","z","A","B",
            "C","D","E","F","G","H","J","K","M",
            "N","P","Q","R","S","T","U","V","W",
            "X","Y","Z"
    };
    private static final String[] num = {
            "2","3","4","5","6","7","8","9"
    };

    /**
     * 随机生成密码
     * @return
     */
    public static String randomPassword(){
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random(new Date().getTime());
        boolean flag = false;
        //默认长度至少为8
        int length = random.nextInt(3) + 8;
        for(int i = 0;i < length;i++){
            if(flag){
                //组装数字
                stringBuffer.append(num[random.nextInt(num.length)]);
            }else{
                //组装字母
                stringBuffer.append(word[random.nextInt(word.length)]);
            }
            flag = !flag;
        }
        return stringBuffer.toString();
    }





    public static void main(String[] args) {
        System.out.println(randomPassword());
        String s1 = "abc";
        String s2 = s1;
        String s3 = new String("abc");
        String s4 = new String("abc");
        String s5 = s1;
        System.out.println(s1.equals(s4));
    }

}
