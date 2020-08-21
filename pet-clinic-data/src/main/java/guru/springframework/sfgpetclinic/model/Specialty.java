package guru.springframework.sfgpetclinic.model;

public class Specialty extends BaseEntity{

    public String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
