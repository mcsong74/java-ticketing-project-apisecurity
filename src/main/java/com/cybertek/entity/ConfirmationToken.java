package com.cybertek.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="confirmation_email")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause="is_deleted=false")
public class ConfirmationToken extends BaseEntity{
    // this class is for email validation

    private  String token;

    @OneToOne(targetEntity = User.class) //make relation uni-directional
    @JoinColumn(name="user_id")
    private User user;

    private LocalDate expireDate;

    public boolean isTokenValid(LocalDate date){
        LocalDate now = LocalDate.now();
        return date.isEqual(now) || date.isEqual(now.plusDays(1));
    }

    //constructor, 1 arg, main constructor
    public ConfirmationToken(User user){
        this.user = user;
        expireDate=LocalDate.now().plusDays(1);
        token = UUID.randomUUID().toString();
    }





}
