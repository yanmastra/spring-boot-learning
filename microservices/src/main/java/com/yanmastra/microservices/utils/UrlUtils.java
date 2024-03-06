package com.yanmastra.microservices.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestClientException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UrlUtils {
    private UrlUtils() {
    }

    private static final Map<String, URL> urlMap = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(UrlUtils.class.getName());

    public static String call(String method, String stringUrl, String content, Map<String, String> headers) throws IOException {
        return call(method, stringUrl, content, headers, false);
    }
    public static String call(String method, String stringUrl, String content, Map<String, String> headers, boolean showError) throws IOException {

        URL url = null;
        if (urlMap.containsKey(stringUrl)) {
            url = urlMap.get(stringUrl);
        } else {
            url = new URL(stringUrl);
            urlMap.put(stringUrl, url);
        }

        int statusCode = 0;
        try (MyHttpConnection connection = new MyHttpConnection((HttpURLConnection) url.openConnection())){
            connection.setRequestMethod(method);
            connection.setUseCaches(false);

            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (!StringUtils.isBlank(content)) {
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(content.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }

            statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder respContent = new StringBuilder();

                    String line;
                    while ((line = in.readLine()) != null) {
                        respContent.append(line);
                    }
                    return respContent.toString();
                } catch (Exception e) {
                    if (showError) logger.error(e.getMessage(), e);
                }
            } else {
                if (showError) logger.error("status: " + statusCode);
            }
        } catch (Exception e) {
            if (showError) logger.error(e.getMessage(), e);
            else logger.error(e.getMessage());
            throw new RestClientException(e.getMessage(), e);
        }
        throw new RestClientException("Unauthorized");
    }

    static class MyHttpConnection implements AutoCloseable {
        private final HttpURLConnection connection;

        public MyHttpConnection(HttpURLConnection connection) {
            if (connection == null) throw new NullPointerException("Variable connection couldn't be null");
            this.connection = connection;
            this.connection.setDoOutput(true);
        }

        @Override
        public void close() throws Exception {
//            logger.error("closing connection");
            connection.disconnect();
        }

        public void setRequestMethod(String name) throws ProtocolException {
            connection.setRequestMethod(name);
        }

        public void setUseCaches(boolean b) {
            connection.setUseCaches(b);
        }

        public void setRequestProperty(String key, String s) {
            connection.setRequestProperty(key, s);
        }

        public void setConnectTimeout(int i) {
            connection.setConnectTimeout(i);
        }

        public void setReadTimeout(int i) {
            connection.setReadTimeout(i);
        }

        public OutputStream getOutputStream() throws IOException {
            return connection.getOutputStream();
        }

        public int getResponseCode() throws IOException {
            return connection.getResponseCode();
        }

        public InputStream getInputStream() throws IOException {
            return connection.getInputStream();
        }
    }
}
