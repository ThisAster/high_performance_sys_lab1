package com.example.clinic.service;

import java.util.*;
import java.util.stream.Collectors;

import com.example.clinic.dto.InvoiceDTO;
import com.example.clinic.entity.Patient;
import com.example.clinic.exception.EntityNotFoundException;
import com.example.clinic.mapper.ConsultationMapper;
import com.example.clinic.mapper.InvoiceMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.clinic.entity.Appointment;
import com.example.clinic.repository.AppointmentRepository;
import com.example.clinic.repository.PatientRepository;
import com.example.clinic.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BillingService {

    private final PatientRepository patientRepository;
    private final InvoiceMapper invoiceMapper;



    public InvoiceDTO generateInvoice(List<Long> patientIds) {

        Set<Long> patientIdSet = new HashSet<>(patientIds);

        Set<Patient> patients = patientRepository.findByIdInWithAppointments(patientIdSet);


        patientIdSet.removeAll(patients.stream().map(Patient::getId).collect(Collectors.toSet()));

        if (!patientIdSet.isEmpty()) {
            throw new EntityNotFoundException(STR."Patients with ids \{StringUtils.join(patientIdSet, ',')} not found.");
        }

        List<Appointment> appointments = patients.stream().flatMap((patient) -> patient.getAppointments().stream()).collect(Collectors.toList());

        return invoiceMapper.appointmentListToInvoice(appointments);
    }
    
}
