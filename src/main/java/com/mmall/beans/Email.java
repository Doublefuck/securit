package com.mmall.beans;

import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * 封装邮件的信息
 * Created by Administrator on 2018/3/3 0003.
 */
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private String subject; //邮件主题

    private String message; //邮件信息

    private Set<String> receivers; //收件人

}
