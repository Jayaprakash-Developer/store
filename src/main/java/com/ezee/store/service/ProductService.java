package com.ezee.store.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ezee.store.dao.ProductDAO;
import com.ezee.store.dto.ProductDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepo;

	public void saveProduct(ProductDAO productDAO) {
		productRepo.addProduct(productDAO);
	}

	@Cacheable(value = { "localProduct" }, key = "#id", unless = "#result == null")
	public ProductDTO getProductById(int id) {
		return productRepo.fetchById(id);
	}

	public List<ProductDTO> getAllProduct() {
		return productRepo.fetchAllCustomer();
	}

	@CachePut(value = "localProduct", key = "#id")
	public ProductDAO updateProduct(int id, Map<String, Object> product) {
		ProductDAO fetchById = productRepo.fetchProductDAOById(id);
		if (product != null) {
			product.forEach((key, value) -> {
				switch (key) {
				case "productName":
					fetchById.setProductName((String) value);
					break;
				case "categoryId":
					fetchById.setCategoryId((int) value);
					break;
				case "weightId":
					fetchById.setWeightId((int) value);
					break;
				case "packageType":
					fetchById.setPackageType((String) value);
					break;
				case "brandName":
					fetchById.setBrandName((String) value);
					break;
				case "price":
					fetchById.setPrice((double) value);
					break;
				case "expireDay":
					fetchById.setExpireDay((Date) value);
					break;
				case "description":
					fetchById.setDescription((String) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION);
				}
			});

			int updatedRows = productRepo.update(fetchById);
			if (updatedRows == 0) {
				throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION);
			}
		} else {
			throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
		}
		return fetchById;
	}

	@CacheEvict(value = "localProduct", key = "#id")
	public int deleteproduct(int id) {
		return productRepo.delete(id);
	}

}
