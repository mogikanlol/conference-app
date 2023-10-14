package com.nikolaev.user;

import com.nikolaev.conference.dto.BriefConferenceDto;
import com.nikolaev.conference_request.ConferenceRequestService;
import com.nikolaev.conference_request.dto.BriefConferenceRequestDto;
import com.nikolaev.security.JwtTokenUtil;
import com.nikolaev.submission.dto.BriefSubmissionDto;
import com.nikolaev.user.dto.BriefUserDto;
import com.nikolaev.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserResource {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final ConferenceRequestService conferenceRequestService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserDto user, Device device) {
        userService.save(user);
        return ResponseEntity.ok(userService.save(user));
//        if (userService.save(user)) {
//
////            userService.sendConfirmationEmail(user);
//
//
//            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
//                    user.getEmail(),
//                    user.getPassword()
//            ));
//
//            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
//            final String token = jwtTokenUtil.generateToken(userDetails, device);
//
////            return ResponseEntity.ok(new JwtAuthenticationResponse(token, userDetails.getUsername()));
//            return ResponseEntity.ok(new JwtAuthenticationResponse(token, userDetails));
//        }
//
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //    @RequestMapping(value = "/{id}/conferences", method = RequestMethod.GET)
//    public ResponseEntity getConferences(@PathVariable("id") Long id,
//                                         @RequestParam(value = "reviewable", required = false, defaultValue = "false")
//                                                 String reviewable) {
//        return ResponseEntity.ok(userService.getUserConferences(id, Boolean.valueOf(reviewable)));
//    }
    @GetMapping("/{id}/conferences")
    public List<BriefConferenceDto> getConferences(@PathVariable("id") Long id) {
        return userService.getUserConferences(id);
    }

    /*
    @RequestMapping(value = "/{id}/reviewer", method = RequestMethod.GET)
    public ResponseEntity isReviewer(@RequestParam(value = "conferenceId") Long conferenceId,
                                     @PathVariable("id") Long userId) {
        return ResponseEntity.ok(userService.isReviewer(userId, conferenceId));
    }

     */

    /*
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

     */


    @GetMapping("/{id}/submissions")
    public List<BriefSubmissionDto> getSubmissions(@PathVariable("id") Long userId) {
        return userService.getSubmissions(userId);
    }

    @GetMapping("/{id}/conference-requests")
    public Page<BriefConferenceRequestDto> getConferenceRequests(@PathVariable("id") Long id,
                                                                 @PageableDefault Pageable pageable,
                                                                 @RequestParam(name = "status", required = false) Integer statusNumber) {
        return conferenceRequestService.findRequestsByUserId(id, pageable, statusNumber);
    }

    @GetMapping
    public Page<BriefUserDto> getUsers(@RequestParam(name = "search", required = false) String searchString,
                                       @PageableDefault Pageable pageable) {
        if (searchString == null) {
            return userService.getUsers(pageable);
        } else if (!searchString.isEmpty()) {
            return userService.searchUsers(searchString, pageable);
        }
        return null;
    }

}
