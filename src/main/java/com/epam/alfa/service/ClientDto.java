package com.epam.alfa.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto {

    private long id;
    private Risk riskProfile;

    public enum Risk {
        LOW(1),
        NORMAL(2),
        HIGH(3);

        int riskLevel;

        Risk(int riskLevel) {
            this.riskLevel = riskLevel;
        }

        public int getRiskLevel() {
            return riskLevel;
        }
    }
}
