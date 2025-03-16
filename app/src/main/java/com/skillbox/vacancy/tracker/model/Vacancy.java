package com.skillbox.vacancy.tracker.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {

    private String id;
    private String source;
    private Region region;
    private Company company;

    @SerializedName("creation-date")
    private String creationDate;

    private String salary;
    @SerializedName("salary_min")
    private Integer salaryMin;
    @SerializedName("salary_max")
    private Integer salaryMax;

    @SerializedName("job-name")
    private String jobName;

    @SerializedName("vac_url")
    private String vacUrl;

    private String employment;
    private String schedule;
    private String duty;
    private Category category;
    private Requirement requirement;
    private Addresses addresses;

    @SerializedName("social_protected")
    private String socialProtected;

    private Term term;

    @SerializedName("contact_list")
    private List<Contact> contactList;

    @SerializedName("contact_person")
    private String contactPerson;

    @SerializedName("work_places")
    private int workPlaces;

    @SerializedName("code_profession")
    private String codeProfession;

    private String typicalPosition;
    private List<String> skills;
    private WorkPlaceType workPlaceType;
    private List<String> shift;
    private String conditions;

    private int trainingDays;
    private String currency;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Region {

        @SerializedName("region_code")
        private String regionCode;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Company {

        private String companycode;
        private String email;

        @SerializedName("hr-agency")
        private boolean hrAgency;

        private String inn;
        private String kpp;
        private String name;
        private String ogrn;
        private String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {

        private String specialisation;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Requirement {

        private String education;
        private String qualification;
        private String experience;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Addresses {

        private List<Address> address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {

        private String location;
        private String lng;
        private String lat;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Term {

        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contact {

        @SerializedName("contact_type")
        private String contactType;

        @SerializedName("contact_value")
        private String contactValue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkPlaceType {

        private boolean workPlaceForeign;
        private boolean workPlaceOrdinary;
        private boolean workPlaceQuota;
        private boolean workPlaceSpecial;
    }
}
