    package com.example.modular_booking_system.user.controller;

    import com.example.modular_booking_system.user.model.User;
    import com.example.modular_booking_system.user.service.UserService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequiredArgsConstructor
    public class UserController {

        private final UserService userService;

        @GetMapping("/users/{id}")
        public ResponseEntity<User> getUser(@PathVariable Long id) {
            return ResponseEntity.ok(userService.findById(id));
        }

        @PostMapping("/users")
        public ResponseEntity<User> createUser(@RequestBody User user) {
            if (userService.existsByEmail(user.getContact().getEmail())) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(userService.save(user));
        }
    }
