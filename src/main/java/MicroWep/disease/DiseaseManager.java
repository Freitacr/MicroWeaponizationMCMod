package MicroWep.disease;

import MicroWep.lib.ConstantsLib;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DiseaseManager {

    public static Random random = new Random();
    private static double diseaseTickChance = .04;
    private static HashMap<EntityLivingBase, Set<IDisease>> activeDiseases = new HashMap<>();
    private HashSet<EntityLivingBase> checkedEntities = new HashSet<>();
    private int entityCheckedAmount = 0;

    private static HashMap<String, IDisease> registeredDiseases = new HashMap<>();

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if(!event.getWorld().isRemote) {
            if (event.getEntity() instanceof EntityLivingBase && !checkedEntities.contains(event.getEntity())) {
                entityCheckedAmount += 1;
                EntityLivingBase entity = (EntityLivingBase) event.getEntity();
                if(entity.getEntityData().hasKey(ConstantsLib.MODID)) {
                    NBTTagCompound diseaseTag = (NBTTagCompound) entity.getEntityData().getTag(ConstantsLib.MODID);
                    Set<String> keys = diseaseTag.getKeySet();
                    Set<IDisease> diseases = new HashSet<>();
                    for (String key : keys) {
                        IDisease disease = registeredDiseases.get(key);
                        System.out.println(registeredDiseases);
                        if (disease != null)
                            diseases.add(disease);
                    }
                    if (!diseases.isEmpty())
                        activeDiseases.put(entity, diseases);
                }
            }
            if (entityCheckedAmount == 20) {
                checkedEntities.removeIf((entityLivingBase) -> !entityLivingBase.isEntityAlive());
                entityCheckedAmount = 0;
                System.out.println(checkedEntities.size());
            }
        }
    }


    @SubscribeEvent
    public void onEntityLivingUpdate(LivingUpdateEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote) {
            if (event.getEntity() instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) event.getEntity();
                Set<IDisease> activeInfections = activeDiseases.get(entity);
                if (activeInfections != null) {
                    for (IDisease disease : activeInfections) {
                        if (infectionRoll()) {
                            DiseaseState state = disease.onDiseaseTick(entity);
                            if(state == DiseaseState.FULLINFECTION) {
                                disease.onDiseaseFinal(entity);
                                System.out.println("A disease has entered a critical phase...");
                            }
                            else if (state == DiseaseState.CURED)
                                disease.onDiseaseCure(entity);
                        }
                    }
                }
            }
        }
    }

    public static boolean infectionRoll() {
        return random.nextDouble() <= diseaseTickChance;
    }

    public boolean registerDisease(String diseaseID, IDisease disease) {
        IDisease currRegistered = this.registeredDiseases.put(diseaseID, disease);
        System.out.println("Registering Disease, Current Value: " + currRegistered);
        if (currRegistered != null) {
            registeredDiseases.put(diseaseID, currRegistered);
            return false;
        }
        System.out.println("Current Value of Registered Diseases: " + registeredDiseases);
        return true;
    }

    public void addDisease(EntityLivingBase entityIn, IDisease disease) {
        activeDiseases.putIfAbsent(entityIn, new HashSet<>());
        Set<IDisease> currentDiseases = activeDiseases.get(entityIn);
        currentDiseases.add(disease);
    }

    public void removeDisease(EntityLivingBase entityIn, IDisease disease, String diseaseID) {
        activeDiseases.get(entityIn).remove(disease);
        ((NBTTagCompound)entityIn.getEntityData().getTag(ConstantsLib.MODID)).removeTag(diseaseID);
        if (activeDiseases.get(entityIn).size() == 0) {
            activeDiseases.remove(entityIn);
            entityIn.getEntityData().removeTag(ConstantsLib.MODID);
        }
    }

    public NBTTagCompound getDiseaseTag(EntityLivingBase entityIn) {
        if (entityIn.getEntityData().hasKey(ConstantsLib.MODID))
            return (NBTTagCompound)entityIn.getEntityData().getTag(ConstantsLib.MODID);
        entityIn.getEntityData().setTag(ConstantsLib.MODID, new NBTTagCompound());
        return getDiseaseTag(entityIn);
    }

    public void removeAllDiseases (EntityLivingBase entityIn) {
        if(entityIn.getEntityData().hasKey(ConstantsLib.MODID)) {
            entityIn.getEntityData().removeTag(ConstantsLib.MODID);
            activeDiseases.remove(entityIn);
        }
    }
}

enum DiseaseState {
    STABLE,
    CURED,
    FULLINFECTION
}