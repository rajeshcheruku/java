package com.example.githubgistapi.model;

public class Gist {
    private String id;
    private String description;
    private String url;

    public Gist() {}

    public Gist(String id, String description, String url) {
        this.id = id;
        this.description = description;
        this.url = url;
    }

    public String getId() { 
        return id; 
    }
    
    public void setId(String id) { 
        this.id = id; 
    }

    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }

    public String getUrl() { 
        return url; 
    }
    
    public void setUrl(String url) { 
        this.url = url; 
    }

    @Override
    public String toString() {
        return "Gist{id='" + id + "', description='" + description + "', url='" + url + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gist gist = (Gist) o;
        return id != null ? id.equals(gist.id) : gist.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}