package MicroWep.disease.infection_managers;

import MicroWep.disease.IDisease;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class EntityAttackInfectionManager implements IInfectionManager{

    private static HashMap<Set<Object>, Set<IDisease>> registeredDiseases = new HashMap<>();

    public Set<IDisease> getValidDiseases(Class<? extends EntityLivingBase> attacker, Class<? extends EntityLivingBase> other) {
        HashSet<Object> keys = new HashSet<>();
        keys.add(attacker);
        keys.add(other);
        Set<IDisease> diseases = registeredDiseases.get(keys);
        if (diseases == null)
            return new HashSet<>();
        else
            return diseases;
    }

    public void registerDisease(IDisease disease, Class<? extends EntityLivingBase> attacker, Class<? extends EntityLivingBase> other) {
        HashSet<Object> keys = new HashSet<>();
        keys.add(attacker);
        keys.add(other);
        registeredDiseases.putIfAbsent(keys, new HashSet<>());
        Set<IDisease> currentDiseases = registeredDiseases.get(keys);
        currentDiseases.add(disease);
    }

}
