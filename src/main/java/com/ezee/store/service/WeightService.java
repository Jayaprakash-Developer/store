package com.ezee.store.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ezee.store.dto.WeightDTO;
import com.ezee.store.exception.ErrorCode;
import com.ezee.store.exception.ServiceException;
import com.ezee.store.repository.WeightRepository;

@Service
public class WeightService {
	@Autowired
	private WeightRepository weightRepository;

	public void saveWeight(WeightDTO weightDto) {
		weightRepository.addWeight(weightDto);
	}

	@Cacheable(value = { "localWeight" }, key = "#id", unless = "#result == null")
	public WeightDTO getWeightById(int id) {
		return weightRepository.fetchById(id);
	}

	public List<WeightDTO> getAllWeight() {
		return weightRepository.fetchAllWeight();
	}

	@CachePut(value = "localWeight", key = "#id")
	public WeightDTO updateWeight(int id, Map<String, Object> weight) {
		WeightDTO weightDto = weightRepository.fetchById(id);
		if (weight != null) {
			weight.forEach((key, value) -> {
				switch (key) {
				case "weightValue":
					weightDto.setWeightValue((double) value);
					break;
				case "weightUnit":
					weightDto.setWeightUnit((String) value);
					break;
				default:
					throw new ServiceException(ErrorCode.KEY_NOT_FOUND_EXCEPTION);
				}
			});
			int updatedRows = weightRepository.update(weightDto);
			if (updatedRows == 0) {
				throw new ServiceException(ErrorCode.UPDATE_FAILED_EXCEPTION);
			}
		} else {
			throw new ServiceException(ErrorCode.ID_NOT_FOUND_EXCEPTION);
		}
		return weightDto;
	}

	@CacheEvict(value = "localWeight", key = "#id")
	public int deleteWeight(int id) {
		return weightRepository.delete(id);
	}

}
