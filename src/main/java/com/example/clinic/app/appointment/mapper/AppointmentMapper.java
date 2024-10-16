package com.example.clinic.app.appointment.mapper;

import com.example.clinic.app.appointment.dto.AppointmentCreationDTO;
import com.example.clinic.app.appointment.mapper.AppoinmentsTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.example.clinic.app.appointment.dto.AppointmentDto;
import com.example.clinic.app.appointment.entity.Appointment;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {

    private final AppoinmentsTypeMapper appoinmentsTypeMapper;

    public AppointmentDto entityToAppointmentDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        return new AppointmentDto(
            appointment.getId(),
            appointment.getAppointmentDate(),
            appointment.getPatient() != null ? appointment.getPatient().getId() : null,
            appoinmentsTypeMapper.entityToAppointmentTypeDTO(appointment.getAppointmentType())
        );
    }
 
    public Appointment appointmentDtoToEntity(AppointmentCreationDTO appointmentDto) {
        if (appointmentDto == null) {
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        return appointment;
    }
}
