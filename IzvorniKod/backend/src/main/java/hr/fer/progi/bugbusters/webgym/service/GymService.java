package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.*;
import hr.fer.progi.bugbusters.webgym.mappers.Mappers;
import hr.fer.progi.bugbusters.webgym.model.*;
import hr.fer.progi.bugbusters.webgym.model.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("gymService")
public class GymService {
    private final UserRepository userRepository;
    private final GymRepository gymRepository;
    private final GymLocationRepository gymLocationRepository;
    private final GymUserRepository gymUserRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipUserRepository membershipUserRepository;
    private final JobRequestRepository jobRequestRepository;
    ModelMapper modelMapper;

    @Autowired
    public GymService(@Qualifier("userRep") UserRepository userRepository,
                      @Qualifier("gymRep") GymRepository gymRepository,
                      @Qualifier("gymLocationRep") GymLocationRepository gymLocationRepository,
                      @Qualifier("gymUserRep") GymUserRepository gymUserRepository,
                      @Qualifier("membershipRep") MembershipRepository membershipRepository,
                      @Qualifier("membershipUserRep") MembershipUserRepository membershipUserRepository,
                      @Qualifier("jobRequestRep") JobRequestRepository jobRequestRepository,
                      ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.gymRepository = gymRepository;
        this.gymLocationRepository = gymLocationRepository;
        this.gymUserRepository = gymUserRepository;
        this.membershipRepository = membershipRepository;
        this.membershipUserRepository = membershipUserRepository;
        this.jobRequestRepository = jobRequestRepository;
        this.modelMapper = modelMapper;
    }

    public List<Gym> listGyms() {
        Iterable<Gym> it = gymRepository.findAll();
        List<Gym> gyms = new ArrayList<>();

        for (Gym gym : it) {
            gyms.add(gym);
        }
        return gyms;
    }

    public boolean addGym(String username, Gym gym) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) return false;
        User user = optionalUser.get();
        if (user.getRole() != Role.OWNER) return false;

        GymUser gymUser = new GymUser();
        gymUser.setGym(gym);
        gymUser.setUser(user);
        gymUser.setWorkDateBegin(Date.from(Instant.now()));

        gymRepository.save(gym);
        gymUserRepository.save(gymUser);
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
            List<GymUser> gymUserList = gym.getGymUsers();
            if (gymUserList != null) {
                for (GymUser gymUser : gymUserList) {
                    User user = gymUser.getUser();
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
        gymInfoDto.setLocations(gymLocationDtoList);
        gymInfoDto.setMemberships(membershipDtoList);
        gymInfoDto.setCoaches(coachDtoList);

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

    public List<GymDto> getMyGyms(String username) {
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        if (user.getRole() != Role.COACH && user.getRole() != Role.OWNER) return null;

        List<GymDto> gymDtoList = new ArrayList<>();

        List<GymUser> gymUserList = gymUserRepository.findByUser(user);
        for (GymUser gymUser: gymUserList) {
            Gym gym = gymUser.getGym();
            gymDtoList.add(modelMapper.map(gym, GymDto.class));
        }

        return gymDtoList;
    }

    public void deleteMyGym(long id, String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");
        User user = optionalUser.get();
        if (user.getRole() != Role.OWNER) throw new IllegalArgumentException("403");

        Optional<Gym> optionalGym = gymRepository.findById(id);
        if (optionalGym.isEmpty()) throw new IllegalArgumentException("404");
        Gym gym = optionalGym.get();

        boolean ownsGym = false;
        for (GymUser gymUser: gymUserRepository.findByGym(gym)) {
            if (gymUser.getUser().getUsername().equals(username)) ownsGym = true;
        }
        if (!ownsGym) throw new IllegalArgumentException("403");

        List<GymUser> gymUserList = gym.getGymUsers();
        List<GymLocation> gymLocationList = gym.getGymLocations();
        List<Membership> membershipList = gym.getMemberships();
        List<JobRequest> jobRequestList = gym.getJobRequests();

        deleteFromRepo(gymUserList, gymUserRepository);
        deleteFromRepo(gymLocationList, gymLocationRepository);
        for (Membership membership: membershipList) {
            List<MembershipUser> membershipUserList = membership.getMembershipUserList();
            deleteFromRepo(membershipUserList, membershipUserRepository);
            membershipRepository.delete(membership);
        }
        deleteFromRepo(jobRequestList, jobRequestRepository);

        gymRepository.delete(gym);
    }

    public void deleteGymLocation(long id, String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");
        User user = optionalUser.get();
        if (user.getRole() != Role.OWNER) throw new IllegalArgumentException("403");

        Optional<GymLocation> optionalGymLocation = gymLocationRepository.findById(id);
        if (optionalGymLocation.isEmpty()) throw new IllegalArgumentException("404");
        GymLocation gymLocation = optionalGymLocation.get();

        Gym gym = gymLocation.getGym();
        boolean ownsGym = false;
        for (GymUser gymUser: gym.getGymUsers()) {
            if (gymUser.getUser().getUsername().equals(username)) ownsGym = true;
        }
        if (!ownsGym) throw new IllegalArgumentException("403");

        gymLocationRepository.delete(gymLocation);
    }

    public void updateGymLocation(GymLocationDto gymLocationDto, String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");
        User user = optionalUser.get();
        if (user.getRole() != Role.OWNER) throw new IllegalArgumentException("403");

        Optional<GymLocation> optionalGymLocation = gymLocationRepository.findById(gymLocationDto.getId());
        if (optionalGymLocation.isEmpty()) throw new IllegalArgumentException("404");
        GymLocation gymLocation = optionalGymLocation.get();

        Gym gym = gymLocation.getGym();
        boolean ownsGym = false;
        for (GymUser gymUser: gym.getGymUsers()) {
            if (gymUser.getUser().getUsername().equals(username)) ownsGym = true;
        }
        if (!ownsGym) throw new IllegalArgumentException("403");

        gymLocation = mapToGymLocation(gymLocation, gymLocationDto);
        gymLocationRepository.save(gymLocation);
    }

    public List<JobRequestDto> getAllJobRequests(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");
        User user = optionalUser.get();
        if (user.getRole() != Role.OWNER) throw new IllegalArgumentException("403");

        List<JobRequestDto> jobRequestDtoList = new ArrayList<>();

        List<GymUser> gymUserList = user.getGymUserList();
        for (GymUser gymUser: gymUserList) {
            List<JobRequest> jobRequestList = gymUser.getGym().getJobRequests();
            for (JobRequest jobRequest: jobRequestList) {
                if (jobRequest.getState() != JobRequestState.IN_REVIEW) continue;
                jobRequestDtoList.add(Mappers.mapJobRequestToDto(jobRequest, gymUser.getGym(), modelMapper.map(jobRequest.getUser(), CoachDto.class)));
            }
        }

        return jobRequestDtoList;
    }

    public void responseForJobRequest(JobResponseDto jobResponseDto, String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");
        User user = optionalUser.get();
        if (user.getRole() != Role.OWNER) throw new IllegalArgumentException("403");

        Optional<JobRequest> optionalJobRequest = jobRequestRepository.findById(jobResponseDto.getReqId());
        if (optionalJobRequest.isEmpty()) throw new IllegalArgumentException("404");
        JobRequest jobRequest = optionalJobRequest.get();

        boolean ownsGym = false;
        for (GymUser gymUser: jobRequest.getGym().getGymUsers()) {
            if (gymUser.getUser().getUsername().equals(username)) ownsGym = true;
        }
        if (!ownsGym) throw new IllegalArgumentException("403");

        if (jobResponseDto.getResponse()) jobRequest.setState(JobRequestState.APPROVED);
        else jobRequest.setState(JobRequestState.DENIED);

        jobRequestRepository.save(jobRequest);

        GymUser gymUser = new GymUser();
        gymUser.setGym(jobRequest.getGym());
        gymUser.setUser(jobRequest.getUser());
        gymUser.setWorkDateBegin(Date.valueOf(LocalDate.now()));
        gymUserRepository.save(gymUser);
    }

    public void addGymOwner(AddGymOwnerDto addGymOwnerDto, String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");
        User user = optionalUser.get();
        if (user.getRole() != Role.OWNER) throw new IllegalArgumentException("403");

        Optional<User> optionalNewOwner = userRepository.findById(addGymOwnerDto.getUsername());
        if (optionalNewOwner.isEmpty()) throw new IllegalArgumentException("404");
        User newOwner = optionalNewOwner.get();

        Optional<Gym> optionalGym = gymRepository.findById(addGymOwnerDto.getGymId());
        if (optionalGym.isEmpty()) throw new IllegalArgumentException("404");
        Gym gym = optionalGym.get();

        boolean ownsGym = false;
        for (GymUser gymUser: gym.getGymUsers()) {
            if (gymUser.getUser().getUsername().equals(username)) ownsGym = true;
        }
        if (!ownsGym) throw new IllegalArgumentException("403");

        GymUser gymUser = new GymUser();
        gymUser.setWorkDateBegin(java.util.Date.from(Instant.now()));
        gymUser.setUser(newOwner);
        gymUser.setGym(gym);

        gymUserRepository.save(gymUser);
    }

    private static GymLocation mapToGymLocation(GymLocation gymLocation, GymLocationDto gymLocationDto) {
        if (gymLocationDto.getCity() != null) gymLocation.setCity(gymLocationDto.getCity());
        if (gymLocationDto.getClosesAt() != null) gymLocation.setClosesAt(Time.valueOf(gymLocationDto.getClosesAt()));
        if (gymLocationDto.getCountry() != null) gymLocation.setCountry(gymLocationDto.getCountry());
        if (gymLocationDto.getOpensAt() != null) gymLocation.setOpensAt(Time.valueOf(gymLocationDto.getOpensAt()));
        if (gymLocationDto.getPhoneNumber() != null) gymLocation.setPhoneNumber(gymLocationDto.getPhoneNumber());
        if (gymLocationDto.getStreet() != null) gymLocation.setStreet(gymLocationDto.getStreet());

        return gymLocation;
    }

    private static <T, S extends JpaRepository<T, Long>> void deleteFromRepo(List<T> deleteList, S repo) {
        for (T element: deleteList) {
            repo.delete(element);
        }
    }
}
