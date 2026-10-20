package com.bangone.erm.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        // 1. Coba ambil dari hidden field 'originUrl' di form
        String originUrl = request.getParameter("originUrl");

        // 2. Jika hidden field tidak ada, baru coba Referer sebagai cadangan
        if (originUrl == null || originUrl.isEmpty()) {
            originUrl = request.getHeader("Referer");
        }

        // 3. Redirect kembali ke asal (Form Add/Edit)
        if (originUrl != null && !originUrl.isEmpty()) {
            return "redirect:" + originUrl;
        }

        // 4. Last resort: balik ke dashboard
        return "redirect:/dashboard";
    }
}