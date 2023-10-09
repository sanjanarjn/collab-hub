package com.nasa.collab.hub.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    public UserDto saveUserPreferences(UserDto userDto) throws JsonProcessingException {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPreferences(objectMapper.writeValueAsString(userDto.getPreferences()));
        User savedUser = userRepository.save(user);

        userDto.setId(savedUser.getId());
        return userDto;
    }

    public User getUserById(long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            return user.get();
        }
        throw new Exception("Invalid user id");
    }
}
