package com.pledge.app.endpoint;

import com.pledge.app.dto.HobbyDto;
import com.pledge.app.entity.Hobby;
import com.pledge.app.service.HobbyService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/hobby")
public class HobbyEndpoint {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HobbyService hobbyService;

    @GetMapping
    public ResponseEntity<?> getAllHobbies() {
        List<Hobby> hobbyList = hobbyService.findAll();
        Type targetListType = new TypeToken<List<HobbyDto>>() {
        }.getType();
        List<HobbyDto> hobbyDtos = modelMapper.map(hobbyList, targetListType);
        return ResponseEntity.ok(hobbyDtos);
    }

    //    @RolesAllowed({"ROLE_ADMIN", "ROLE_DEVELOPER"})
    @PostMapping
    public ResponseEntity<?> createHobby(@RequestBody HobbyDto hobby) {
        Hobby hobbyToSave = modelMapper.map(hobby, Hobby.class);
        Hobby saved = hobbyService.createHobby(hobbyToSave);
        return ResponseEntity.ok(modelMapper.map(saved, HobbyDto.class));
    }

    //    @RolesAllowed({"ROLE_ADMIN", "ROLE_DEVELOPER"})
    @PutMapping
    public ResponseEntity<?> updateHobby(@RequestBody HobbyDto hobby) {
        Hobby hobbyToSave = modelMapper.map(hobby, Hobby.class);
        Hobby saved = hobbyService.updateHobby(hobbyToSave);
        return ResponseEntity.ok(modelMapper.map(saved, HobbyDto.class));
    }
}
