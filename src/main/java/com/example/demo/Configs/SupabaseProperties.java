package com.example.demo.Configs;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "supabase")
public class SupabaseProperties {
    private String url;
    private String key;
    private Bucket bucket = new Bucket();

    public static class Bucket {
        private String profile;
        private String item;

        public String getItem() {
			return item;
		}
		public void setItem(String item) {
			this.item = item;
		}
		public String getProfile() { return profile; }
        public void setProfile(String profile) { this.profile = profile; }
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public Bucket getBucket() { return bucket; }
    public void setBucket(Bucket bucket) { this.bucket = bucket; }
}
