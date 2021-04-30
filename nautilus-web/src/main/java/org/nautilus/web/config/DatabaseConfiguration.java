package org.nautilus.web.config;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.nautilus.web.listener.TrimStringMongoEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import lombok.Getter;
import lombok.Setter;

@Configuration
@EnableMongoAuditing
@EnableJpaAuditing
public class DatabaseConfiguration {
	
    @Getter
    @Setter
    @MappedSuperclass
    @EntityListeners(AuditingEntityListener.class)
    public static class BaseModel {

        @Id
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        private String id;
        
        @CreatedBy
        @Column(name = "created_by")
        private String createdBy;

        @CreatedDate
        @Column(name = "created_date")
        private Date createdDate;

        @LastModifiedBy
        @Column(name = "last_modified_by")
        private String lastModifiedBy;

        @LastModifiedDate
        @Column(name = "last_modified_date")
        private Date lastModifiedDate;
    }
    
	@Bean
	public TrimStringMongoEventListener trimStringMongoEventListener() {
	    return new TrimStringMongoEventListener();
	}
}
