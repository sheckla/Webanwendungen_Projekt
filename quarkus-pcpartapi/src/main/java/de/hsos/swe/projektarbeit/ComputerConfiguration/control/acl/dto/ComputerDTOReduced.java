package de.hsos.swe.projektarbeit.ComputerConfiguration.control.acl.dto;

/**
 * @author Jannis Welkener
 */
public class ComputerDTOReduced {

    public String pcName;
    public String pcOwner;
    public String id;

    public ComputerDTOReduced(){}

    public String gpu;
    public String cpu;
    public String ram;
    public String psu;
    public String pc_case;
    public String motherboard;
    public String fan;

    public int commentAmount = 1;

    public double userRating = 5;

    public boolean isPublic;

    // userRating/5 (General information)
    public static final int maxRating = 5;
}
