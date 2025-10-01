// File: vn.iotstar.repository.ProductRepository.java
package vn.iotstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Các phương thức cơ bản như save, findById, findAll, deleteById được kế thừa.

}