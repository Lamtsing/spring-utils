package com.lamtsing.utils.generator;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Lamtsing
 */
@Getter
@Setter
public class User {

    @TableId
    private Long id;

    private String userName;

    private String teacher;
}
