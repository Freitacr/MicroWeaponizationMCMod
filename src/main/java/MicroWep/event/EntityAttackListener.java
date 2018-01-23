package MicroWep.event;

import MicroWep.MicroWeaponization;
import MicroWep.disease.IDisease;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

public class EntityAttackListener {


    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote) {
            Entity affectedEntity = event.getEntity();
            Entity attackerEntity = event.getSource().getImmediateSource();
            if (affectedEntity instanceof EntityLivingBase && attackerEntity instanceof EntityLivingBase) {
                EntityLivingBase affected = (EntityLivingBase)affectedEntity;
                EntityLivingBase attacker = (EntityLivingBase)attackerEntity;
                Set<IDisease> validDiseases = MicroWeaponization.EAttackManager.getValidDiseases(attacker.getClass(), affected.getClass());
                for (IDisease disease : validDiseases) {
                    if (disease.getInfectionChance() >= affected.getRNG().nextDouble())
                    {
                        disease.onInfection(affected, MicroWeaponization.manager.getDiseaseTag(affected));
                        MicroWeaponization.manager.addDisease(affected, disease);
                    }
                }
            }
        }

    }
}
