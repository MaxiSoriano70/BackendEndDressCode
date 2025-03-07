package com.grupo5.DressCode.dto;
import com.grupo5.DressCode.utils.ERol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id; // Campo añadido
    private String firstName;
    private String lastName;
    private String email;
    private ERol role; // Campo añadido para el rol
}
