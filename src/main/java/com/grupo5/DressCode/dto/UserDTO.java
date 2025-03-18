package com.grupo5.DressCode.dto;
import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.utils.ERol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private ERol role;
    private Set<ClotheDTO> favoriteClothes;
}
