package geostat.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class FormioBaseEntity {

    @EmbeddedId
    private PpiId id;

    @Column(name = "JsonV")
    private String data;

    public PpiId getId() {
        return id;
    }

    public void setId(PpiId id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}