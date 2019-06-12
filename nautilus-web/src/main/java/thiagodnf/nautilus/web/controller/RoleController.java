package thiagodnf.nautilus.web.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.web.model.Role;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.RoleService;
import thiagodnf.nautilus.web.util.Privileges;

@Controller
@RequestMapping("/roles")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@GetMapping("/add")
	@PreAuthorize ("hasAuthority('CREATE_ROLE')")
	public String create(Model model) {
		
		model.addAttribute("role", new Role());
		model.addAttribute("privileges", Privileges.getPrivilegies());

		return "form-role";
	}
	
	@PostMapping("/save")
	@PreAuthorize ("hasAuthority('SAVE_ROLE')")
	public String save(
			@Valid Role role,
			BindingResult bindingResult, 
			RedirectAttributes ra, 
			Model model) {

		if (bindingResult.hasErrors()) {

			model.addAttribute("privileges", Privileges.getPrivilegies());
			
			return "form-role";
		}

		if (StringUtils.isBlank(role.getId())) {
			role.setId(null);
		}

		Role savedRole = roleService.save(role);

		flashMessageService.success(ra, "msg.role.save.success", savedRole.getName());
		
		return "redirect:/admin#roles";
	}
	
	@GetMapping("/edit/{id:.+}")
	@PreAuthorize ("hasAuthority('EDIT_ROLE')")
	public String edit(Model model, @PathVariable("id") String id) {
		
		model.addAttribute("role", roleService.findById(id));
		model.addAttribute("privileges", Privileges.getPrivilegies());
		
		return "form-role";
	}
	
	@GetMapping("/view/{id:.+}")
	@PreAuthorize ("hasAuthority('VIEW_ROLE')")
	public String view(Model model, @PathVariable("id") String id) {
		
		model.addAttribute("role", roleService.findById(id));
		
		return "role";
	}
	
	@PostMapping("/delete/{id:.+}")
	@PreAuthorize ("hasAuthority('DELETE_ROLE')")
	public String deleteById(Model model, @PathVariable("id") String id, RedirectAttributes ra) {

		Role role = roleService.findById(id);

		roleService.delete(role);

		flashMessageService.success(ra, "msg.role.delete.success", role.getName());

		return "redirect:/admin#roles";
	}
}
