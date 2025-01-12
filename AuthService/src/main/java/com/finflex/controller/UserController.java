package com.finflex.controller;


import com.finflex.dto.UserRequestDto;
import com.finflex.dto.UserResponseDto;
import com.finflex.entity.User;
import com.finflex.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.finflex.constants.RestApiList.*;


@RestController
@CrossOrigin("*")
@RequestMapping(USERS)
public class UserController {

    private final UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }

    @Operation(
            summary = "Getting All Users REST API",
            description = "REST API to get All users"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @GetMapping(GET_ALL_USERS)
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @Operation(
            summary = "Getting User REST API",
            description = "REST API to get single user by TCKN"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @GetMapping(GET_USER_BY_TCKN)
    public ResponseEntity<UserResponseDto> getUserByTckn(@RequestParam
                                                             @Pattern(regexp="(^[1-9][0-9]{10}$)",message = "geçersiz TCKN")
                                                             String tckn) {
        UserResponseDto user = userService.getUserByTckn(tckn);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(GET_USER_BY_USER_NUMBER + "/{userNo}")
    public ResponseEntity<UserResponseDto> getUserByUserNo(@PathVariable String userNo) {
        return ResponseEntity.ok(userService.findByPersonelNumber(userNo));
    }

    @Operation(
            summary = "Create User REST API",
            description = "REST API to create new User for FinFlex application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @PostMapping(CREATE_USER)
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        User createdUser = userService.createUser(userRequestDto);
        UserResponseDto responseDto = userService.getUserByTckn(createdUser.getTckn());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete User REST API",
            description = "REST API to delete existsing User for FinFlex application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"

            )
    }
    )
    @DeleteMapping(DEACTIVATE_USER_BY_TCKN)
    public ResponseEntity<Void> deleteUser(@RequestParam
                                               @Pattern(regexp="(^[1-9][0-9]{10}$)",message = "geçersiz TCKN")
                                               String tckn) {
        userService.deactivateUser(tckn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Update User REST API",
            description = "REST API to update User"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @PutMapping(UPDATE_USER)
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(userRequestDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}
