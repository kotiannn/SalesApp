package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 07/31/2017.
 */

public class Statesli {


    private List<Statelist> statelist;

    public List<Statelist> getStatelist() {
        return statelist;
    }

    public void setStatelist(List<Statelist> statelist) {
        this.statelist = statelist;
    }

    public static class Statelist {
        private String id;
        private String name;

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
    }
}
