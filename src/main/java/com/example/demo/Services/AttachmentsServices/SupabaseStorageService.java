
package com.example.demo.Services.AttachmentsServices;

import com.example.demo.Configs.SupabaseProperties;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class SupabaseStorageService {

    private final SupabaseProperties supabaseProperties;
    private final OkHttpClient client = new OkHttpClient();

    public SupabaseStorageService(
            SupabaseProperties supabaseProperties) {
        this.supabaseProperties = supabaseProperties;
    }

    private String getBaseUrl() {
        String url = supabaseProperties.getUrl();

        if (url == null || url.isBlank()) {
            throw new IllegalStateException(
                    "Supabase Project URL is missing.");
        }

        url = url.trim().replaceAll("/+$", "");

        if (!url.startsWith("https://")
                && !url.startsWith("http://")) {
            throw new IllegalStateException(
                    "Supabase URL must start with http:// or https://");
        }

        return url;
    }

    private String getKey() {
        String key = supabaseProperties.getKey();

        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "Supabase API key is missing.");
        }

        return key.trim();
    }

    public String uploadFile(
            String bucketName,
            String fileName,
            byte[] fileBytes,
            String contentType) {

        try {
            if (bucketName == null || bucketName.isBlank()
                    || fileName == null || fileName.isBlank()) {
                throw new IllegalArgumentException(
                        "Bucket name and file name are required.");
            }

            if (fileBytes == null || fileBytes.length == 0) {
                throw new IllegalArgumentException(
                        "File is empty.");
            }

            String baseUrl = getBaseUrl();
            String key = getKey();

            String url = baseUrl
                    + "/storage/v1/object/"
                    + bucketName + "/" + fileName;

            MediaType mediaType = MediaType.parse(
                    contentType != null
                            ? contentType
                            : "application/octet-stream");

            RequestBody requestBody =
                    RequestBody.create(fileBytes, mediaType);

            Request request = new Request.Builder()
                    .url(url)
                    .header("apikey", key)
                    .header("Authorization", "Bearer " + key)
                    .header("Content-Type",
                            mediaType.toString())
                    .post(requestBody)
                    .build();

            try (Response response =
                         client.newCall(request).execute()) {

                String responseBody = response.body() != null
                        ? response.body().string()
                        : "";

                if (!response.isSuccessful()) {
                    throw new IOException(
                            "Supabase upload failed. HTTP "
                            + response.code() + ": "
                            + responseBody);
                }
            }

            // This URL is publicly accessible only if the
            // bucket is configured as public.
            return baseUrl
                    + "/storage/v1/object/public/"
                    + bucketName + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to upload file to Supabase: "
                    + e.getMessage(), e);
        }
    }

    public void deleteFile(
            String bucketName,
            String fileName) {

        try {
            String url = getBaseUrl()
                    + "/storage/v1/object/"
                    + bucketName + "/" + fileName;

            Request request = new Request.Builder()
                    .url(url)
                    .header("apikey", getKey())
                    .header("Authorization",
                            "Bearer " + getKey())
                    .delete()
                    .build();

            try (Response response =
                         client.newCall(request).execute()) {

                String responseBody = response.body() != null
                        ? response.body().string()
                        : "";

                if (!response.isSuccessful()) {
                    throw new IOException(
                            "Supabase delete failed. HTTP "
                            + response.code() + ": "
                            + responseBody);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to delete file from Supabase: "
                    + e.getMessage(), e);
        }
    }
    
    /**
     * Check if a file exists in the specified bucket.
     * @return true if exists (HTTP 200), false if not exists (HTTP 404)
     * @throws RuntimeException for other HTTP errors or network issues
     */
    public boolean fileExists(String bucketName, String fileName) {
        try {
            if (bucketName == null || bucketName.isBlank()
                    || fileName == null || fileName.isBlank()) {
                throw new IllegalArgumentException("Bucket name and file name are required.");
            }

            String url = getBaseUrl()
                    + "/storage/v1/object/"
                    + bucketName + "/" + fileName;

            Request request = new Request.Builder()
                    .url(url)
                    .header("apikey", getKey())
                    .header("Authorization", "Bearer " + getKey())
                    .head()   // Key: use HEAD instead of GET, fetch only response headers, not file content
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 200) {
                    return true;
                }
                if (response.code() == 404) {
                    return false;
                }
                // Other status codes (403, 500, etc.) throw exception, let caller handle
                String body = response.body() != null ? response.body().string() : "";
                throw new IOException("Supabase existence check failed. HTTP "
                        + response.code() + ": " + body);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to check file existence on Supabase: "
                    + e.getMessage(), e);
        }
    }
    
    public boolean isValidSupabaseUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        try {
            URI uri = new URI(url.trim());

            // 1) must be absolute http/https
            String scheme = uri.getScheme();
            if (scheme == null
                    || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
                return false;
            }

            // 2) host must match the configured Supabase host
            String baseUrl = getBaseUrl(); // already validated in your class
			URI baseUri = new URI(baseUrl);
            String expectedHost = baseUri.getHost();
            if (expectedHost == null || !expectedHost.equalsIgnoreCase(uri.getHost())) {
                return false;
            }

            // 3) path must be a Supabase storage object path
            String path = uri.getPath();
            if (path == null) {
                return false;
            }

            // path must start with /storage/v1/object/ (optionally /public/)
            String prefix = "/storage/v1/object/";
            if (!path.startsWith(prefix)) {
                return false;
            }

            // strip optional /public/ segment
            String rest = path.substring(prefix.length());
            if (rest.startsWith("public/")) {
                rest = rest.substring("public/".length());
            }

            // rest must be "{bucket}/{path...}" → at least one "/" and non-empty parts
            String[] parts = rest.split("/", 2);
            if (parts.length < 2) {
                return false;                 // no bucket or no file path
            }
            if (parts[0].isBlank() || parts[1].isBlank()) {
                return false;
            }

            // 4) reject path traversal
            if (rest.contains("..")) {
                return false;
            }

            return true;

        } catch (URISyntaxException e) {
            return false;
        }
    }
    
}
