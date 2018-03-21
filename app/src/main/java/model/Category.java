package model;

/**
 * Created by White_Orchard on 15/03/2018.
 */

public class Category {

    private String id;
    private String composite_id;
    private String name;
    private String user;
    private String url;

    public Category() {
    }

    public Category(String name, String user, String url) {
        this.id = String.valueOf((user + name + url).hashCode());
        this.composite_id = String.valueOf((user + name).hashCode());
        this.name = name;
        this.user = user;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComposite_id() {
        return composite_id;
    }

    public void setComposite_id(String composite_id) {
        this.composite_id = composite_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        if (!getId().equals(category.getId())) return false;
        if (!getComposite_id().equals(category.getComposite_id())) return false;
        if (!getName().equals(category.getName())) return false;
        if (!getUser().equals(category.getUser())) return false;
        return getUrl().equals(category.getUrl());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getComposite_id().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getUser().hashCode();
        result = 31 * result + getUrl().hashCode();
        return result;
    }
}
