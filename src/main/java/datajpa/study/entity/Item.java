package datajpa.study.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)//필수
@NoArgsConstructor//필수
public class Item implements Persistable<String> {
    //임이의 id를 위해 추가 Persistable
    @Id
    private String id;
    @CreatedDate
    private LocalDateTime createdDate;
    //데이터 추가시 날짜 정보도 삽입

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdDate==null;
        //업데이트 없이 insert
    }
}
