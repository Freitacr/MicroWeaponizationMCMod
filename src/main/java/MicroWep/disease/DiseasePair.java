package MicroWep.disease;

import net.minecraft.entity.EntityLivingBase;

public class DiseasePair {

    private EntityLivingBase entity;
    private IDisease disease;

    public DiseasePair(EntityLivingBase entity, IDisease disease) {
        this.entity = entity;
        this.disease = disease;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public IDisease getDisease() {
        return disease;
    }

    public void setDisease(IDisease disease) {
        this.disease = disease;
    }

    public void setEntity(EntityLivingBase entity) {
        this.entity = entity;
    }
}
