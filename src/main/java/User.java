import org.hibernate.bytecode.internal.bytebuddy.BytecodeProviderImpl;

import javax.persistence.*;
import javax.sound.sampled.FloatControl;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "usr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "mesData")
    private String mesData=null;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(length = 1000000)
    private Map<String, String> oldTable = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(length = 1000000)
    private Map<String, String> newTable = new HashMap<>();

    @Column(name = "date")
    private LocalDate checkDate = LocalDate.now();

    public User() {
    }

    public void setMesData(String mesData) {
        this.mesData = mesData;
    }

    public void setOldTable(Map<String, String> oldTable) {
        this.oldTable = oldTable;
    }

    public void setNewTable(Map<String, String> newTable) {
        this.newTable = newTable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCheckDate(LocalDate checkDate) {
        this.checkDate = checkDate;
    }

    public String getMesData() {
        return mesData;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getOldTable() {
        return oldTable;
    }

    public Map<String, String> getNewTable() {
        return newTable;
    }

    public LocalDate getCheckDate() {
        return checkDate;
    }

}
