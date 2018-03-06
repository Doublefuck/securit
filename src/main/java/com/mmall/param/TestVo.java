package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2018/2/26 0026.
 */
@Setter
@Getter
public class TestVo {

    @NotBlank
    private String msg;

    @NotNull
    private Integer id;

}
