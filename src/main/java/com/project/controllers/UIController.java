//package com.project.controllers;
//
//import com.project.model.Product;
//import com.project.repositories.ElasticSearchQuery;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@Controller
//public class UIController {
//
//    @Autowired
//    private ElasticSearchQuery elasticSearchQuery;
//    
//    private void loadProjekts() throws IOException {
//        int size = elasticSearchQuery.searchAllDocuments().size();
//        System.out.println("Sample Projekts Loaded size " + size); 
//        if (size == 0) {
//            int count = 10;
//            for(int x = 1; x <= count; x++) {
//                insertNewProject(x);
//            }
//        }
//        else System.out.println("nnot first");
//        
//    }
//    
//    private void insertNewProject(int x) throws IOException {
//        Product product = new Product();
//        product.setId("test " + x);
//        product.setDescription("ABC " + x);
//        product.setName("CDA " + x);
//        product.setPrice(x / 10.0);
//        elasticSearchQuery.createOrUpdateDocument(product);
//        System.out.println("Sample Projekts Loaded " + x);   
//    }
//    
//    @GetMapping("/preloadProduct")
//    public String preloadPro() throws IOException {
//        int size = elasticSearchQuery.searchAllDocuments().size();
//        if (size == 0) loadProjekts();
//        else System.out.println("not first");
//        return "index";
//    }
//    
//    @GetMapping("/")
//    public String viewHomePage(Model model) throws IOException {
//        model.addAttribute("listProductDocuments",elasticSearchQuery.searchAllDocuments());
//        return "index";
//    }
//
//    @PostMapping("/saveProduct")
//    public String saveProduct(@ModelAttribute("product") Product product) throws IOException {
//        elasticSearchQuery.createOrUpdateDocument(product);
//        return "redirect:/";
//    }
//
//    @GetMapping("/showFormForUpdate/{id}")
//    public String showFormForUpdate(@PathVariable(value = "id") String id, Model model) throws IOException {
//
//        Product product = elasticSearchQuery.getDocumentById(id);
//        model.addAttribute("product", product);
//        return "updateProductDocument";
//    }
//
//    @GetMapping("/showNewProductForm")
//    public String showNewEmployeeForm(Model model) {
//        // create model attribute to bind form data
//        Product product = new Product();
//        model.addAttribute("product", product);
//        return "newProductDocument";
//    }
//
//    @GetMapping("/deleteProduct/{id}")
//    public String deleteProduct(@PathVariable(value = "id") String id) throws IOException {
//
//        this.elasticSearchQuery.deleteDocumentById(id);
//        return "redirect:/";
//    }
//}