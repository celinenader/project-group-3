package com.ecse321.group3.tutorME.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(name="university")
public class University {

    @Id
    @Column
    private String university_name;

    @OneToMany(mappedBy = "universities", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "university-subject")
    private List<Subject> subjects;

    public University() {}

    

    public University(String university_name, List<Subject> subjects) {
		super();
		this.university_name = university_name;
		this.subjects = subjects;
	}


    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
