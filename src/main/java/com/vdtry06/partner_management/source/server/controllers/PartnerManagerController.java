package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.enumerated.EmployeePosition;
import com.vdtry06.partner_management.lib.enumerated.ShiftType;
import com.vdtry06.partner_management.source.server.config.AppUrls;
import com.vdtry06.partner_management.source.server.dto.contract.ContractDetailResponse;
import com.vdtry06.partner_management.source.server.dto.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.dto.shift.ShiftRequest;
import com.vdtry06.partner_management.source.server.dto.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.dto.shift_task_contract.ShiftTaskContractResponse;
import com.vdtry06.partner_management.source.server.dto.task.TaskRequest;
import com.vdtry06.partner_management.source.server.dto.task_contract.TaskContractResponse;
import com.vdtry06.partner_management.source.server.dto.task_contract.TaskContractSelectionResponse;
import com.vdtry06.partner_management.source.server.services.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/partner-manager")
@RequiredArgsConstructor
public class PartnerManagerController {
    private final AuthService authService;
    private final PartnerService partnerService;
    private final ContractService contractService;
    private final TaskService taskService;
    private final TaskContractService taskContractService;
    private final ShiftTaskContractService shiftTaskContractService;
    private final ShiftService shiftService;
    private final AppUrls appUrls;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("username", session.getAttribute("username"));

        return appUrls.getHome().get("partner-manager");
    }

    // Partner Management
    @GetMapping("/partner-management")
    public String showPartnerManagement(HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        model.addAttribute("username", session.getAttribute("username"));

        return appUrls.getManagement().get("partner");
    }

    @GetMapping("/partner/add")
    public String showAddPartnerForm(HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("partnerRequest", new PartnerRequest());

        return appUrls.getCreate().get("create-partner");
    }

    @PostMapping("/add-partner")
    public String addPartner(@Valid @ModelAttribute PartnerRequest request, BindingResult result, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        if (result.hasErrors()) {
            model.addAttribute("username", session.getAttribute("username"));
            return appUrls.getCreate().get("partner-manager");
        }

        try {
            Integer employeeId = (Integer) session.getAttribute("employeeId");
            partnerService.createPartner(request, employeeId);

            redirectAttributes.addFlashAttribute("successMessage", "Thêm đối tác thành công");
            return "redirect:" + appUrls.getHome().get("partner-manager");
        } catch (Exception e) {
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("errorMessage", e.getMessage());
            return appUrls.getCreate().get("create-partner");
        }
    }

    // Partner manager Sign contract with partner
    @GetMapping("/search-partner")
    public String searchPartnerPage(@RequestParam(value = "namePartner", required = false) String namePartner, HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();
        model.addAttribute("username", session.getAttribute("username"));

        if (namePartner == null || namePartner.trim().isEmpty()) {
            return appUrls.getSearch().get("search-partner");
        }

        Integer employeeId = (Integer) session.getAttribute("employeeId");
        model.addAttribute("namePartner", namePartner);
        model.addAttribute("partners", partnerService.searchPartnersByNamePartner(namePartner, employeeId));

        return appUrls.getSearch().get("partner-manager");
    }

    @GetMapping("/contract/select-partner/{partnerId}")
    public String selectPartner(@PathVariable Integer partnerId, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        try {
            Integer employeeId = (Integer) session.getAttribute("employeeId");
            Integer contractId = contractService.createDraftContract(partnerId, employeeId);
            session.setAttribute("currentContractId", contractId);

            return "redirect:" + appUrls.getSelect().get("select-partner");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" +  appUrls.getSearch().get("partner-manager");
        }
    }

    @GetMapping("/task/add")
    public String showAddTaskForm(HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return  "redirect:" + appUrls.getLogin();

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) {
            return "redirect:" + appUrls.getSearch().get("partner-manager");
        }

        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("taskRequest", new TaskRequest());
        model.addAttribute("contractId", contractId);

        return appUrls.getCreate().get("create-task");
    }

    @PostMapping("/task/add")
    public String addTask(@Valid @ModelAttribute TaskRequest request, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return  "redirect:" + appUrls.getLogin();

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) {
            return "redirect:" + appUrls.getSearch().get("partner-manager");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("contractId", contractId);
            return appUrls.getCreate().get("create-task");
        }

        try {
            taskService.createTask(request);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm đầu việc mới thành công!");
            return "redirect:" + appUrls.getSelect().get("select-partner");
        } catch (Exception e) {
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("contractId", contractId);
            model.addAttribute("errorMessage", e.getMessage());
            return appUrls.getCreate().get("create-task");
        }

    }

    @GetMapping("/contract/tasks")
    public String listTasks(HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return  "redirect:" + appUrls.getLogin();

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) return "redirect:" + appUrls.getSearch().get("partner-manager");

        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("contractId", contractId);

        TaskContractSelectionResponse taskContractSelectionResponse = taskContractService.getSelectionData(contractId);

        model.addAttribute("selectedTaskContracts", taskContractSelectionResponse.getSelectedTasks());
        model.addAttribute("selectedTaskContractMap", taskContractSelectionResponse.getTaskContractMap());
        model.addAttribute("selectedShiftTaskContractMap", taskContractSelectionResponse.getShiftTaskContractMap());

        return appUrls.getList().get("list-task");
    }

    // do trong thẻ a link không thể sử dụng method delete nên phải sử dụng Get
    @GetMapping("/contract/cancel")
    public String cancelContract(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        Integer contractId = (Integer) session.getAttribute("currentContractId");

        if (contractId != null) {
            try {
                contractService.cancelDraftContract(contractId);
                session.removeAttribute("currentContractId");
                redirectAttributes.addFlashAttribute("successMessage", "Đã hủy hợp đồng nháp!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            }
        }

        return "redirect:" + appUrls.getHome().get("partner-manager");
    }

    @PostMapping("/contract/task/select/{taskId}")
    public String selectTask(@PathVariable Integer taskId, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        try {
            Integer contractId = (Integer) session.getAttribute("currentContractId");
            if (contractId == null) return "redirect:" + appUrls.getSearch().get("partner-manager");

            Integer taskContractId = taskContractService.createTaskContract(contractId, taskId);
            Integer shiftTaskContractId = shiftTaskContractService.createShiftTaskContract(taskContractId);

            return "redirect:" + appUrls.getSelect().get("select-task")
                    + taskContractId
                    + "/shift-task-contract/"
                    + shiftTaskContractId
                    + "/add-shift";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + appUrls.getSelect().get("select-partner");
        }
    }

    @GetMapping("/contract/task-contract/{taskContractId}/shift-task-contract/{shiftTaskContractId}/add-shift")
    public String showAddShiftForm(@PathVariable Integer taskContractId, @PathVariable Integer shiftTaskContractId, HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        try {
            TaskContractResponse taskContractResponse = taskContractService.getTaskContractResponseById(taskContractId);

            if (shiftTaskContractId == null || shiftTaskContractId == 0) {
                List<ShiftTaskContractResponse> list = shiftTaskContractService.getListShiftTaskContractByTaskContractId(taskContractId);
                if (list.isEmpty()) {
                    shiftTaskContractId = shiftTaskContractService.createShiftTaskContract(taskContractId);
                } else {
                    shiftTaskContractId = list.get(0).getId();
                }
            }

            List<ShiftResponse> shifts = shiftService.getShiftsByShiftTaskContractId(shiftTaskContractId);

            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("shiftRequest", new ShiftRequest());
            model.addAttribute("taskContractId", taskContractId);
            model.addAttribute("shiftTaskContractId", shiftTaskContractId);
            model.addAttribute("taskName", taskContractResponse.getTaskName());
            model.addAttribute("shifts", shifts);
            model.addAttribute("shiftTypes", ShiftType.values());
            session.setAttribute("currentShiftTaskContractId", shiftTaskContractId);

            return appUrls.getCreate().get("create-shift");
        } catch (Exception e) {
            return "redirect:" + appUrls.getList().get("list-task");
        }
    }

    @PostMapping("/contract/task-contract/{taskContractId}/shift-task-contract/{shiftTaskContractId}/add-shift")
    public String addShift(@PathVariable Integer taskContractId,
                           @PathVariable Integer shiftTaskContractId,
                           @Valid @ModelAttribute ShiftRequest shiftRequest,
                           BindingResult result,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) return "redirect:" + appUrls.getSearch().get("partner-manager");

        if (result.hasErrors()) {
            try {
                TaskContractResponse taskContractResponse = taskContractService.getTaskContractResponseById(taskContractId);
                List<ShiftResponse> shifts = shiftService.getShiftsByShiftTaskContractId(shiftTaskContractId);
                model.addAttribute("username", session.getAttribute("username"));
                model.addAttribute("taskContractId", taskContractId);
                model.addAttribute("shiftTaskContractId", shiftTaskContractId);
                model.addAttribute("taskName", taskContractResponse.getTaskName());
                model.addAttribute("shifts", shifts);
                model.addAttribute("shiftTypes", ShiftType.values());
                return appUrls.getCreate().get("create-shift");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:" + appUrls.getList().get("list-task");
            }
        }

        try {
            Integer shiftTaskContractCurrentId = shiftService.createShift(shiftTaskContractId, shiftRequest);

            redirectAttributes.addFlashAttribute("successMessage", "Thêm ca làm việc thành công!");

            return "redirect:" + appUrls.getSelect().get("select-task")
                    + taskContractId
                    + "/shift-task-contract/"
                    + shiftTaskContractCurrentId
                    + "/add-shift";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + appUrls.getSelect().get("select-task")
                    + taskContractId
                    + "/shift-task-contract/"
                    + shiftTaskContractId
                    + "/add-shift";
        }
    }

    @GetMapping("/contract/task-contract/{taskContractId}/complete")
    public String completeTask(@PathVariable Integer taskContractId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER))
            return "redirect:" + appUrls.getLogin();

        boolean hasShifts = shiftTaskContractService.hasAtLeastOneShift(taskContractId);
        Integer shiftTaskContractId = (Integer) session.getAttribute("currentShiftTaskContractId");

        if (!hasShifts) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cần ít nhất 1 ca làm để hoàn thành đầu việc");
            return "redirect:" + appUrls.getSelect().get("select-task")
                    + taskContractId
                    + "/shift-task-contract/"
                    + shiftTaskContractId
                    + "/add-shift";
        }

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) return "redirect:" + appUrls.getSearch().get("partner-manager");

        try {
//            shiftTaskContractService.deleteNullShiftTaskContracts(taskContractId);

            taskContractService.updateTaskUnitPrice(taskContractId);

            redirectAttributes.addFlashAttribute("successMessage", "Đã hoàn thành thêm đầu việc!");
            return "redirect:" + appUrls.getSelect().get("select-partner");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + appUrls.getSelect().get("select-partner");
        }
    }

    @GetMapping("/contract/task-contract/{taskContractId}/cancel")
    public String cancelTaskContract(@PathVariable Integer taskContractId,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER))
            return "redirect:" + appUrls.getLogin();

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) return "redirect:" + appUrls.getSearch().get("partner-manager");

        try {
            shiftTaskContractService.deleteByTaskContractId(taskContractId);
            taskContractService.deleteTaskContract(taskContractId);

            redirectAttributes.addFlashAttribute("successMessage", "Đã hủy đầu việc và xóa toàn bộ ca làm việc!");
            return "redirect:" + appUrls.getSelect().get("select-partner");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi hủy đầu việc: " + e.getMessage());
            return "redirect:" + appUrls.getSelect().get("select-partner");
        }
    }

    @GetMapping("/contract/confirm")
    public String confirmContract(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER))
            return "redirect:" + appUrls.getLogin();

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) return "redirect:" + appUrls.getSearch().get("partner-manager");

        try {
            contractService.updateContract(contractId);
            ContractDetailResponse contractDetailResponse = contractService.getContractDetailById(contractId);

            if (contractDetailResponse.getTaskContracts().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Hợp đồng phải có ít nhất 1 đầu việc!");
                return "redirect:" + appUrls.getSelect().get("select-partner");
            }

            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("contractDetail", contractDetailResponse);

            return appUrls.getConfirm().get("confirm-contract");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:" + appUrls.getSelect().get("select-partner");
        }
    }

    @PostMapping("/contract/finalize")
    public String finalizeContract(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER))
            return "redirect:" + appUrls.getLogin();

        Integer contractId = (Integer) session.getAttribute("currentContractId");
        if (contractId == null) return "redirect:" + appUrls.getSearch().get("partner-manager");

        try {
            contractService.finalizeContract(contractId);
            shiftTaskContractService.deleteNullShiftTaskContractsByContractId(contractId);

            session.removeAttribute("currentContractId");

            redirectAttributes.addFlashAttribute("successMessage", "Lưu hợp đồng thành công!");
            return "redirect:" + appUrls.getHome().get("partner-manager");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi lưu hợp đồng: " + e.getMessage());
            return "redirect:" + appUrls.getConfirm().get("contract");
        }
    }
}