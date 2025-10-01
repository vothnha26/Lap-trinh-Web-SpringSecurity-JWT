package vn.iot.star.Controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import vn.iot.star.Model.Customer;


@RestController
@EnableMethodSecurity
public class CustomerController {
	final private List<Customer> customers = List.of(
		new Customer("001","Trình Văn Lưu",null,"luuag999@gmail.com"),
		new Customer("002","Văn Lưu", null,"luuga000@gmail.com")
	);
	
	@GetMapping("/")
	public ResponseEntity<String> home(){
		return ResponseEntity.ok("Welcome to the Home Page! Try /hello or login to access /customer/all.");
	}
	
	@GetMapping("/hello")
	public ResponseEntity<String> hello(){
		return ResponseEntity.ok("hello is Guest");
	}
	
	@GetMapping("/customer/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<Customer>> getCustomList()
	{
		List<Customer> list = this.customers;
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/customer/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Customer> getCustomList(@PathVariable("id") String id)
	{
		List<Customer> customers = this.customers.stream().filter(custom -> custom.getId().equals(id)).toList();
		return ResponseEntity.ok(customers.get(0));
	}
	
}

