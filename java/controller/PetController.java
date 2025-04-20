package controller;

import dto.PetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.PetService;

import java.util.List;

@RestController
@RequestMapping("/api/cats")
public class PetController {

    @Autowired
    private PetService catService;

    @GetMapping("/{id}")
    public PetDto getCat(@PathVariable Long id) {
        return catService.getPetById(id);
    }

    @GetMapping
    public List<PetDto> getCats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String color
    ) {
        return catService.getPets(page, size, color);
    }

    @PostMapping
    public PetDto createCat(@RequestBody PetDto catDTO) {
        return catService.createPet(catDTO);
    }

    @PutMapping("/{id}")
    public PetDto updateCat(@PathVariable Long id, @RequestBody PetDto catDTO) {
        return catService.updatePet(id, catDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCat(@PathVariable Long id) {
        catService.deletePet(id);
    }
}
