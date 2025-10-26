package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
import com.vdtry06.partner_management.source.server.dto.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.dto.shift.ShiftRequest;
import com.vdtry06.partner_management.source.server.dto.task.TaskRequest;
import com.vdtry06.partner_management.source.server.services.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/partner-manager")
@RequiredArgsConstructor
public class PartnerManagerController {

    private final PartnerService partnerService;
    private final ContractService contractService;
    private final TaskService taskService;
    private final TaskContractService taskContractService;
    private final ShiftService shiftService;

    // ========== HOME & PARTNER MANAGEMENT ==========

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("employeeId") == null) {
            return "redirect:/auth/login";
        }

        EmployeePostion position = (EmployeePostion) session.getAttribute("position");
        if (position != EmployeePostion.PARTNER_MANAGER) {
            return "redirect:/auth/login";
        }

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("username", session.getAttribute("username"));

        return "partner-manager/home";
    }

    @GetMapping("/partners")
    public String listPartners(HttpSession session, Model model) {
        if (session.getAttribute("employeeId") == null) {
            return "redirect:/auth/login";
        }

        EmployeePostion position = (EmployeePostion) session.getAttribute("position");
        if (position != EmployeePostion.PARTNER_MANAGER) {
            return "redirect:/auth/login";
        }

        Integer employeeId = (Integer) session.getAttribute("employeeId");

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("partners", partnerService.getPartnersByManagerIdResponse(employeeId));

        return "partner-manager/partner-list";
    }

    @GetMapping("/partners/add")
    public String showAddPartnerForm(HttpSession session, Model model) {
        if (session.getAttribute("employeeId") == null) {
            return "redirect:/auth/login";
        }

        EmployeePostion position = (EmployeePostion) session.getAttribute("position");
        if (position != EmployeePostion.PARTNER_MANAGER) {
            return "redirect:/auth/login";
        }

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("partnerRequest", new PartnerRequest());

        return "partner-manager/add-partner";
    }

    @PostMapping("/partners/add")
    public String addPartner(@Valid @ModelAttribute PartnerRequest partnerRequest,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (session.getAttribute("employeeId") == null) {
            return "redirect:/auth/login";
        }

        EmployeePostion position = (EmployeePostion) session.getAttribute("position");
        if (position != EmployeePostion.PARTNER_MANAGER) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("fullname", session.getAttribute("fullname"));
            return "partner-manager/add-partner";
        }

        try {
            Integer employeeId = (Integer) session.getAttribute("employeeId");
            partnerService.createPartner(partnerRequest, employeeId);

            redirectAttributes.addFlashAttribute("successMessage", "Thêm đối tác thành công!");
            return "redirect:/partner-manager/partners";
        } catch (Exception e) {
            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            return "partner-manager/add-partner";
        }
    }

    // ========== CONTRACT MANAGEMENT ==========

    @GetMapping("/contracts/search-partner")
    public String searchPartnerPage(HttpSession session, Model model) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("fullname", session.getAttribute("fullname"));
        return "partner-manager/search-partner";
    }

    @PostMapping("/contracts/search-partner")
    public String searchPartner(@RequestParam("keyword") String keyword,
                                HttpSession session,
                                Model model) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        Integer employeeId = (Integer) session.getAttribute("employeeId");

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("keyword", keyword);
        model.addAttribute("partners", partnerService.searchPartnersByKeyword(keyword, employeeId));

        return "partner-manager/search-partner";
    }

    @GetMapping("/contracts/select-partner/{partnerId}")
    public String selectPartner(@PathVariable Integer partnerId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        try {
            Integer employeeId = (Integer) session.getAttribute("employeeId");

            // Tạo contract draft
            Integer contractId = contractService.createDraftContract(partnerId, employeeId);

            // Lưu contractId vào session
            session.setAttribute("currentContractId", contractId);

            return "redirect:/partner-manager/contracts/tasks";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/partner-manager/contracts/search-partner";
        }
    }

    // ========== TASK MANAGEMENT ==========

    @GetMapping("/contracts/tasks")
    public String listTasks(HttpSession session, Model model) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) {
            return "redirect:/partner-manager/contracts/search-partner";
        }

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("contractId", contractId);
        model.addAttribute("selectedTaskContracts", taskContractService.getTaskContractsByContractId(contractId));

        return "partner-manager/list-tasks";
    }

    @GetMapping("/contracts/tasks/add-new")
    public String showAddTaskForm(HttpSession session, Model model) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) {
            return "redirect:/partner-manager/contracts/search-partner";
        }

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("taskRequest", new TaskRequest());
        model.addAttribute("contractId", contractId);

        return "partner-manager/add-task";
    }

    @PostMapping("/contracts/tasks/add-new")
    public String addNewTask(@Valid @ModelAttribute TaskRequest taskRequest,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) {
            return "redirect:/partner-manager/contracts/search-partner";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("contractId", contractId);
            return "partner-manager/add-task";
        }

        try {
            taskService.createTask(taskRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm đầu việc mới thành công!");
            return "redirect:/partner-manager/contracts/tasks";
        } catch (Exception e) {
            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("contractId", contractId);
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            return "partner-manager/add-task";
        }
    }

    @PostMapping("/contracts/tasks/select/{taskId}")
    public String selectTask(@PathVariable Integer taskId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        try {
            Integer contractId = (Integer) session.getAttribute("currentContractId");
            if (contractId == null) {
                return "redirect:/partner-manager/contracts/search-partner";
            }

            Integer taskContractId = taskContractService.createTaskContract(contractId, taskId);

            // Chuyển sang trang thêm shift
            return "redirect:/partner-manager/contracts/task-contracts/" + taskContractId + "/add-shift";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/partner-manager/contracts/tasks";
        }
    }

    // ========== SHIFT MANAGEMENT ==========

    @GetMapping("/contracts/task-contracts/{taskContractId}/add-shift")
    public String showAddShiftForm(@PathVariable Integer taskContractId,
                                   HttpSession session,
                                   Model model) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) {
            return "redirect:/partner-manager/contracts/search-partner";
        }

        try {
            var taskContract = taskContractService.getTaskContractById(taskContractId);
            var shifts = shiftService.getShiftsByTaskContractId(taskContractId);

            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("shiftRequest", new ShiftRequest());
            model.addAttribute("taskContractId", taskContractId);
            model.addAttribute("taskName", taskContract.getTaskId().getNameTask());
            model.addAttribute("shifts", shifts);

            return "partner-manager/add-shift";
        } catch (Exception e) {
            return "redirect:/partner-manager/contracts/tasks";
        }
    }

    @PostMapping("/contracts/task-contracts/{taskContractId}/add-shift")
    public String addShift(@PathVariable Integer taskContractId,
                           @Valid @ModelAttribute ShiftRequest shiftRequest,
                           BindingResult bindingResult,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            var taskContract = taskContractService.getTaskContractById(taskContractId);
            var shifts = shiftService.getShiftsByTaskContractId(taskContractId);

            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("taskContractId", taskContractId);
            model.addAttribute("taskName", taskContract.getTaskId().getNameTask());
            model.addAttribute("shifts", shifts);
            return "partner-manager/add-shift";
        }

        try {
            shiftService.createShift(shiftRequest, taskContractId);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm ca làm việc thành công!");
            return "redirect:/partner-manager/contracts/task-contracts/" + taskContractId + "/add-shift";
        } catch (Exception e) {
            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());

            var taskContract = taskContractService.getTaskContractById(taskContractId);
            var shifts = shiftService.getShiftsByTaskContractId(taskContractId);

            model.addAttribute("taskContractId", taskContractId);
            model.addAttribute("taskName", taskContract.getTaskId().getNameTask());
            model.addAttribute("shifts", shifts);
            return "partner-manager/add-shift";
        }
    }

    @GetMapping("/contracts/task-contracts/{taskContractId}/shifts/{shiftId}/edit")
    public String showEditShiftForm(@PathVariable Integer taskContractId,
                                    @PathVariable Integer shiftId,
                                    HttpSession session,
                                    Model model) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        try {
            var shift = shiftService.getShiftById(shiftId);
            var taskContract = taskContractService.getTaskContractById(taskContractId);

            ShiftRequest shiftRequest = ShiftRequest.builder()
                    .workDate(shift.getWorkDate())
                    .startTime(shift.getStartTime())
                    .endTime(shift.getEndTime())
                    .workerCount(shift.getWorkerCount())
                    .shiftUnitPrice(shift.getShiftUnitPrice())
                    .description(shift.getDescription())
                    .build();

            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("shiftRequest", shiftRequest);
            model.addAttribute("shiftId", shiftId);
            model.addAttribute("taskContractId", taskContractId);
            model.addAttribute("taskName", taskContract.getTaskId().getNameTask());

            return "partner-manager/edit-shift";
        } catch (Exception e) {
            return "redirect:/partner-manager/contracts/task-contracts/" + taskContractId + "/add-shift";
        }
    }

    @PostMapping("/contracts/task-contracts/{taskContractId}/shifts/{shiftId}/edit")
    public String editShift(@PathVariable Integer taskContractId,
                            @PathVariable Integer shiftId,
                            @Valid @ModelAttribute ShiftRequest shiftRequest,
                            BindingResult bindingResult,
                            HttpSession session,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            var taskContract = taskContractService.getTaskContractById(taskContractId);

            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("shiftId", shiftId);
            model.addAttribute("taskContractId", taskContractId);
            model.addAttribute("taskName", taskContract.getTaskId().getNameTask());
            return "partner-manager/edit-shift";
        }

        try {
            shiftService.updateShift(shiftId, shiftRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật ca làm việc thành công!");
            return "redirect:/partner-manager/contracts/task-contracts/" + taskContractId + "/add-shift";
        } catch (Exception e) {
            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());

            var taskContract = taskContractService.getTaskContractById(taskContractId);
            model.addAttribute("shiftId", shiftId);
            model.addAttribute("taskContractId", taskContractId);
            model.addAttribute("taskName", taskContract.getTaskId().getNameTask());
            return "partner-manager/edit-shift";
        }
    }

    @PostMapping("/contracts/task-contracts/{taskContractId}/shifts/{shiftId}/delete")
    public String deleteShift(@PathVariable Integer taskContractId,
                              @PathVariable Integer shiftId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        try {
            shiftService.deleteShift(shiftId);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa ca làm việc thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/partner-manager/contracts/task-contracts/" + taskContractId + "/add-shift";
    }

    @GetMapping("/contracts/task-contracts/{taskContractId}/complete")
    public String completeTaskContract(@PathVariable Integer taskContractId,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) {
            return "redirect:/partner-manager/contracts/search-partner";
        }

        // Kiểm tra xem task contract có ít nhất 1 shift chưa
        int shiftCount = shiftService.countShiftsByTaskContract(taskContractId);
        if (shiftCount == 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Phải thêm ít nhất 1 ca làm việc!");
            return "redirect:/partner-manager/contracts/task-contracts/" + taskContractId + "/add-shift";
        }

        return "redirect:/partner-manager/contracts/tasks";
    }

    // ========== CONTRACT CONFIRMATION ==========

    @GetMapping("/contracts/confirm")
    public String showConfirmContract(HttpSession session, Model model) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) {
            return "redirect:/partner-manager/contracts/search-partner";
        }

        try {
            var contractDetail = contractService.getContractDetail(contractId);

            // Kiểm tra có task contract nào chưa
            if (contractDetail.getTaskContracts().isEmpty()) {
                return "redirect:/partner-manager/contracts/tasks";
            }

            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("contractDetail", contractDetail);

            return "partner-manager/confirm-contract";
        } catch (Exception e) {
            return "redirect:/partner-manager/contracts/tasks";
        }
    }

    @PostMapping("/contracts/finalize")
    public String finalizeContract(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) {
            return "redirect:/partner-manager/contracts/search-partner";
        }

        try {
            contractService.finalizeContract(contractId);

            // Xóa contractId khỏi session
            session.removeAttribute("currentContractId");

            redirectAttributes.addFlashAttribute("successMessage", "Lưu hợp đồng thành công!");
            return "redirect:/partner-manager/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/partner-manager/contracts/confirm";
        }
    }

    @GetMapping("/contracts/cancel")
    public String cancelContract(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!checkAuthentication(session)) {
            return "redirect:/auth/login";
        }

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId != null) {
            try {
                contractService.cancelDraftContract(contractId);
                session.removeAttribute("currentContractId");
                redirectAttributes.addFlashAttribute("successMessage", "Đã hủy hợp đồng nháp!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            }
        }

        return "redirect:/partner-manager/home";
    }

    // ========== HELPER METHOD ==========

    private boolean checkAuthentication(HttpSession session) {
        if (session.getAttribute("employeeId") == null) {
            return false;
        }

        EmployeePostion position = (EmployeePostion) session.getAttribute("position");
        return position == EmployeePostion.PARTNER_MANAGER;
    }
}