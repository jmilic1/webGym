package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.*;
import hr.fer.progi.bugbusters.webgym.model.*;
import hr.fer.progi.bugbusters.webgym.model.dto.GymInfoDto;
import hr.fer.progi.bugbusters.webgym.model.dto.GymLocationDto;
import hr.fer.progi.bugbusters.webgym.model.dto.MembershipDto;
import hr.fer.progi.bugbusters.webgym.model.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("gymService")
public class GymService {
    private final GymRepository gymRepository;
    private final GymLocationRepository gymLocationRepository;
    private final MembershipRepository membershipRepository;
    ModelMapper modelMapper;

    @Autowired
    public GymService(@Qualifier("gymRep") GymRepository gymRepository, @Qualifier("gymLocationRep") GymLocationRepository gymLocationRepository, @Qualifier("membershipRep") MembershipRepository membershipRepository, ModelMapper modelMapper) {
        this.gymRepository = gymRepository;
        this.gymLocationRepository = gymLocationRepository;
        this.modelMapper = modelMapper;
        this.membershipRepository = membershipRepository;
    }

    public List<Gym> listGyms() {
        Iterable<Gym> it = gymRepository.findAll();
        List<Gym> gyms = new ArrayList<>();

        for (Gym gym : it) {
            gyms.add(gym);
        }
        return gyms;
    }

    public boolean addGym(Gym gym) {
        gymRepository.save(gym);
        return true;
    }

    public GymLocation getGymLocation(Long id) {
        Optional<GymLocation> opGymLoc = gymLocationRepository.findById(id);
        if (opGymLoc.isPresent()) {
            return opGymLoc.get();
        }

        return null;
    }

    public GymInfoDto getGymInfo(Long id) {
        Optional<Gym> optionalGym = gymRepository.findById(id);

        if (optionalGym.isEmpty()) {
            return null;
        }
        Gym gym = optionalGym.get();

        List<GymLocation> gymLocations = gym.getGymLocations();
        List<GymLocationDto> gymLocationDtoList = gymLocations.stream()
                .map(x -> modelMapper.map(x, GymLocationDto.class))
                .collect(Collectors.toList());

        List<Membership> memberships = gym.getMemberships();
        List<MembershipDto> membershipDtoList = memberships.stream()
                .map(x -> modelMapper.map(x, MembershipDto.class))
                .collect(Collectors.toList());

        List<User> coaches = new ArrayList<>();
        //if (gymLocations != null) {
        for (GymLocation gymLocation : gymLocations) {
            List<UserGym> userGymList = gymLocation.getUserGymList();
            if (userGymList != null) {
                for (UserGym userGym : userGymList) {
                    User user = userGym.getUser();
                    if (user != null) {
                        coaches.add(user);
                    }
                }
            }
        }
        //}
        List<UserDto> coachDtoList = coaches.stream()
                .map(x -> modelMapper.map(x, UserDto.class))
                .collect(Collectors.toList());

        GymInfoDto gymInfoDto = modelMapper.map(gym, GymInfoDto.class);
        gymInfoDto.setGymLocationList(gymLocationDtoList);
        gymInfoDto.setMembershipList(membershipDtoList);
        gymInfoDto.setCoachList(coachDtoList);

        return gymInfoDto;
    }

    public void createGymLocation(GymLocation gymLocation){
        gymLocationRepository.save(gymLocation);
    }

    public Membership getMembership(Long id){
        return membershipRepository.findById(id).get();
    }

    public void createMembership(Membership membership){
        membershipRepository.save(membership);
    }
}
