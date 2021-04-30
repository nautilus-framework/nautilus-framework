package org.nautilus.web.feature.user.model;

import javax.persistence.Entity;

import org.nautilus.web.config.DatabaseConfiguration.BaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Settings extends BaseModel {

    private String decimalSeparator = "POINT";

    private int decimalPlaces = 4;

    private int maxExecutions = 2;

    private String language = "en_US";

    private String timeZone = "(GMT-03:00) America/Sao_Paulo";
}
