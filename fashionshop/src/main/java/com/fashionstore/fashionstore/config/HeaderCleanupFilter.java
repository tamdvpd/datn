package com.fashionstore.fashionstore.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HeaderCleanupFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResp = (HttpServletResponse) response;

        // Gỡ các header gây xung đột
        httpResp.setHeader("Cross-Origin-Opener-Policy", null);
        httpResp.setHeader("Cross-Origin-Embedder-Policy", null);

        chain.doFilter(request, response);
    }
}
