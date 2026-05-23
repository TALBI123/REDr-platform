package com.example.demo.controller;

import com.example.demo.models.Client;
import com.example.demo.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients/new")
    public String showForm(Model model) {
        model.addAttribute("client", new Client());
        return "client-form";
    }



    
    @PostMapping("/clients")
    public String saveClient(@Valid @ModelAttribute("client") Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client-form";
        }

        clientService.save(client);
        return "success";
    }
}