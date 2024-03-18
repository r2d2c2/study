package datajpa.study.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass//이클레스를 상속받은 엔티티는 테이블 생성
public class JpaBaseEntity {
    @Column(updatable = false)//수정 불가
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    @PrePersist
    public void perPersist(){
        LocalDateTime now=LocalDateTime.now();
        createdDate=now;
        updateDate=now;
    }
    @PreUpdate
    public void preUpdate(){//수정 되면 수정일 저장
        updateDate=LocalDateTime.now();
    }
//    @PostPersist //포스트 기반도 있다
//    @PostUpdate
}
