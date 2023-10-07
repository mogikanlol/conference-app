package com.nikolaev.conference_request;

import com.nikolaev.conference_request.comment.ConferenceRequestCommentDto;
import com.nikolaev.conference_request.dto.BriefConferenceRequestDto;
import com.nikolaev.conference_request.dto.ConferenceRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class ConferenceRequestController {

    private final ConferenceRequestService conferenceRequestService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createRequest(@RequestBody ConferenceRequestDto requestDTO) {
        log.info("createRequest() is invoked");
        log.info(requestDTO.toString());
        conferenceRequestService.createRequest(requestDTO);
        return ResponseEntity.ok(requestDTO);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<BriefConferenceRequestDto> getAll(@RequestParam(value = "status", required = false) Integer statusNumber,
                                                  @PageableDefault Pageable pageable) {
        return conferenceRequestService.getAll(statusNumber, pageable);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody ConferenceRequestDto requestDTO) {
        conferenceRequestService.update(requestDTO);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ConferenceRequestDto getRequest(@PathVariable("id") Long requestId) {
        return conferenceRequestService.getRequest(requestId);
    }

    @RequestMapping(value = "{id}/comments", method = RequestMethod.POST)
    public ConferenceRequestDto createComment(@RequestBody ConferenceRequestCommentDto commentDto,
                                        @PathVariable("id") Long requestId) {
        return conferenceRequestService.createComment(requestId, commentDto);
    }
}
