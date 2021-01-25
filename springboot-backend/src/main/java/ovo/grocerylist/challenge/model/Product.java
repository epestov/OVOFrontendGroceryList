package ovo.grocerylist.challenge.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "products", indexes = @Index(columnList = "category"))
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank
	private String name;

	@NotBlank
	private String category;
	
	public Product() {}
	
	public Product(String name, String category) {
		super();
		this.name = name;
		this.category = category;
	}
	
	public Product(long id, String name, String category) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
