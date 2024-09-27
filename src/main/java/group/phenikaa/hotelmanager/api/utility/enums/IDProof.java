package group.phenikaa.hotelmanager.api.utility.enums;

import group.phenikaa.hotelmanager.api.utility.interfaces.IStringProvider;

public enum IDProof implements IStringProvider {
    Driving_License("A government-issued license allowing the holder to drive."),
    Citizen_Identification_Card("A card issued by the government to identify a citizen."),
    Passport("A travel document issued by a country, certifying the identity and nationality of its holder."),
    National_ID_Card("An identification card issued by the government, often required for voting and other services."),
    Voter_ID("An identification document required to vote in elections."),
    Employee_ID("An identification card issued by an employer to their employees."),
    Student_ID("An identification card issued to students by educational institutions."),
    Social_Security_Card("A card issued by the government to track earnings and benefits for social security."),
    Military_ID("An identification card issued to members of the armed forces."),
    Health_Insurance_Card("A card that verifies an individual's health insurance coverage."),
    Resident_Permit("A document that allows a foreign national to reside in a country."),
    Utility_Bill("A bill from a utility company, often used as proof of address."),
    Bank_Statement("A document summarizing the transactions in a bank account over a certain period.");

    private final String description;

    IDProof(String description) {
        this.description = description;
    }

    @Override
    public String url() {
        return description;
    }
}

