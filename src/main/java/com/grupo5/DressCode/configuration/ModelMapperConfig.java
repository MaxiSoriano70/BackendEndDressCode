package com.grupo5.DressCode.configuration;

import com.grupo5.DressCode.dto.ReservationItemDTO;
import com.grupo5.DressCode.entity.ReservationItem;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configuración para mapear correctamente ReservationItem a ReservationItemDTO
        modelMapper.addMappings(new PropertyMap<ReservationItem, ReservationItemDTO>() {
            @Override
            protected void configure() {
                map().setParentReservationId(source.getReservation().getReservationId());
            }
        });

        return modelMapper;
    }
}
