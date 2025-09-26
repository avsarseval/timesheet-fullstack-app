package com.aksigorta.timesheet.security;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }


    // istekte geçerli jwt var mı oma bakıyor
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        try {
            // 1. Gelen isteğin başlığından (Header) JWT'yi ayrıştır.
            String jwt = getJwtFromRequest(request);

            // 2. JWT'nin var olup olmadığını VE geçerli olup olmadığını kontrol et.
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // 3. Token geçerliyse, içinden kullanıcı adını (username) al.
                String username = tokenProvider.getUsernameFromJWT(jwt);

                // 4. Kullanıcı adını kullanarak veritabanından kullanıcının tüm detaylarını yükle.
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // 5. Spring Security'nin anlayacağı bir "Authentication" nesnesi oluştur.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Oluşturulan Authentication nesnesini SecurityContextHolder'a yerleştir.
                //    Bu işlemden sonra, Spring Security bu isteğin kimliğinin doğrulandığını bilir.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        // 7. İsteğin, zincirdeki bir sonraki filtreye veya hedefine (Controller) devam etmesini sağla.
        filterChain.doFilter(request, response);
    }


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // "Bearer " kısmını (ilk 7 karakter) atıp, sadece token'ı döndür.
            return bearerToken.substring(7);
        }
        return null;
    }
}