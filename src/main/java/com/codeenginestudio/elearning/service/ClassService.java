package com.codeenginestudio.elearning.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.codeenginestudio.elearning.dto.ClassDTO;

public interface ClassService {

	void deleteClass(Long id);

	void deleteClassByTeacherId(Long teacherId);

	void editStatusClass(Long classid);

	void saveAddClass(ClassDTO classDTO);

	void saveEditClass(ClassDTO classDTO);

	ClassDTO getClassByClassid(Long classid);

	List<Long> getListByStatus(boolean status);

	List<ClassDTO> getAllClass();

	List<ClassDTO> getClassByTeacherId(Long teacherId);

	Page<ClassDTO> getClassPage(Integer page);

	Page<ClassDTO> getClassPageByTeacherId(Integer page, Long teacherId);
}
