package com.aksigorta.timesheet.security;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

// NOT: Tüm "javax.servlet" import'ları "jakarta.servlet" olarak güncellendi.

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // Gerekli servisleri Spring'den istiyoruz (Dependency Injection).
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Bu metot, projemize gelen her bir istek için SADECE BİR KEZ çalışır.
     * Görevi, istekte geçerli bir JWT olup olmadığını kontrol etmektir.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        try {
            // 1. Gelen isteğin başlığından (Header) JWT'yi ayrıştır.
            String jwt = getJwtFromRequest(request);

            // 2. JWT'nin var olup olmadığını VE geçerli olup olmadığını kontrol et.
            //    tokenProvider.validateToken() metodu imza, son kullanma tarihi gibi kontrolleri yapar.
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // 3. Token geçerliyse, içinden kullanıcı adını (username) al.
                String username = tokenProvider.getUsernameFromJWT(jwt);

                // 4. Kullanıcı adını kullanarak veritabanından kullanıcının tüm detaylarını yükle.
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // 5. Spring Security'nin anlayacağı bir "Authentication" nesnesi oluştur.
                //    Bu, "Bu kullanıcıyı tanıdık ve sisteme giriş yapmış sayıyoruz" demektir.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Oluşturulan Authentication nesnesini SecurityContextHolder'a yerleştir.
                //    Bu işlemden sonra, Spring Security bu isteğin kimliğinin doğrulandığını bilir.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // Bir hata oluşursa, bunu logla.
            logger.error("Could not set user authentication in security context", ex);
        }

        // 7. İsteğin, zincirdeki bir sonraki filtreye veya hedefine (Controller) devam etmesini sağla.
        filterChain.doFilter(request, response);
    }

    /**
     * Gelen isteğin "Authorization" başlığından JWT'yi çıkaran yardımcı bir metot.
     * Token genellikle "Bearer <token>" formatında gelir.
     * @param request Gelen HTTP isteği
     * @return Sadece token kısmı veya bulunamazsa null.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // "Bearer " kısmını (ilk 7 karakter) atıp, sadece token'ı döndür.
            return bearerToken.substring(7);
        }
        return null;
    }
}