package com.nikolaev.conference_request;

import com.nikolaev.conference.ConferenceService;
import com.nikolaev.conference_request.comment.ConferenceRequestComment;
import com.nikolaev.conference_request.comment.ConferenceRequestCommentDto;
import com.nikolaev.conference_request.comment.ConferenceRequestCommentRepository;
import com.nikolaev.conference_request.dto.BriefConferenceRequestDto;
import com.nikolaev.conference_request.dto.ConferenceRequestDto;
import com.nikolaev.conference_request.dto.ConferenceRequestMapper;
import com.nikolaev.conference_request.status.ConferenceRequestStatus;
import com.nikolaev.conference_request.status.ConferenceRequestStatusName;
import com.nikolaev.conference_request.status.ConferenceRequestStatusRepository;
import com.nikolaev.user.User;
import com.nikolaev.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConferenceRequestServiceImpl implements ConferenceRequestService {

    private final ConferenceRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ConferenceService conferenceService;
    private final ConferenceRequestStatusRepository conferenceRequestStatusRepository;
    private final ConferenceRequestCommentRepository commentRepository;

    @Override
    public void createRequest(ConferenceRequestDto requestDTO) {
        User user = userRepository.findByUsername(requestDTO.getOrganizer().getUsername());
        ConferenceRequestStatus status = conferenceRequestStatusRepository.findByName(ConferenceRequestStatusName.PENDING);

        ConferenceRequest request = new ConferenceRequest();
        request.setTitle(requestDTO.getTitle());
        request.setAcronym(requestDTO.getAcronym());
        request.setWebPage(requestDTO.getWebPage());
        request.setOrganizer(user);
        request.setCity(requestDTO.getCity());
        request.setCountry(requestDTO.getCountry());
        request.setStatus(status);
        request.setExpirationDate(requestDTO.getExpirationDate());
        requestRepository.save(request);
    }

    @Override
    public Page<BriefConferenceRequestDto> getAll(Integer statusNumber, Pageable pageable) {
        if (statusNumber == null) {
            return requestRepository.findAll(pageable).map(ConferenceRequestMapper::toBriefDto);
        } else {
            ConferenceRequestStatus status
                    = conferenceRequestStatusRepository.findByName(ConferenceRequestStatusName.fromInt(statusNumber));
            return requestRepository.findAllByStatus(status, pageable).map(ConferenceRequestMapper::toBriefDto);
        }
    }

    @Override
    public void update(ConferenceRequestDto requestDto) {
        User organizer = userRepository.findByUsername(requestDto.getOrganizer().getUsername());
        ConferenceRequestStatus status = conferenceRequestStatusRepository.findByName(ConferenceRequestStatusName.PENDING);
        ConferenceRequest request = requestRepository.getOne(requestDto.getId());
        request.setStatus(status);
        request.setTitle(requestDto.getTitle());
        request.setAcronym(requestDto.getAcronym());
        request.setWebPage(requestDto.getWebPage());
        request.setOrganizer(organizer);
        request.setCity(requestDto.getCity());
        request.setCountry(requestDto.getCountry());
        request.setExpirationDate(requestDto.getExpirationDate());
//        ConferenceRequestComment comment = new ConferenceRequestComment();
//        comment.setContent();
//        comment.setDate();
//        comment.setRequest();

        requestRepository.save(request);
    }

    @Override
    public ConferenceRequestDto getRequest(Long requestId) {
        return ConferenceRequestMapper.toDto(requestRepository.getOne(requestId));
    }

    @Override
    public ConferenceRequestDto createComment(Long requestId, ConferenceRequestCommentDto commentDto) {
        ConferenceRequest request = requestRepository.getOne(requestId);
        ConferenceRequestStatus requestStatus = conferenceRequestStatusRepository.findByName(ConferenceRequestStatusName.fromInt(commentDto.getStatus()));

        ConferenceRequestComment comment = new ConferenceRequestComment();
        comment.setRequest(request);
        comment.setDate(commentDto.getDate());
        comment.setContent(commentDto.getContent());
        comment.setStatus(requestStatus);

        commentRepository.save(comment);

        request.setStatus(comment.getStatus());
        if (comment.getStatus().getName().equals(ConferenceRequestStatusName.ACCEPTED)) {
            conferenceService.createConference(request);
        }
        ConferenceRequest entity = requestRepository.save(request);

        return ConferenceRequestMapper.toDto(entity);
    }

    @Override
    public Page<BriefConferenceRequestDto> findRequestsByUserId(Long id, Pageable pageable, Integer statusNumber) {
        if (statusNumber == null) {
            return requestRepository.findAllByOrganizerId(id, pageable).map(ConferenceRequestMapper::toBriefDto);
        } else {
            ConferenceRequestStatus status
                    = conferenceRequestStatusRepository.findByName(ConferenceRequestStatusName.fromInt(statusNumber));
            return requestRepository.findAllByOrganizerIdAndStatus(id, status, pageable).map(ConferenceRequestMapper::toBriefDto);
        }
    }

}
