package com.nasa.collab.hub.user;

import com.nasa.collab.hub.preference.Preference;
import com.nasa.collab.hub.preference.PreferenceDto;
import com.nasa.collab.hub.preference.PreferenceType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserDto {

    private long id;
    private String email;
    private Map<PreferenceType, List<PreferenceDto>> preferences;
}
