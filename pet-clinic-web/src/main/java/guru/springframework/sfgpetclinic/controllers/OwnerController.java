package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.Binding;
import java.util.List;
import java.util.Map;

@RequestMapping("/owners") // Dictates the root route as "owners"
@Controller
public class OwnerController {

    public static final String VIEWS_OWNER_CREATE_UPDATE = "owners/createOrUpdateOwnerForm";

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

   /* @RequestMapping({"","/", "/index", "/index.html"})
    public String listOwners(Model model) {

        model.addAttribute("owners", this.ownerService.findAll());

        return "owners/index";
    }*/

    @RequestMapping("/find")
    public String findOwners(Model model) {

        model.addAttribute("owner", Owner.builder().build());



        return "owners/findOwners";
    }

    @GetMapping()
    public String processFindForm(Owner owner, BindingResult result, Model model) {
        if(owner.getLastName() == null) {
            owner.setLastName("");
        }

        List<Owner> results = ownerService.findAllByLastNameLike("%" + owner.getLastName() + "%");

        if(results.isEmpty()) {
            result.rejectValue("lastName", "notFound", "not found");
            return "owners/findOwners";
        } else if(results.size() == 1) {
            owner = results.get(0);
            return "redirect:/owners/" + owner.getId();
        } else {
            model.addAttribute("selections", results);
            return "owners/ownerList";
        }
    }

    //Example of ModelAndView use
    @GetMapping("/{ownerId}")
    public ModelAndView showOwner(@PathVariable Long ownerId) {

        Owner owner = ownerService.findById(ownerId);
        ModelAndView modelAndView = new ModelAndView("owners/ownerDetails");
        modelAndView.addObject(owner);
        return modelAndView;
    }

    @GetMapping("/new")
    public String initCreationForm(Model model) {
        Owner owner = Owner.builder().build();
        model.addAttribute("owner", owner);
        return VIEWS_OWNER_CREATE_UPDATE;
    }

    @PostMapping("/new")
    public String processCreationForm(Owner owner, BindingResult result){
        if(result.hasErrors()) {
            return VIEWS_OWNER_CREATE_UPDATE;
        }
        else {
            Owner savedOwner = ownerService.save(owner);
            return "redirect:/owners/" + savedOwner.getId();
        }
    }

    @GetMapping("/{ownerId}/edit")
    public String initUpdateForm(@PathVariable Long ownerId, Model model) {
        Owner owner = ownerService.findById(ownerId);
        model.addAttribute("owner", owner);
        return VIEWS_OWNER_CREATE_UPDATE;
    }

    @PostMapping("/{ownerId}/edit")
    public String processUpdateOwnerForm(Owner owner, BindingResult result, @PathVariable Long ownerId) {
        if(result.hasErrors()) {
            return VIEWS_OWNER_CREATE_UPDATE;
        }
        else {
            owner.setId(ownerId);
            Owner savedOwner = ownerService.save(owner);
            return "redirect:/owners/" + savedOwner.getId();
        }
    }

}
