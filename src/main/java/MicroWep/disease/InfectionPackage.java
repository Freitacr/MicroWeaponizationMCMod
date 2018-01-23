package MicroWep.disease;

import MicroWep.disease.infection_managers.IInfectionManager;

public class InfectionPackage {

    private Object[] parameters;
    private IInfectionManager infectionManager;

    public InfectionPackage(IInfectionManager infectionManager, Object ... parameters) {
        this.parameters = parameters;
        this.infectionManager = infectionManager;
    }

    public IInfectionManager getInfectionManager() {
        return infectionManager;
    }

    public Object[] getParameters() {
        return parameters;
    }
}
