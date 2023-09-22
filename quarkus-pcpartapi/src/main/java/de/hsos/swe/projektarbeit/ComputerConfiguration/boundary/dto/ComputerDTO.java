package de.hsos.swe.projektarbeit.ComputerConfiguration.boundary.dto;

import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.CPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Case;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Fan;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.GPU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.Motherboard;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.PSU;
import de.hsos.swe.projektarbeit.ComputerParts.entity.Parts.RAM;

/**
 * @author Jannis Welkener
 */
public class ComputerDTO {

    public String pcName;
    public String pcOwner;

    public String id;

    public ComputerDTO(){}

    public ComputerDTO(GPU g,CPU c,RAM r,PSU p,Case c_,Motherboard m,Fan f,String id_){
        gpu=g;
        cpu=c;
        ram=r;
        psu=p;
        pc_case=c_;
        motherboard=m;
        fan=f;

        id=id_;
    }

    public boolean isPublic;

    public GPU gpu;
    public CPU cpu;
    public RAM ram;
    public PSU psu;
    public Case pc_case;
    public Motherboard motherboard;
    public Fan fan;

    public int commentAmount = 1;
    public double userRating = 5;

    // userRating/5 (General information)
    public static final int maxRating = 5;
}
