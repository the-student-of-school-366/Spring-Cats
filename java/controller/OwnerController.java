package controller;

import dto.OwnerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.OwnerService;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping("/{id}")
    public OwnerDto getOwner(@PathVariable Long id) {
        return ownerService.getOwnerById(id);
    }

    @GetMapping
    public List<OwnerDto> getOwners() {
        return ownerService.getAllOwners();
    }

    @PostMapping
    public OwnerDto createOwner(@RequestBody OwnerDto ownerDTO) {
        return ownerService.createOwner(ownerDTO);
    }

    @PutMapping("/{id}")
    public OwnerDto updateOwner(@PathVariable Long id, @RequestBody OwnerDto ownerDTO) {
        return ownerService.updateOwner(id, ownerDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
    }
}
