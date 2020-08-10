package com.task.booking.controllers;

import com.task.booking.models.Resource;
import com.task.booking.repo.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private ResourceRepository resourceRepository;

    @GetMapping("/")
    public String greeting (Map <String, Object> model){
        return "greeting";
    }

    @GetMapping("/resource-add")
    public String addResource(){
        return "resource-add";
    }

    @PostMapping("/resource-add")
    public String addPostResource(@RequestParam String name, Model model){
        Resource res = new Resource();
        res.setNameResource(name);
        resourceRepository.save(res);
        return "redirect:/resource-all";
    }

    @GetMapping("/resource-all")
    public String allResource (Map<String, Object> model){
       Iterable <Resource>  resources = resourceRepository.findAll();
       model.put("resources", resources);
       return "resource-all";

    }

    @PostMapping("/resource-edit/{id}")
    public String editPostResource(@PathVariable(value="id") Long id, @RequestParam String nameResource){
        Resource resource = resourceRepository.findById(id).orElseThrow();
        resource.setNameResource(nameResource);
        resourceRepository.save(resource);

        return "redirect:/resource-all";
    }

    @GetMapping("/resource-edit/{id}")
    public String editResource(@PathVariable(value="id") Long id, Model model){
        if (!resourceRepository.existsById(id)) {
            return "redirect:/resource-all";
        }
        Optional<Resource> resource = resourceRepository.findById(id);
        ArrayList<Resource> res = new ArrayList<>();
        resource.ifPresent(res::add);
        model.addAttribute("resource", res);

        return "resource-edit";
    }

    @PostMapping("filter")
    public String filter (@RequestParam String filter, Map<String, Object> model){
        Iterable<Resource> resources;
        if (filter != null && !filter.isEmpty()){
            resources = resourceRepository.findByNameResourceContains(filter);
        } else
            resources = resourceRepository.findAll();

        model.put("resources", resources);
        return "resource-all";
    }

}
