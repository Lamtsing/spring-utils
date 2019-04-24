package com.lamtsing.utils.generator;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Lamtsing
 */
@Entity
@Getter
@Setter
public class User {

    @Id
    private Long id;

    private String userName;

    private String teacher;
}
