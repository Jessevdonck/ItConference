package com.springboot_javawebexamen;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_CODE = "errorCode";
    private static final String ERROR_MESSAGE = "errorMessage";

    @RequestMapping("/error")
    public String handleError(
            @RequestParam(name = "errorCode", required = false, defaultValue = "Onbekend") String errorCode,
            @RequestParam(name = "errorMessage", required = false, defaultValue = "Er trad een onverwachte fout op") String errorMessage,
            HttpServletRequest request,
            Model model) {

        if ("Onbekend".equals(errorCode)) {
            Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            if (status != null) {
                Integer statusCode = Integer.valueOf(status.toString());
                if (statusCode == HttpStatus.NOT_FOUND.value()) {
                    errorCode = "404";
                    errorMessage = "Pagina niet gevonden";
                } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                    errorCode = "403";
                    errorMessage = "Toegang geweigerd";
                } else {
                    errorCode = statusCode.toString();
                    errorMessage = "Er trad een onverwachte fout op";
                }
            }
        }

        model.addAttribute(ERROR_CODE, errorCode);
        model.addAttribute(ERROR_MESSAGE, errorMessage);

        return "error";
    }
}
