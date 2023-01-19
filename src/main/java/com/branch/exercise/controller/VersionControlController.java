package com.branch.exercise.controller;

import com.branch.exercise.controller.dto.VersionControlDto;
import com.branch.exercise.service.GithubService;
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
    GithubService githubService;

    @GetMapping("/user/{userName}")
    public ResponseEntity<VersionControlDto> getUserDataForUsername(@PathVariable("userName") String username) {

        logger.info(String.format("Getting version control data for %s", username));

        return ResponseEntity.ok(githubService.getUserData(username));
    }

    @ExceptionHandler({NoGithubUserException.class})
    public ResponseEntity<String> exceptionHandler(HttpServletRequest req, NoGithubUserException e) {
        logger.error("Non-existent github user inputted", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
