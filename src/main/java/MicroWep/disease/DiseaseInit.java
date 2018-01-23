package MicroWep.disease;

import MicroWep.MicroWeaponization;
import MicroWep.lib.ConstantsLib;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class DiseaseInit {

    public static ZombieFlu zFlu;

    public static void init(DiseaseManager manager) {
        zFlu = new ZombieFlu();
        manager.registerDisease(ConstantsLib.ZOMBIE_DISEASE, zFlu);
        MicroWeaponization.EAttackManager.registerDisease(zFlu, EntityZombie.class, EntityPlayerMP.class);
        MicroWeaponization.EAttackManager.registerDisease(zFlu, EntityZombieVillager.class, EntityPlayerMP.class);
        MicroWeaponization.EAttackManager.registerDisease(zFlu, EntityHusk.class, EntityPlayerMP.class);
    }


}
