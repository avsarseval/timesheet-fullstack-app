package com.aksigorta.timesheet.controller;

import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Bu sınıfın bir REST API controller'ı olduğunu ve JSON döneceğini belirtir.
@RequestMapping("/api/auth") // Bu controller'daki tüm adreslerin "/api/auth" ile başlayacağını söyler.
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // "/api/auth" ile "/register" birleşir ve tam adres "/api/auth/register" olur.
    // @PostMapping: Bu metodun sadece POST isteklerini dinleyeceğini söyler.
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // @RequestBody: Gelen JSON verisini bir User nesnesine dönüştürür.
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser); // Başarılı olursa, kaydedilen kullanıcıyı geri döner.
    }
}