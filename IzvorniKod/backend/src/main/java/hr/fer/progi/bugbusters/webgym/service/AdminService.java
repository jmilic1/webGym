package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.PlanRepository;
import hr.fer.progi.bugbusters.webgym.dao.UserRepository;
import hr.fer.progi.bugbusters.webgym.mappers.Mappers;
import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("adminService")
public class AdminService {

    UserRepository userRepository;
    PlanRepository planRepository;
    ModelMapper modelMapper;

    @Autowired
    public AdminService(@Qualifier("userRep") UserRepository userRepository, @Qualifier("planRep") PlanRepository planRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.modelMapper = modelMapper;
    }

    public List<PlanDto> getAllPlans() {
        return planRepository.findAll().stream().map(Mappers::mapPlanToPlanDto).collect(Collectors.toList());
    }

}
