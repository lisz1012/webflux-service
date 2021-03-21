package com.lisz.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Document(value = "aaa")
public class Person implements Serializable {
	@Id
	private String id;
	@Field
	private String name;
	@Field
	private int age;
	@Field
	private Double price;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", age=" + age +
				", price=" + price +
				'}';
	}
}
