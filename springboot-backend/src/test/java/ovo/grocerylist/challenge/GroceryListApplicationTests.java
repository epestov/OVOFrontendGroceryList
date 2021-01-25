package ovo.grocerylist.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import ovo.grocerylist.challenge.model.Product;
import ovo.grocerylist.challenge.repository.ProductRepository;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class GroceryListApplicationTests {

	@Test
	public void contextLoads() {}
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Test
	public void testGetAllProducts() throws Exception {
		
		List<Product> products = createTestProductsList();
		productRepository.saveAll(products);
		
		MvcResult mvcResult = mockMvc.perform(
				get("/products"))
				.andExpect(status().isOk()).andReturn();
		
		String actualResponse = mvcResult.getResponse().getContentAsString();
		String expectedResponse = objectMapper.writeValueAsString(products);
		
		assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedResponse);
	}
	
	@Test
	public void testGetProductsByCategory() throws Exception {

		List<Product> products = createTestProductsList();
		productRepository.saveAll(products);
		
		MvcResult mvcResult = mockMvc.perform(
				get("/products/category?q=Bakery"))
				.andExpect(status().isOk()).andReturn();
		
		String actualResponse = mvcResult.getResponse().getContentAsString();
		String expectedResponse = objectMapper.writeValueAsString(createTestBakeryList());
		
		assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedResponse);
	}

	@Test
	public void testCreateNewProduct() throws Exception {

		Product newProduct = new Product("Ice Cream", "Dairy");
		Product savedProduct = new Product(1, "Ice Cream", "Dairy");
		
		mockMvc.perform(
				post("/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProduct))
				.with(csrf())
			).andExpect(status().isOk())
			.andExpect(content().string(objectMapper.writeValueAsString(savedProduct)));
	}

	@Test
	public void testUpdateProduct() throws Exception {

		Product existingProduct = new Product("Ice Cream", "Dairy");
		Product updatedProduct = new Product(1, "Jogurt", "Dairy");
		
		productRepository.save(existingProduct);
		
		mockMvc.perform(
				put("/products/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedProduct))
				.with(csrf())
			).andExpect(status().isOk())
			.andExpect(content().string(objectMapper.writeValueAsString(updatedProduct)));
	}

	@Test
	public void testDeleteProduct() throws Exception {

		Product existingProduct = new Product("Ice Cream", "Dairy");
		productRepository.save(existingProduct);

		mockMvc.perform(
				delete("/products/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(existingProduct))
				.with(csrf())
			).andExpect(status().isOk());
	}

	@Test
	public void testDeleteNonExisitngProduct() throws Exception {

		Product product = new Product(2, "Ice Cream", "Dairy");

		mockMvc.perform(
				delete("/products/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(product))
				.with(csrf())
			).andExpect(status().isNotFound());
	}

	@Test
	public void testCategoryMustNotBeBlank() throws Exception {
		
		Product product = new Product("", "");
		mockMvc.perform(
				post("/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(product))
				.with(csrf())
			).andExpect(status().isBadRequest());
	}

	private List<Product> createTestBakeryList() {

		List<Product> products = new ArrayList<Product>();
		products.add(new Product(1, "Bread", "Bakery"));
		products.add(new Product(2, "Cake", "Bakery"));
		products.add(new Product(3, "Cookie", "Bakery"));
		
		return products;
	}
	
	private List<Product> createTestProductsList() {
		
		List<Product> products = new ArrayList<Product>();
		products.addAll(createTestBakeryList());
		products.add(new Product(4, "Milk", "Dairy"));
		products.add(new Product(5, "Butter", "Dairy"));
		products.add(new Product(6, "Tiramisu", "Frozen"));
		
		return products;
	}
}
