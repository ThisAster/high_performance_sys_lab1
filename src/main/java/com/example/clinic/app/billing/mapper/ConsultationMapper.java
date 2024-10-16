package com.example.clinic.app.billing.mapper;

import com.example.clinic.app.billing.dto.ConsultationDTO;
import com.example.clinic.app.appointment.entity.Appointment;
import com.example.clinic.app.doctor.mapper.DoctorMapper;
import com.example.clinic.app.patient.mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ConsultationMapper {

    private final DoctorMapper doctorMapper;
    private final PatientMapper patientMapper;


    public ConsultationDTO appointmentToConsultationDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        ConsultationDTO consultationDTO = new ConsultationDTO();

        consultationDTO.setPrice(appointment.getAppointmentType().getPrice());
        consultationDTO.setDoctor(doctorMapper.entityToDoctorDto(appointment.getAppointmentType().getDoctor()));
        consultationDTO.setPatient(patientMapper.entityToPatientDto(appointment.getPatient()));

        return consultationDTO;
    }


    public List<ConsultationDTO> appointmentToConsultationDTO(List<Appointment> appointments) {
        if (appointments == null) {
            return null;
        }
        return appointments.stream().map(this::appointmentToConsultationDTO).collect(Collectors.toList());
    }
}
