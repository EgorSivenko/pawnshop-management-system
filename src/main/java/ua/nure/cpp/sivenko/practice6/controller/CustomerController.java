package ua.nure.cpp.sivenko.practice6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.cpp.sivenko.practice6.service.CustomerService;
import ua.nure.cpp.sivenko.practice6.model.Customer;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String getCustomers(@RequestParam(value = "id", required = false) Long customerId, Model model) {
        if (customerId != null) {
            Customer customer = customerService.getCustomerById(customerId);
            if (customer != null) {
                model.addAttribute("customers", Collections.singletonList(customer));
            }
        } else {
            List<Customer> customers = customerService.getAllCustomers();
            model.addAttribute("customers", customers);
        }
        return "customer/customers";
    }

    @GetMapping("/customers/add")
    public String addCustomerForm(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "customer/add_customer";
    }

    @PostMapping("/customers")
    public String addCustomer(@ModelAttribute("customer") Customer customer, Model model) {
        try {
            customerService.addCustomer(customer);
        } catch (SQLException e) {
            model.addAttribute("error", e.getMessage());
            return "customer/add_customer";
        }
        return "redirect:/customers";
    }

    @GetMapping("/customers/edit/{customerId}")
    public String updateCustomerForm(@PathVariable Long customerId, Model model) {
        Customer customer = customerService.getCustomerById(customerId);
        model.addAttribute("customer", customer);
        return "customer/update_customer";
    }

    @PostMapping("/customers/{customerId}")
    public String updateCustomer(@PathVariable Long customerId, @ModelAttribute("customer") Customer customer, Model model) {
        try {
            Customer customerById = customerService.getCustomerById(customerId);
            if (!Objects.equals(customerById, customer)) {
                customerService.updateCustomer(customerId, customer);
            }
        } catch (SQLException e) {
            model.addAttribute("error", e.getMessage());
            return "customer/update_customer";
        }
        return "redirect:/customers";
    }

    @GetMapping("/customers/{customerId}")
    public String deleteCustomer(@PathVariable Long customerId, Model model) {
        try {
            customerService.deleteCustomer(customerId);
        } catch (SQLException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/customers";
    }
}
