package com.ime.api.depense.service;

import com.ime.api.depense.dto.DepenseDto;
import java.util.List;

public interface DepenseService {
    DepenseDto createDepense(DepenseDto depenseDto);
    DepenseDto getDepenseById(Long id);
    List<DepenseDto> getAllDepenses();
    DepenseDto updateDepense(Long id, DepenseDto depenseDto);
    void deleteDepense(Long id);
}