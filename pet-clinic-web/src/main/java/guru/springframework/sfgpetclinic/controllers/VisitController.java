package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class VisitController {

    private final VisitService visitService;
    private final PetService petService;

    @Autowired
    public VisitController(VisitService visitService, PetService petService) {
        this.visitService = visitService;
        this.petService = petService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping(value = "/owners/*/pets/{petId}/visits/new")
    public String initNewVisitForm(@PathVariable("petId") long petId, Model model) {
        model.addAttribute("visit", new Visit());
        model.addAttribute("pet", petService.findById(petId));
        return "pets/createOrUpdateVisitForm";
    }

    @PostMapping(value = "/owners/{ownerId}/pets/{petId}/visits/new")
    public String processNewVisitForm(Visit visit, BindingResult result, @PathVariable("petId") Long petId) {
        if (result.hasErrors()) {
            return "pets/createOrUpdateVisitForm";
        } else {
            Pet pet = petService.findById(petId);
            visit.setPet(pet);
            pet.addVisit(visit);
            visitService.save(visit);
            return "redirect:/owners/{ownerId}";
        }
    }

    @GetMapping(value = "/owners/*/pets/{petId}/visits")
    public String showVisits(@PathVariable Long petId, Model model) {
        model.addAttribute("visits", this.petService.findById(petId).getVisits());
        return "visitList";
    }

}
