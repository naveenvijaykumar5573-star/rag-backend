package com.ragplatform.service;

import com.ragplatform.dto.LoginRequest;
import com.ragplatform.dto.LoginResponse;
import com.ragplatform.dto.RegisterRequest;
import com.ragplatform.dto.RegisterResponse;
import com.ragplatform.entity.Tenant;
import com.ragplatform.entity.User;
import com.ragplatform.repository.TenantRepository;
import com.ragplatform.repository.UserRepository;
import com.ragplatform.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            TenantRepository tenantRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =====================================================
    // REGISTER
    // =====================================================

    public RegisterResponse register(RegisterRequest request) {

        // 1. Check email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Email already registered"
            );
        }

        // 2. Create tenant slug
        String slug = request.getTenantName()
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        // 3. Check tenant
        if (tenantRepository.existsBySlug(slug)) {
            throw new RuntimeException(
                    "Tenant already exists"
            );
        }

        // 4. Create tenant
        Tenant tenant = new Tenant(
                request.getTenantName(),
                slug
        );

        tenant = tenantRepository.save(tenant);

        // 5. Hash password
        String hashedPassword =
                passwordEncoder.encode(
                        request.getPassword()
                );

        // 6. Create user
        User user = new User(
                request.getName(),
                request.getEmail(),
                hashedPassword,
                tenant
        );

        user = userRepository.save(user);

        // 7. Return response
        return new RegisterResponse(
                user.getId(),
                tenant.getId(),
                user.getName(),
                user.getEmail(),
                tenant.getName()
        );
    }

    // =====================================================
    // LOGIN
    // =====================================================

    public LoginResponse login(LoginRequest request) {

        // 1. Find user by email
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        // 2. Verify password
        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        // 3. Get tenant
        Tenant tenant = user.getTenant();

        // 4. Generate JWT
        String token = jwtService.generateToken(
                user.getId().toString(),
                tenant.getId().toString(),
                user.getRole()
        );

        // 5. Return response
        return new LoginResponse(
                token,
                "Bearer",
                user.getId().toString(),
                tenant.getId().toString(),
                user.getRole()
        );
    }
}