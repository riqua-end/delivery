package org.delivery.db.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;

@SuperBuilder // 부모가 가지고 있는 변수를 포함하는 Builder() 패턴을 생성
@Data
@EqualsAndHashCode(callSuper = true) // equals()와 hashCode() 메소드를 자동으로 생성하며, 상위 클래스의 필드도 비교하도록 설정합니다.
@Entity
@Table(name = "account")
public class AccountEntity extends BaseEntity {
}
