package com.nikolaev.conference_request;

import com.nikolaev.conference_request.comment.ConferenceRequestCommentDto;
import com.nikolaev.conference_request.dto.BriefConferenceRequestDto;
import com.nikolaev.conference_request.dto.ConferenceRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class ConferenceRequestResource {

    private final ConferenceRequestService conferenceRequestService;

    @PostMapping
    public ConferenceRequestDto createRequest(@RequestBody ConferenceRequestDto requestDTO) {
        log.info("createRequest() is invoked");
        log.info(requestDTO.toString());
        conferenceRequestService.createRequest(requestDTO);
        return requestDTO;
    }

    @GetMapping
    public Page<BriefConferenceRequestDto> getAll(@RequestParam(name = "status", required = false) Integer statusNumber,
                                                  @PageableDefault Pageable pageable) {
        return conferenceRequestService.getAll(statusNumber, pageable);
    }

    @PutMapping
    public void update(@RequestBody ConferenceRequestDto requestDTO) {
        conferenceRequestService.update(requestDTO);
    }

    @GetMapping("{id}")
    public ConferenceRequestDto getRequest(@PathVariable("id") Long requestId) {
        return conferenceRequestService.getRequest(requestId);
    }

    @PostMapping("{id}/comments")
    public ConferenceRequestDto createComment(@RequestBody ConferenceRequestCommentDto commentDto,
                                              @PathVariable("id") Long requestId) {
        return conferenceRequestService.createComment(requestId, commentDto);
    }
}
