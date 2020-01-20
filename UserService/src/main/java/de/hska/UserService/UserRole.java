package de.hska.UserService;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class UserRole implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "type")
    private String typ;


    @Column(name = "level1")
    private int level;

    public UserRole() {
    }

    public UserRole(String typ, int level) {
        this.typ = typ;
        this.level = level;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTyp() {
        return this.typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
