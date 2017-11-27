package ch.wisv.payments.model;

public enum CommitteeEnum {

    AKCIE("Akcie"),
    ANNUCIE("Annucie"),
    BUSINESSTOUR("Business Tour"),
    CHIPCIE("CHipCie"),
    CHOCO("ChoCo"),
    COH("CoH"),
    COMMA("ComMA"),
    DIES("Dies"),
    EIWEIW("EIWEIW"),
    FACIE("Facie"),
    FLITCIE("Flitcie"),
    FILMCREW("FilmCrew"),
    GALACIE("Galacie"),
    ICOM("iCom"),
    LUCIE("Lucie"),
    LANCIE("LANcie"),
    MACHAZINE("MaCHazine"),
    MAPHYA("MaPhyA"),
    MATCH("MatCH"),
    MEISCIE("MeisCie"),
    REIS("Reis"),
    SJAARCIE("Sjaarcie"),
    SYMPOSIUM("Symposium"),
    VERDIEPCIE("Verdiepcie"),
    WIFI("WiFi"),
    W3CIE("W3Cie"),
    WIEWIE("WIEWIE"),
    WOCKY("Wocky!"),
    BESTUUR("Bestuur");

    private final String name;

    CommitteeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
