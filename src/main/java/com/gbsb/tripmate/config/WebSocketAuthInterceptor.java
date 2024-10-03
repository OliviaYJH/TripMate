package com.gbsb.tripmate.config;

import com.gbsb.tripmate.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
@AllArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Authentication authentication = validateToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                accessor.setUser(authentication);
            }
        }
        return message;
    }

    private Authentication validateToken(String token) {
        try {
            String username = jwtUtil.getUsernameFromJWT(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token)) {
                return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            } else {
                throw new AccessDeniedException("Invallid JWT Token");
            }
        } catch (Exception e) {
            throw new RuntimeException("JWT 토큰 처리 중 오류 발생", e);
        }
    }
}
