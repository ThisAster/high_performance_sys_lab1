/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.clinic.app.analysis.service;

import com.example.clinic.app.analysis.dto.AnalysisCreationDto;
import com.example.clinic.app.patient.service.PatientService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinic.app.analysis.entity.Analysis;
import com.example.clinic.app.patient.entity.Patient;
import com.example.clinic.exception.EntityNotFoundException;
import com.example.clinic.app.analysis.mapper.AnalysisMapper;
import com.example.clinic.app.analysis.repository.AnalysisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final PatientService patientService;
    private final AnalysisRepository analysisRepository;
    private final AnalysisMapper analysisMapper;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Analysis createAnalysis(AnalysisCreationDto analysisDto, Long patientId) {
        Patient patient = patientService.getPatientById(patientId);

        Analysis analysis = analysisMapper.analysisDtoToEntity(analysisDto);

        analysis.setPatient(patient);

        return analysisRepository.save(analysis);
    }   

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Analysis updateAnalysis(Long id, AnalysisCreationDto analysisDto) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Analysis with id " + id + " not found"));

        analysis.setType(analysisDto.type());
        analysis.setSampleDate(analysisDto.sampleDate());
        analysis.setResult(analysisDto.result());
        analysis.setStatus(analysisDto.status());

        return analysisRepository.save(analysis);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteAnalysis(Long id) {
        if (!analysisRepository.existsById(id)) {
            throw new EntityNotFoundException("Analysis with id " + id + " not found");
        }
        analysisRepository.deleteById(id);
    }

    public Analysis getAnalysisById(Long id) {
        return analysisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Analysis with id " + id + " not found"));
    }

    public Page<Analysis> getAnalyses(Pageable page) {
        return analysisRepository.findAll(page);
    }
}