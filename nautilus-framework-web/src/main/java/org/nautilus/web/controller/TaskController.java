package org.nautilus.web.controller;

import org.nautilus.web.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("")
    public String showTasks(Model model) {
        return "tasks/tasks";
    }

    @GetMapping("/generate-approx-pareto-fronts")
    public String generateApproxParetoFronts(Model model) {

        taskService.generateApproxParetoFronts();

        return "redirect:/";
    }
}
