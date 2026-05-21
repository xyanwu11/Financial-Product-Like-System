package com.productlike.system.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.HtmlUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Configuration
public class XssFilterConfig {

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1);
        return registration;
    }

    public static class XssFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            chain.doFilter(new XssRequestWrapper((HttpServletRequest) request), response);
        }
    }

    public static class XssRequestWrapper extends HttpServletRequestWrapper {

        public XssRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return value != null ? HtmlUtils.htmlEscape(value) : null;
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) return null;
            String[] escaped = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                escaped[i] = HtmlUtils.htmlEscape(values[i]);
            }
            return escaped;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            String body = new BufferedReader(
                    new InputStreamReader(super.getInputStream(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            String filtered = escapeJsonValues(body);
            byte[] bytes = filtered.getBytes(StandardCharsets.UTF_8);

            return new ServletInputStream() {
                private final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

                @Override public int read() { return bais.read(); }
                @Override public boolean isFinished() { return bais.available() == 0; }
                @Override public boolean isReady() { return true; }
                @Override public void setReadListener(ReadListener readListener) {}
            };
        }

        private String escapeJsonValues(String json) {
            if (json == null || json.isEmpty()) return json;
            return json.replaceAll("\"([^\"]*)<([^\"]*?)>([^\"]*?)\"",
                    "\"$1&lt;$2&gt;$3\"");
        }
    }
}
