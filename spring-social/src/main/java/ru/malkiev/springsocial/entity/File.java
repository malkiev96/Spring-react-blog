package ru.malkiev.springsocial.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FILES")
@EqualsAndHashCode(callSuper = true)
@Data
public class File extends BaseFile{

}
