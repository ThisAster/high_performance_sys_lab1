package com.example.clinic.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinic.dto.AppointmentDto;
import com.example.clinic.entity.Appointment;
import com.example.clinic.entity.Doctor;
import com.example.clinic.entity.Patient;
import com.example.clinic.exception.EntityNotFoundException;
import com.example.clinic.mapper.AppointmentMapper; 
import com.example.clinic.repository.AppointmentRepository;
import com.example.clinic.repository.DoctorRepository;
import com.example.clinic.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Appointment createAppointment(AppointmentDto appointmentDto) {
        Patient patient = patientRepository.findByIdWithAppointmentsAndDoctors(appointmentDto.patient_id())
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + appointmentDto.patient_id() + " not found"));
    
        Doctor doctor = patient.getAppointments().stream()
                .map(Appointment::getDoctor)
                .filter(doc -> doc.getId().equals(appointmentDto.doctor_id()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + appointmentDto.doctor_id() + " not found"));
    
        Appointment appointment = appointmentMapper.appointmentDtoToEntity(appointmentDto);
    
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        
        return appointmentRepository.save(appointment);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Appointment updateAppointment(Long id, AppointmentDto appointmentDto) {
        Appointment appointment = this.getAppointmentById(id);

        appointment.setAppointmentDate(appointmentDto.appointmentDate());
        appointment.setDescription(appointmentDto.description());
        return appointmentRepository.save(appointment);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Appointment with id " + id + " not found");
        }
        appointmentRepository.deleteById(id);
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment with id " + id + " not found"));
    } 
}
