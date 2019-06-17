package thiagodnf.nautilus.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.dto.RoleDTO;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.RoleService;
import thiagodnf.nautilus.web.util.Messages;
import thiagodnf.nautilus.web.util.Privileges;

@Controller
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@GetMapping("/add")
	public String form(RoleDTO roleDTO, Model model) {
		
		model.addAttribute("roleDTO", roleDTO);
		model.addAttribute("privileges", Privileges.getPrivilegies());

		return "roleForm";
	}
	
	@PostMapping("/save")
	public String save(@Valid RoleDTO roleDTO, BindingResult bindingResult, RedirectAttributes ra, Model model) {

		if (bindingResult.hasErrors()) {
			return form(roleDTO, model);
		}
		
		RoleDTO saved = null;

		if (roleDTO.getId() == null) {
			saved = roleService.create(roleDTO);
		} else {
			saved = roleService.update(roleDTO);
		}
		
		flashMessageService.success(ra, Messages.ROLE_SAVE_SUCCESS, saved.getName());
		
		return "redirect:/admin/roles";
	}
	
	@GetMapping("/edit/{id:.+}")
	public String edit(@PathVariable String id, Model model) {
		return form(roleService.findById(id), model);
	}
	
	@PostMapping("/delete/{id:.+}")
	public String deleteById(@PathVariable String id, RedirectAttributes ra, Model model) {

		RoleDTO roleDTO = roleService.deleteById(id);

		flashMessageService.success(ra, Messages.ROLE_DELETE_SUCCESS, roleDTO.getName());

		return "redirect:/admin/roles";
	}
}
