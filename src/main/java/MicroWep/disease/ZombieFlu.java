package MicroWep.disease;

import MicroWep.MicroWeaponization;
import MicroWep.disease.infection_managers.EntityAttackInfectionManager;
import MicroWep.disease.infection_managers.IInfectionManager;
import MicroWep.lib.ConstantsLib;
import MicroWep.lib.PotionsLib;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import static MicroWep.disease.DiseaseManager.random;


public class ZombieFlu implements IDisease{

    private static float blindnessChance = .001f;
    private static float miningFatigueChance = .007f;
    private static float temporaryEffectChance = .06f;
    private static float infectionChance = .4f;
    private static String diseaseID = "infection_zombie";

    public boolean willInfect(InfectionPackage infectionParameters) {
        IInfectionManager infectionSource = infectionParameters.getInfectionManager();
        if (infectionSource instanceof EntityAttackInfectionManager) {
            EntityLivingBase attacker = (EntityLivingBase)infectionParameters.getParameters()[0];
            EntityLivingBase other = (EntityLivingBase) infectionParameters.getParameters()[1];
            if (attacker instanceof EntityZombie && other instanceof EntityPlayer)
                return (other.getHealth() <= 20.0f && DiseaseManager.random.nextDouble() <= infectionChance);
        }
        return false;
    }

    public void onInfection(EntityLivingBase entityIn, NBTTagCompound diseaseTag) {
        diseaseTag.setLong(diseaseID, entityIn.getEntityWorld().getTotalWorldTime() + 36000);
    }

    public DiseaseState onDiseaseTick(EntityLivingBase entityIn) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer playerIn = (EntityPlayer) entityIn;
            double selectedChance = random.nextDouble();
            if (selectedChance <= blindnessChance) {
                applyEffect(playerIn, PotionsLib.BLINDNESS, Integer.MAX_VALUE, false);
            }
            else if (selectedChance <= miningFatigueChance) {
                applyEffect(playerIn, PotionsLib.MINING_FATIGUE, Integer.MAX_VALUE);
            }
            else if (selectedChance <= temporaryEffectChance) {
                int selectedEffect = random.nextInt(6);
                switch(selectedEffect) {
                    case 0:
                        applyEffect(playerIn, PotionsLib.HUNGER, 300);
                        break;
                    case 1:
                        applyEffect(playerIn, PotionsLib.POISON, 300);
                        break;
                    case 2:
                        applyEffect(playerIn, PotionsLib.NAUSEA, 40, false);
                        break;
                    case 3:
                        applyEffect(playerIn, PotionsLib.SLOWNESS, 300);
                        break;
                }
            }

            if(entityIn.getEntityWorld().getTotalWorldTime() >= ((NBTTagCompound)entityIn.getEntityData().getTag(ConstantsLib.MODID)).getLong(ConstantsLib.ZOMBIE_DISEASE)) {
                return DiseaseState.FULLINFECTION;
            }
            return DiseaseState.STABLE;
        }
        return DiseaseState.CURED;
    }

    public void onDiseaseFinal(EntityLivingBase entityIn) {
        EntityZombie toSpawn = new EntityZombie(entityIn.getEntityWorld());
        toSpawn.setPosition(entityIn.getPosition().getX(), entityIn.getPosition().getY(), entityIn.getPosition().getZ());
        entityIn.getEntityWorld().spawnEntity(toSpawn);
        entityIn.attackEntityFrom(DamageSource.GENERIC, entityIn.getHealth());
        MicroWeaponization.manager.removeAllDiseases(entityIn);
    }

    public void onDiseaseCure(EntityLivingBase entityIn) {
        MicroWeaponization.manager.removeDisease(entityIn, this, ConstantsLib.ZOMBIE_DISEASE);
    }

    private static void applyEffect(EntityLivingBase entityIn, Potion potionIn, int duration, boolean update, boolean respectCreative) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer affectedPlayer = (EntityPlayer)entityIn;
            if (affectedPlayer.isCreative() && respectCreative)
                return;
        }

        if (entityIn.isPotionActive(potionIn)) {
            if (update) {
                PotionEffect miningFatigue = entityIn.getActivePotionEffect(potionIn);
                entityIn.removePotionEffect(potionIn);
                entityIn.addPotionEffect(new PotionEffect(potionIn, duration, miningFatigue.getAmplifier() + 1));
            } else {
                entityIn.removePotionEffect(potionIn);
                entityIn.addPotionEffect(new PotionEffect(potionIn, duration));
            }
        } else {
            entityIn.addPotionEffect(new PotionEffect(potionIn, duration));
        }
    }

    private static void applyEffect(EntityLivingBase entityIn, Potion potionIn, int duration, boolean update) {
        applyEffect(entityIn, potionIn, duration, update, true);
    }

    private static void applyEffect(EntityLivingBase entityIn, Potion potionIn, int duration) {
        applyEffect(entityIn, potionIn, duration, true, true);
    }

    public double getInfectionChance() {
        return infectionChance;
    }

    @Override
    public boolean isEntityInfected(EntityLivingBase entityIn) {
        NBTTagCompound diseaseTag = MicroWeaponization.manager.getDiseaseTag(entityIn);
        return diseaseTag.hasKey(ConstantsLib.ZOMBIE_DISEASE);
    }
}
