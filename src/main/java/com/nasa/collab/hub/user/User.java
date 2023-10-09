package com.nasa.collab.hub.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nasa.collab.hub.preference.Preference;
import com.nasa.collab.hub.preference.PreferenceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String email;

    private String preferences;

    @Transient
    public Map<PreferenceType, List<Preference>> getUserPreferences() throws JsonProcessingException {
        if(preferences != null) {
            return new ObjectMapper().readValue(this.preferences, new TypeReference<Map<PreferenceType, List<Preference>>>(){});
        }
        return Collections.EMPTY_MAP;
    }
}
