package thiagodnf.nautilus.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.core.model.SelectedSolution;
import thiagodnf.nautilus.core.util.Formatter;
import thiagodnf.nautilus.web.dto.UserDTO;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.service.UserService;

@Controller
@RequestMapping("/experiment")
public class ExperimentController {

    @Autowired
    private ExecutionService executionService;

    @Autowired
    private PluginService pluginService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("")
    public String showResult(Model model) {

        User user = securityService.getLoggedUser().getUser();

        Map<String, String> users = new HashMap<>();
        Map<String, Date> dates = new HashMap<>();
        Map<String, Execution> parents = new HashMap<>();
        Map<String, String> times = new HashMap<>();

        for (UserDTO dto : userService.findAll()) {
            users.put(dto.getId(), dto.getEmail());
        }

        List<Execution> executions = new ArrayList<>();

        for (Execution execution : executionService.findAll()) {

            if (execution.getSolutions() == null || execution.getSolutions().isEmpty()) {
                continue;
            }
            
            if (execution.getSelectedSolutions() == null || execution.getSelectedSolutions().isEmpty()) {
                continue;
            }
            
            executions.add(execution);
        }
        
        for (Execution execution : executions) {

            Execution parent = executionService.getParent(execution.getId());
            
            parents.put(execution.getId(), parent);

            for (SelectedSolution selectedSolution : execution.getSelectedSolutions()) {
                
                Date selectedDate = selectedSolution.getSelectionDate();
                
                long diff = selectedDate.getTime() - parent.getCreationDate().getTime();
                
                times.put(execution.getId(), Formatter.interval(diff));
            }
        }
        
        model.addAttribute("users", users);
        model.addAttribute("executions", executions);
        model.addAttribute("dates", dates);
        model.addAttribute("times", times);
        
        model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
        
        return "experiment";
    }
}
