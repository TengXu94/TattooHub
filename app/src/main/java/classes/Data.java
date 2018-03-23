package classes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by root on 08.03.18.
 */

public class Data {
    private Images images;
    private User user;
    private Object[] tags;

    public Object[] getTags() { return tags;}

    public Images getImages() {
        return images;
    }

    public User getUser() {
        return user;
    }

    public class User {

        private String profile_picture;

        private String full_name;

        public String getProfile_picture() {
            return profile_picture;
        }

        public String getFull_name() {
            return full_name;
        }
    }

    public class Images {

        private Standard_resolution standard_resolution;

        public Standard_resolution getStandard_resolution() {
            return standard_resolution;
        }

        public class Standard_resolution {

            private String url;

            public String getUrl() {
                return url;
            }
        }
    }


    public class Tags {
        private Collection<Objects> tags;

        public Collection<Objects> getTags() {
            return tags;
        }
    }
}
