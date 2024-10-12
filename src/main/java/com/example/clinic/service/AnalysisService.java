/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.clinic.service;

import com.example.clinic.entity.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinic.dto.AnalysisDto;
import com.example.clinic.entity.Analysis;
import com.example.clinic.entity.Patient;
import com.example.clinic.exception.EntityNotFoundException;
import com.example.clinic.mapper.AnalysisMapper;
import com.example.clinic.repository.AnalysisRepository;
import com.example.clinic.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final PatientRepository patientRepository;
    private final AnalysisMapper analysisMapper;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Analysis createAnalysis(AnalysisDto analysisDto, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + patientId + " not found"));

        Analysis analysis = analysisMapper.analysisDtoToEntity(analysisDto);

        analysis.setPatient(patient);

        return analysisRepository.save(analysis);
    }   

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Analysis updateAnalysis(Long id, AnalysisDto analysisDto) {
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