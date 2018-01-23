package MicroWep.disease;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public interface IDisease {

    boolean willInfect(InfectionPackage infectionParameters);

    void onInfection(EntityLivingBase entityIn, NBTTagCompound diseaseTag);

    DiseaseState onDiseaseTick(EntityLivingBase entityIn);

    void onDiseaseFinal(EntityLivingBase entityIn);

    void onDiseaseCure(EntityLivingBase entityIn);

    double getInfectionChance();

    boolean isEntityInfected(EntityLivingBase entityIn);


}
