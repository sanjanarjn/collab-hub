package com.nasa.collab.hub.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/preference")
    public ResponseEntity<UserDto> savePreferences(@RequestBody UserDto userDto) throws JsonProcessingException {
        UserDto savedUser = userService.saveUserPreferences(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


}
