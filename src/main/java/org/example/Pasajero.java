package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pasajero {
    @JsonProperty("_id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Surname")
    private String surname;

    @JsonProperty("Age")
    private int age;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("FamilyID")
    private String familyID;

    @JsonProperty("CivilStatus")
    private String civilStatus;

    @JsonProperty("Children")
    private int children;

    @JsonProperty("TripsAchieved")
    private int tripsAchieved;

    @JsonProperty("Salary")
    private double salary;

    @JsonProperty("nivelSalud")
    private int nivelSalud;

    @JsonProperty("healthMetrics")
    private HealthMetrics healthMetrics;

    public Pasajero() {
    }

    public Pasajero(String id, String name, String surname, int age, String gender, String familyID, String civilStatus, int children, int tripsAchieved, double salary, int nivelSalud) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.familyID = familyID;
        this.civilStatus = civilStatus;
        this.children = children;
        this.tripsAchieved = tripsAchieved;
        this.salary = salary;
        this.nivelSalud = nivelSalud;
    }

    public String getId() {
        return id;
    }

    public String Name() {
        return name;
    }

    public String Surname() {
        return surname;
    }

    public int getNivelSalud() {
        return nivelSalud;
    }

    public void setNivelSalud(int nivelSalud) {
        this.nivelSalud = nivelSalud;
    }

    public String getEstadoSalud() {
        if (nivelSalud >= 80) {
            return "Salud Perfecta";
        } else if (nivelSalud >= 60) {
            return "Salud Buena";
        } else if (nivelSalud >= 40) {
            return "Salud Estable";
        } else if (nivelSalud >= 20) {
            return "Salud Regular";
        } else {
            return "Salud Crítica";
        }
    }

    @Override
    public String toString() {
        return "Pasajero{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", familyID='" + familyID + '\'' +
                ", civilStatus='" + civilStatus + '\'' +
                ", children=" + children +
                ", tripsAchieved=" + tripsAchieved +
                ", salary=" + salary +
                ", nivelSalud=" + nivelSalud +
                '}';
    }


}