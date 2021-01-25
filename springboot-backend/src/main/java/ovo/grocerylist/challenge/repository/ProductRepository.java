package ovo.grocerylist.challenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ovo.grocerylist.challenge.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	public List<Product> findByCategory(String category);
}
