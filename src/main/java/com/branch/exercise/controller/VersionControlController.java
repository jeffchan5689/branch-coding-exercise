package com.branch.exercise.controller;

import com.branch.exercise.controller.dto.VersionControlUserDto;
import com.branch.exercise.service.GithubServerErrorException;
import com.branch.exercise.service.VersionControlService;
import com.branch.exercise.service.NoGithubUserException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/version-control/")
public class VersionControlController {

    Logger logger = LoggerFactory.getLogger(VersionControlController.class);

    @Autowired
    VersionControlService versionControlService;

    @GetMapping("/users/{userName}")
    public ResponseEntity<VersionControlUserDto> getUserDataForUsername(@PathVariable("userName") String username) {

        logger.info(String.format("Getting version control data for %s", username));

        return ResponseEntity.ok(versionControlService.getUserData(username));
    }

    @ExceptionHandler({NoGithubUserException.class})
    public ResponseEntity<String> noUserFoundExceptionHandler(HttpServletRequest req, NoGithubUserException e) {
        logger.error("Non-existent github user inputted", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({GithubServerErrorException.class})
    public ResponseEntity<String> serverErrorExceptionHandler(HttpServletRequest req, NoGithubUserException e) {
        logger.error("Non-existent github user inputted", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NoGithubUserException.class})
    public ResponseEntity<String> clientErrorExceptionHandler(HttpServletRequest req, NoGithubUserException e) {
        logger.error("Non-existent github user inputted", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
