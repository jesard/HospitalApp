package net.thumbtack.school.hospital.endpoint;


import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.service.AdminService;
import net.thumbtack.school.hospital.dto.request.RegAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegAdminDtoResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AdminsEndpoint {

    AdminService adminService = new AdminService();

    @PostMapping(value = "/admins", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegAdminDtoResponse insertAdmin(@Valid @RequestBody RegAdminDtoRequest request, @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return adminService.registerAdmin(request, token);
    }

    @PutMapping(value = "/admins", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegAdminDtoResponse updateAdmin(@Valid @RequestBody UpdateAdminDtoRequest request, @CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) throws ServerException {
        return adminService.updateAdmin(request, token);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ErrorDtoResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//
//        List<Error> errors = new ArrayList<>();
//
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.add(new Error(error.getCode(), error.getField(), error.getDefaultMessage())));
//
//        return new ErrorDtoResponse(errors);
//    }


}
