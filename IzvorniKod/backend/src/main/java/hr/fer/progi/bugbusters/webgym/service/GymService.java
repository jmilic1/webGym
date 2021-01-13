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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public List<GymDto> listGyms() {
        Iterable<Gym> it = gymRepository.findAll();
        List<Gym> gyms = new ArrayList<>();

        for (Gym gym : it) {
            gyms.add(gym);
        }
        return gyms
                .stream()
                .map(Mappers::mapGymToDto)
                .collect(Collectors.toList());
    }

    public boolean addGym(String username, GymDto dto) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();
        if (user.getRole() != Role.OWNER) return false;

        Gym gym = Mappers.mapDtoToGym(dto);

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
        return opGymLoc.orElse(null);

    }

    public GymInfoDto getGymInfo(Long id) {
        Optional<Gym> optionalGym = gymRepository.findById(id);

        if (optionalGym.isEmpty()) {
            return null;
        }
        Gym gym = optionalGym.get();

        List<GymLocation> gymLocations = gym.getGymLocations();
        List<GymLocationDto> gymLocationDtoList = gymLocations.stream()
                .map(Mappers::mapLocationToDto)
                .collect(Collectors.toList());

        List<Membership> memberships = gym.getMemberships();
        List<MembershipDto> membershipDtoList = memberships.stream()
                .map(Mappers::mapMembershipToDto)
                .collect(Collectors.toList());

        List<User> coaches = new ArrayList<>();

        List<GymUser> gymUserList = gym.getGymUsers();
        if (gymUserList != null) {
            for (GymUser gymUser : gymUserList) {
                User user = gymUser.getUser();
                if (user != null && user.getRole().equals(Role.COACH)) {
                    coaches.add(user);
                }
            }
        }

        List<UserDto> coachDtoList = coaches.stream()
                .map(Mappers::mapUserToDto)
                .collect(Collectors.toList());

        GymInfoDto gymInfoDto = modelMapper.map(gym, GymInfoDto.class);
        gymInfoDto.setLocations(gymLocationDtoList);
        gymInfoDto.setMemberships(membershipDtoList);
        gymInfoDto.setCoaches(coachDtoList);

        return gymInfoDto;
    }

    public void createGymLocation(GymLocationDto dto, String username) {

        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");

        User user = optionalUser.get();
        if (user.getRole() != Role.OWNER) throw new IllegalArgumentException("403");

        Optional<Gym> optionalGym = gymRepository.findById(dto.getId());
        if (optionalGym.isEmpty()) throw new IllegalArgumentException("400");
        Gym gym = optionalGym.get();

        boolean ownsGym = false;
        for (GymUser gymUser : gymUserRepository.findByGym(gym)) {
            if (gymUser.getUser().getUsername().equals(username)) ownsGym = true;
        }
        if (!ownsGym) throw new IllegalArgumentException("403");

        List<GymLocation> gymLocationList = gymLocationRepository.findAll();
        for (GymLocation location : gymLocationList) {
            if (location.getCity().equals(dto.getCity())
                    && location.getCountry().equals(dto.getCountry())
                    && location.getStreet().equals(dto.getStreet())) return;
        }
        Optional<GymLocation> gymLocation = gymLocationRepository.findById(dto.getId());
        if (gymLocation.isPresent()) return;

        GymLocation location = Mappers.mapDtoToLocation(dto, gym);
        gymLocationRepository.save(location);
    }

    public MembershipDto getMembership(Long id) {
        Optional<Membership> membership = membershipRepository.findById(id);
        if (membership.isEmpty()) throw new IllegalArgumentException("403");

        return Mappers.mapMembershipToDto(membership.get());
    }

    public void createMembership(MembershipDto dto) {
        Optional<Membership> membershipOptional = membershipRepository.findById(dto.getId());
        if (membershipOptional.isPresent()) return;

        Membership membership = Mappers.mapDtoToMembership(dto);
        membershipRepository.save(membership);
    }

    public List<GymDto> getMyGyms(String username) {
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isEmpty()) throw new RuntimeException("Logged in user not found!");
        User user = optionalUser.get();

        if (user.getRole() != Role.COACH && user.getRole() != Role.OWNER)
            throw new RuntimeException("Only users with roles COACH or OWNER can view their gyms!");

        List<GymDto> gymDtoList = new ArrayList<>();

        List<GymUser> gymUserList = gymUserRepository.findByUser(user);
        for (GymUser gymUser : gymUserList) {
            Gym gym = gymUser.getGym();
            gymDtoList.add(Mappers.mapGymToDto(gym));
        }

        return gymDtoList;
    }

    public void deleteMyGym(long id, String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");

        User user = optionalUser.get();
        if (user.getRole() != Role.OWNER) throw new IllegalArgumentException("403");

        Optional<Gym> optionalGym = gymRepository.findById(id);
        if (optionalGym.isEmpty()) throw new IllegalArgumentException("400");
        Gym gym = optionalGym.get();

        boolean ownsGym = false;
        for (GymUser gymUser : gymUserRepository.findByGym(gym)) {
            if (gymUser.getUser().getUsername().equals(username)) ownsGym = true;
        }
        if (!ownsGym) throw new IllegalArgumentException("403");

        List<GymUser> gymUserList = gym.getGymUsers();
        List<GymLocation> gymLocationList = gym.getGymLocations();
        List<Membership> membershipList = gym.getMemberships();
        List<JobRequest> jobRequestList = gym.getJobRequests();

        deleteFromRepo(gymUserList, gymUserRepository);
        deleteFromRepo(gymLocationList, gymLocationRepository);
        for (Membership membership : membershipList) {
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
        if (optionalGymLocation.isEmpty()) throw new IllegalArgumentException("400");
        GymLocation gymLocation = optionalGymLocation.get();

        Gym gym = gymLocation.getGym();
        boolean ownsGym = false;
        for (GymUser gymUser : gym.getGymUsers()) {
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
        for (GymUser gymUser : gym.getGymUsers()) {
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
        for (GymUser gymUser : gymUserList) {
            List<JobRequest> jobRequestList = gymUser.getGym().getJobRequests();
            for (JobRequest jobRequest : jobRequestList) {
                if (jobRequest.getState() != JobRequestState.IN_REVIEW) continue;

                jobRequestDtoList.add(Mappers.mapJobRequestToDto(jobRequest));
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
        if (optionalJobRequest.isEmpty()) throw new IllegalArgumentException("400");
        JobRequest jobRequest = optionalJobRequest.get();

        boolean ownsGym = false;
        for (GymUser gymUser : jobRequest.getGym().getGymUsers()) {
            if (gymUser.getUser().getUsername().equals(username)) ownsGym = true;
        }
        if (!ownsGym) throw new IllegalArgumentException("403");

        if (jobResponseDto.getResponse()) {
            if (jobRequest.getState().equals(JobRequestState.APPROVED)) {
                return;
            }
            jobRequest.setState(JobRequestState.APPROVED);
            GymUser gymUser = createGymUser(jobRequest);
            gymUserRepository.save(gymUser);
        } else jobRequest.setState(JobRequestState.DENIED);

        jobRequestRepository.save(jobRequest);
    }

    public void addGymOwner(AddGymOwnerDto addGymOwnerDto, String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");

        User user = optionalUser.get();
        if (user.getRole() != Role.OWNER) throw new IllegalArgumentException("403");

        Optional<User> optionalNewOwner = userRepository.findById(addGymOwnerDto.getUsername());
        if (optionalNewOwner.isEmpty()) throw new IllegalArgumentException("400");
        User newOwner = optionalNewOwner.get();

        Optional<Gym> optionalGym = gymRepository.findById(addGymOwnerDto.getGymId());
        if (optionalGym.isEmpty()) throw new IllegalArgumentException("400");
        Gym gym = optionalGym.get();

        boolean ownsGym = false;
        for (GymUser gymUser : gym.getGymUsers()) {
            if (gymUser.getUser().getUsername().equals(username)) ownsGym = true;
        }
        if (!ownsGym) throw new IllegalArgumentException("403");

        List<GymUser> gymUserList = gymUserRepository.findAll();
        for (GymUser gymUser : gymUserList) {
            if (gymUser.getUser().getUsername().equals(newOwner.getUsername())
                    && gymUser.getGym().getId().equals(gym.getId())) throw new IllegalArgumentException("403");
        }

        GymUser gymUser = new GymUser();
        gymUser.setWorkDateBegin(java.util.Date.from(Instant.now()));
        gymUser.setUser(newOwner);
        gymUser.setGym(gym);

        gymUserRepository.save(gymUser);
    }

    private static GymLocation mapToGymLocation(GymLocation gymLocation, GymLocationDto gymLocationDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        if (gymLocationDto.getCity() != null) gymLocation.setCity(gymLocationDto.getCity());
        if (gymLocationDto.getCountry() != null) gymLocation.setCountry(gymLocationDto.getCountry());
        if (gymLocationDto.getPhoneNumber() != null) gymLocation.setPhoneNumber(gymLocationDto.getPhoneNumber());
        if (gymLocationDto.getStreet() != null) gymLocation.setStreet(gymLocationDto.getStreet());

        try {
            if (gymLocationDto.getClosesAt() != null) gymLocation.setClosesAt(new Time(sdf.parse(gymLocationDto.getClosesAt()).getTime()));
            if (gymLocationDto.getOpensAt() != null) gymLocation.setOpensAt(new Time(sdf.parse(gymLocationDto.getOpensAt()).getTime()));
        } catch (ParseException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return gymLocation;
    }

    private static <T, S extends JpaRepository<T, Long>> void deleteFromRepo(List<T> deleteList, S repo) {
        for (T element : deleteList) {
            repo.delete(element);
        }
    }

    private GymUser createGymUser(JobRequest jobRequest) {
        GymUser gymUser = new GymUser();
        gymUser.setGym(jobRequest.getGym());
        gymUser.setUser(jobRequest.getUser());
        gymUser.setWorkDateBegin(Date.valueOf(LocalDate.now()));
        return gymUser;
    }
}
