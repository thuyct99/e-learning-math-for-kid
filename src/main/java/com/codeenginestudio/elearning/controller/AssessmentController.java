package com.codeenginestudio.elearning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeenginestudio.elearning.dto.AssessmentDTO;
import com.codeenginestudio.elearning.service.AssessmentService;
import com.codeenginestudio.elearning.service.ClassService;
import com.codeenginestudio.elearning.service.QuestionOfAssessmentService;
import com.codeenginestudio.elearning.service.StudentInClassService;
import com.codeenginestudio.elearning.util.SecurityUtil;
import com.codeenginestudio.elearning.validation.AssessmentValidation;

@Controller
public class AssessmentController {

	@Autowired
	private AssessmentService assessmentService;

	@Autowired
	private ClassService classService;

	@Autowired
	private StudentInClassService studentInClassService;

	@Autowired
	private QuestionOfAssessmentService questionOfAssessmentService;

	AssessmentValidation assessmentValidation = new AssessmentValidation();

	// Teacher role
	@GetMapping("/teacher/assessment")
	public String showListAssessment(Model model, @RequestParam(name = "page", required = false) Integer page) {

		model.addAttribute("listClass", classService.getAllClass());
		Page<AssessmentDTO> listAssessments = assessmentService.getPageListAssessment(page);
		for (AssessmentDTO assessmentDTO : listAssessments) {
			assessmentDTO.setTotalquestion(questionOfAssessmentService
					.getListQuestionOfAssessmentByAssessment(assessmentDTO.getAssessmentid()).size());
		}
		model.addAttribute("assessmentPage", listAssessments);
		return PREFIX_TEACHER + "listAssessment";
	}

	@GetMapping("/teacher/assessment/addAssessment")
	public String addAssessment(Model model) {

		model.addAttribute("url", "/teacher/assessment/saveAddAssessment");
		model.addAttribute("listClass", classService.getAllClass());
		return PREFIX_TEACHER + "addAndEditAssessment";
	}

	@GetMapping("/teacher/assessment/deleteAssessment")
	public String deleteAssessment(@ModelAttribute("assessmentid") Long assessmentid, RedirectAttributes redirectAttributes) {
		assessmentService.deleteById(assessmentid);
		
		redirectAttributes.addFlashAttribute("messageSuccess", "Delete Assessment Successfully!!! ");
		return "redirect:/teacher/assessment";
	}

	@GetMapping("/teacher/assessment/editAssessmentStatus/{assessmentid}")
	public String editAssessmentStatus(@PathVariable(name = "assessmentid") Long assessmentid,
			RedirectAttributes redirectAttributes) {
		assessmentService.editAssessmentStatus(assessmentid);
		
		redirectAttributes.addFlashAttribute("messageSuccess", "Edit Status Successfully!!! ");
		return "redirect:/teacher/assessment";
	}

	@GetMapping("/teacher/assessment/editAssessment/{assessmentid}")
	public String editAssessment(Model model, @PathVariable(name = "assessmentid") Long assessmentid) {

		model.addAttribute("url", "/teacher/assessment/saveEditAssessment");
		model.addAttribute("listClass", classService.getAllClass());
		model.addAttribute("assessmentEdit", assessmentService.getAssessmentByAssessmentid(assessmentid));
		return PREFIX_TEACHER + "addAndEditAssessment";
	}

	@PostMapping("/teacher/assessment/saveAddAssessment")
	public String saveAddAssessment(Model model, AssessmentDTO assessmentDTO, RedirectAttributes redirectAttributes) {

		AssessmentValidation inValid = assessmentValidation.validateAddAssessment(assessmentDTO, assessmentService);

		if (inValid.getErrAssessmentName() == "" && inValid.getErrExpiredDate() == "") {
			assessmentService.saveAddAssessment(assessmentDTO);
			redirectAttributes.addFlashAttribute("messageSuccess", "Add Assessment Successfully!!! ");
			return "redirect:/teacher/assessment";
		} else {
			model.addAttribute("error", inValid);
			model.addAttribute("listClass", classService.getAllClass());
			return PREFIX_TEACHER + "addAndEditAssessment";
		}
	}

	@PostMapping("/teacher/assessment/saveEditAssessment")
	public String saveEditAssessment(Model model, AssessmentDTO assessmentDTO, RedirectAttributes redirectAttributes) {

		AssessmentValidation inValid = assessmentValidation.validateAddAssessment(assessmentDTO, assessmentService);
		if (inValid.getErrAssessmentName() == "" && inValid.getErrExpiredDate() == "") {
			assessmentService.saveEditAssessment(assessmentDTO);
			redirectAttributes.addFlashAttribute("messageSuccess", "Edit Assessment Successfully!!! ");
			return "redirect:/teacher/assessment";
		} else {
			model.addAttribute("error", inValid);
			model.addAttribute("listClass", classService.getAllClass());
			model.addAttribute("assessmentEdit",
					assessmentService.getAssessmentByAssessmentid(assessmentDTO.getAssessmentid()));
			return PREFIX_TEACHER + "addAndEditAssessment";
		}
	}

	// Student role
	@GetMapping("/student/assessment")
	public String showListAssessmentWithStudentRole(Model model) {

		Long userId = SecurityUtil.getUserPrincipal().getUserid();
		List<Long> listClassid = studentInClassService.getClassIdByStudent(userId);

		List<AssessmentDTO> listAssessments = assessmentService.getListAssessmentByUnExpired();

		for (AssessmentDTO assessmentDTO : listAssessments) {
			assessmentDTO.setTotalquestion(questionOfAssessmentService
					.getListQuestionOfAssessmentByAssessment(assessmentDTO.getAssessmentid()).size());
		}
		model.addAttribute("listClass", classService.getAllClass());
		model.addAttribute("listClassAssigned", listClassid);
		model.addAttribute("assessmentPage", listAssessments);

		return PREFIX_STUDENT + "listAssessment";
	}

	@GetMapping("/student/assessment/history")
	public String showHistoryWithStudentRole(Model model) {

		Long userId = SecurityUtil.getUserPrincipal().getUserid();
		List<Long> listClassid = studentInClassService.getClassIdByStudent(userId);

		List<AssessmentDTO> listAssessments = assessmentService.getListAssessmentByExpired();

		for (AssessmentDTO assessmentDTO : listAssessments) {
			assessmentDTO.setTotalquestion(questionOfAssessmentService
					.getListQuestionOfAssessmentByAssessment(assessmentDTO.getAssessmentid()).size());
		}
		model.addAttribute("listClass", classService.getAllClass());
		model.addAttribute("listClassAssigned", listClassid);
		model.addAttribute("assessmentPage", listAssessments);
		return PREFIX_STUDENT + "listAssessment";
	}

	private static final String PREFIX_TEACHER = "/teacher/assessment/";
	private static final String PREFIX_STUDENT = "/student/assessment/";
}
