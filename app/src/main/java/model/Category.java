package model;

/**
 * Created by White_Orchard on 15/03/2018.
 */

public class Category {

    private String id;
    private String name;
    private String user;
    private String url;

    public Category() {
    }

    public Category(String name, String user, String url) {
        this.id = String.valueOf((user + name).hashCode());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        if (getId() != null ? !getId().equals(category.getId()) : category.getId() != null)
            return false;
        if (getName() != null ? !getName().equals(category.getName()) : category.getName() != null)
            return false;
        if (getUser() != null ? !getUser().equals(category.getUser()) : category.getUser() != null)
            return false;
        return getUrl() != null ? getUrl().equals(category.getUrl()) : category.getUrl() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        return result;
    }
}
