package com.example.clinic.app.patient.mapper;

import com.example.clinic.app.patient.dto.PatientCreationDTO;
import org.springframework.stereotype.Component;

import com.example.clinic.app.patient.dto.PatientDto;
import com.example.clinic.app.patient.entity.Patient;

@Component
public class PatientMapper {

    public PatientDto entityToPatientDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        return new PatientDto(
            patient.getId(),
            patient.getName(),
            patient.getDateOfBirth(),
            patient.getEmail()
        );
    }

    public Patient patientDtoToEntity(PatientCreationDTO patientDto) {
        if (patientDto == null) {
            return null;
        }
        Patient patient = new Patient();
        patient.setName(patientDto.name());
        patient.setDateOfBirth(patientDto.dateOfBirth());
        patient.setEmail(patientDto.email());
        
        return patient;
    }
}
